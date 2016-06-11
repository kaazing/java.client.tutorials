/**
 * Copyright (c) 2007-2015, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.ws.client.demo;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;



import com.kaazing.net.auth.BasicChallengeHandler;
import com.kaazing.net.auth.ChallengeHandler;
import com.kaazing.net.auth.LoginHandler;
import com.kaazing.net.ws.WebSocket;
import com.kaazing.net.ws.WebSocketFactory;
import com.kaazing.net.ws.WebSocketMessageReader;
import com.kaazing.net.ws.WebSocketMessageType;
import com.kaazing.net.ws.WebSocketMessageWriter;
import com.kaazing.ws.client.demo.LoginDialogFragment.LoginDialogListener;

public class EchoActivity extends FragmentActivity {
	
    private TextView locationText;
    private TextView messageText;
    private TextView logTextView;
    private Button sendBtn;
    private Button connectBtn;
    private Button disconnectBtn;
    private Button clearBtn;
    private CheckBox sendBinaryCheckBox;
    
    private WebSocket webSocket;
    private DispatchQueue dispatchQueue;
    private boolean wasConnectedBeforePaused = false;
    private boolean closedExplicitly = false;
    private LoginDialogFragment loginDialog;

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        locationText = (TextView)findViewById(R.id.locationText);
        messageText = (TextView)findViewById(	R.id.messageText);
        logTextView = (TextView)findViewById(R.id.logView);
        sendBtn = (Button)findViewById(R.id.send);
        connectBtn = (Button)findViewById(R.id.connectBtn);
        disconnectBtn = (Button)findViewById(R.id.disconnectBtn);
        clearBtn = (Button)findViewById(R.id.clearBtn);
        sendBinaryCheckBox = (CheckBox)findViewById(R.id.sendBinaryCheckBox);


		locationText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String url= locationText.getText().toString();
				if (url.length()>0){
					connectBtn.setEnabled(true);
				}
				else{
					connectBtn.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

        connectBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				hideKeyboard();
            	connectBtn.setEnabled(false);
            	connect();
            }
        });

        sendBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				hideKeyboard();
            	final boolean sendBinary = sendBinaryCheckBox.isChecked();
            	dispatchQueue.dispatchAsync(new Runnable() {
					public void run() {
						try {
							WebSocketMessageWriter messageWriter = webSocket.getMessageWriter();
							if (sendBinary) {
								String messageToSend = messageText.getText().toString();
								ByteBuffer payload = ByteBuffer.wrap(messageToSend.getBytes());
			                    logMessage("SEND BINARY:" + getHexDump(payload));
			                    messageWriter.writeBinary(payload);
							}
							else {
								logMessage("SEND: " + messageText.getText());
			                    messageWriter.writeText(messageText.getText());
							}
		                } catch (Exception e) {
		                    e.printStackTrace();
		                    logMessage(e.getMessage());
		                }
					}
				});
            }
        });
        
        disconnectBtn.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				disconnect();
			}
		});
        
        clearBtn.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				logTextView.setText("");
			}
		});

    }
    
    public void onPause() {
    	if (webSocket != null) {
    		wasConnectedBeforePaused = true;
    		disconnect();
    	}
    	super.onPause();
    }
    
    public void onResume() {
    	if (wasConnectedBeforePaused) {
    		wasConnectedBeforePaused = false;
    		connect();
    	}
    	super.onResume();
    }
    
   public void onDestroy() {
	   if (webSocket != null) {
	   		disconnect();
	   	}
   		super.onDestroy();
   }
    
    private void connect() {
    	closedExplicitly = false;
    	logMessage("CONNECTING");
    	// initialize dispatch queue that will be used to run 
    	// blocking calls asynchronously on a separate thread
    	dispatchQueue = new DispatchQueue("Async Dispatch Queue");
        dispatchQueue.start();
        dispatchQueue.waitUntilReady();
        
    	// Since WebSocket.connect() is a blocking method which will not return until 
    	// the connection is established or connection fails, it is a good practice to 
    	// establish connection on a separate thread so that UI is not blocked.
    	dispatchQueue.dispatchAsync(new Runnable() {
			public void run() {
				try {
                	WebSocketFactory webSocketFactory = WebSocketFactory.createWebSocketFactory();
        			webSocket = webSocketFactory.createWebSocket(URI.create(locationText.getText().toString()));
        			webSocket.setChallengeHandler(createChallengehandler());
        			webSocket.connect();
        			logMessage("CONNECTED");
        			WebSocketMessageReader messageReader = webSocket.getMessageReader();
        			MessageReceiver messageReceiver = new MessageReceiver(messageReader);
        			new Thread(messageReceiver).start();
        			updateButtonsForConnected();
        		} catch (Exception e) {
        			updateButtonsForDisconnected();
        			logMessage(e.getMessage());
        			dispatchQueue.quit();
        		}
			}
		});
    }
    
    private void disconnect() {
    	closedExplicitly = true;
    	disconnectBtn.setEnabled(false);
    	logMessage("DISCONNECTING");
    	
    	dispatchQueue.removePendingJobs();
    	dispatchQueue.quit();
    	
    	new Thread(new Runnable() {
			public void run() {
				try {
					webSocket.close();
					logMessage("DISCONNECTED");
				} catch (IOException e) {
					logMessage(e.getMessage());
				}
				finally {
					webSocket = null;
					updateButtonsForDisconnected();
				}
			}
		}).start();
    }
    
    private void updateButtonsForConnected() {
    	runOnUiThread(new Runnable() {
			public void run() {
				connectBtn.setEnabled(false);
    			sendBtn.setEnabled(true);
    			disconnectBtn.setEnabled(true);
			}
		});
    }
    
    private void updateButtonsForDisconnected() {
    	runOnUiThread(new Runnable() {
			public void run() {
				connectBtn.setEnabled(true);
				disconnectBtn.setEnabled(false);
				sendBtn.setEnabled(false);
			}
		});
    }
    
    private void logMessage(final String logMessage) {
        runOnUiThread(new Runnable() {
            public void run() {
                logTextView.setText(logMessage + "\n" + logTextView.getText());
            }
        });
    }
    
    private ChallengeHandler createChallengehandler() {
    	final LoginHandler loginHandler = new LoginHandler() {
            private String username;
            private char[] password;
            @Override
            public PasswordAuthentication getCredentials() {
                try {
                	final Semaphore semaphore = new Semaphore(1);
                	
                	// Acquire semaphore so that subsequent acquire will block until released.
            		// This is used to wait until the login dialog is dismissed
                	semaphore.acquire();
                	loginDialog = new LoginDialogFragment();
                	loginDialog.setListener(new LoginDialogListener() {
						public void onDismissed() {
							if(!loginDialog.isCancelled()) {
								username = loginDialog.getUsername();
			                	password = loginDialog.getPassword().toCharArray();
							}
							loginDialog = null;
							semaphore.release();
						}
					});
                	runOnUiThread(new Runnable() {				
						public void run() {
							loginDialog.show(getSupportFragmentManager(), "Login Dialog Fragment");
							loginDialog.getFragmentManager().executePendingTransactions();
							loginDialog.getDialog().setCanceledOnTouchOutside(false);
						}
					});
                	
                	// This will cause the thread to block until the OK or Cancel button is clicked
                	semaphore.acquire();
                	if (loginDialog.isCancelled()) {
                		return null;
                	}
                	
                } catch (Exception e) {
                    e.printStackTrace();
                }
            	return new PasswordAuthentication(username, password);
            }
        };
        BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
        challengeHandler.setLoginHandler(loginHandler);
        return challengeHandler;
    }
    
    private String getHexDump(ByteBuffer buf) {
        if (buf.position() == buf.limit()) {
            return "empty";
        }

        StringBuilder hexDump = new StringBuilder();
        for (int i = buf.position(); i < buf.limit(); i++) {
            hexDump.append(Integer.toHexString(buf.get(i)&0xFF)).append(' ');
        }
        return hexDump.toString();
    }
    
    private class MessageReceiver implements Runnable {
    	
    	private WebSocketMessageReader messageReader;
    	
    	private MessageReceiver(WebSocketMessageReader reader) {
    		this.messageReader = reader;
    	}

		public void run() {
			WebSocketMessageType type = null;
	        try {
	            while ((type = messageReader.next()) != WebSocketMessageType.EOS) {
	                switch (type) {
	                    case BINARY:
	                        ByteBuffer data = messageReader.getBinary();
	                        logMessage("RECEIVED: " + getHexDump(data));
	                        break;
	                    case TEXT:
	                        CharSequence text = messageReader.getText();
	                        logMessage("RECEIVED: " + text.toString());
	                        break;
	                }
	            }
	            if (!closedExplicitly) {
	            	
	            	// Connection got closed due to either of the cases
	            	// - Server closing the connection because of authentication time out
	            	// - network failure
	            	webSocket = null;
	            	logMessage("Connection Closed!!");
	            	updateButtonsForDisconnected();
	            	if (loginDialog != null && !loginDialog.isHidden()) {
	            		loginDialog.cancel();
	            	}
	            }
	        }
	        catch (Exception ex) {
	        	ex.printStackTrace();
	        	logMessage(ex.getMessage());
	        }
		}
    	
    }
}