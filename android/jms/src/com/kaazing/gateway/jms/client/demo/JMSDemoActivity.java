/**
 * Copyright (c) 2007-2015, Kaazing Corporation. All rights reserved.
 */

package com.kaazing.gateway.jms.client.demo;

import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kaazing.gateway.jms.client.demo.LoginDialogFragment.LoginDialogListener;
import com.kaazing.gateway.jms.client.util.Tracer;
import com.kaazing.gateway.jms.client.ConnectionDisconnectedException;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;
import com.kaazing.net.auth.BasicChallengeHandler;
import com.kaazing.net.auth.ChallengeHandler;
import com.kaazing.net.auth.LoginHandler;
import com.kaazing.net.ws.WebSocketFactory;

public class JMSDemoActivity extends FragmentActivity {

    private static String TAG = "com.kaazing.gateway.jms.client.android.demo";
    
    private Button connectBtn;
    private Button disconnectBtn;
    private Button subscribeBtn;
    private Button unsubscribeBtn;
    private Button sendBtn;
    private Button clearBtn;
    private CheckBox sendBinaryCheckBox;
    
    private EditText locationText;
    private EditText destinationText;
    private EditText messageText;
    
    private TextView logTextView;
    
    private JmsConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    
    private DispatchQueue dispatchQueue;
    
    private HashMap<String, ArrayDeque<MessageConsumer>> consumers = new HashMap<String, ArrayDeque<MessageConsumer>>();

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        
        connectBtn      = (Button)findViewById(R.id.connectBtn);
        disconnectBtn   = (Button)findViewById(R.id.disconnectBtn);
        subscribeBtn    = (Button)findViewById(R.id.subscribeBtn);
        unsubscribeBtn  = (Button)findViewById(R.id.unsubscribeBtn);
        sendBtn         = (Button)findViewById(R.id.sendBtn);
        clearBtn        = (Button)findViewById(R.id.clearBtn);
        sendBinaryCheckBox = (CheckBox)findViewById(R.id.sendBinaryCheckBox);
        
        locationText    = (EditText)findViewById(R.id.locationText);
        destinationText = (EditText)findViewById(R.id.destinationText);
        messageText = (EditText)findViewById(R.id.messageText);
        
        logTextView     = (TextView)findViewById(R.id.logView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        
        Tracer.DEBUG = true;
        Logger logger = Logger.getLogger("com.kaazing.gateway.jms.client");
        logger.setLevel(Level.FINE);
        
        
        if (connectionFactory == null) {
			try {
				connectionFactory = JmsConnectionFactory.createConnectionFactory();
				WebSocketFactory webSocketFactory = connectionFactory.getWebSocketFactory();
	            webSocketFactory.setDefaultChallengeHandler(createChallengehandler());
			} catch (JMSException e) {
				e.printStackTrace();
				logMessage("EXCEPTION: " + e.getMessage());
			}
        }
        
        connectBtn.setOnClickListener(new OnClickListener() {       	
			public void onClick(View v) {
				connectBtn.setEnabled(false);
				dispatchQueue = new DispatchQueue("DispatchQueue");
		        dispatchQueue.start();
		        dispatchQueue.waitUntilReady();
				connect();
			}			
		});
        
        disconnectBtn.setOnClickListener(new OnClickListener() {        	
			public void onClick(View v) {
				disconnect();
			}		
		});
        
        subscribeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String destinationName = destinationText.getText().toString();
				logMessage("SUBSCRIBE - " + destinationName);
				dispatchQueue.dispatchAsync(new Runnable() {
					public void run() {
						try {
							Destination destination = getDestination(destinationName);
							if (destination == null) {
								return;
							}
							MessageConsumer consumer = session.createConsumer(destination);
							ArrayDeque<MessageConsumer> consumersToDestination = consumers.get(destinationName);
							if (consumersToDestination == null) {
								consumersToDestination = new ArrayDeque<MessageConsumer>();
								consumers.put(destinationName, consumersToDestination);
							}
							consumersToDestination.add(consumer);
							consumer.setMessageListener(new DestinationMessageListener());
						} catch (JMSException e) {
							e.printStackTrace();
							logMessage("EXCEPTION: " + e.getMessage());
						}
					}
				});
			}
		});
        
        unsubscribeBtn.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				String destinationName = destinationText.getText().toString();
				logMessage("UNSUBSCRIBE - " + destinationName);
				ArrayDeque<MessageConsumer> consumersToDestination = consumers.get(destinationName);
				if (consumersToDestination == null) {
					return;
				}
				final MessageConsumer consumer = consumersToDestination.poll();
				if (consumer == null) {
					return;
				}
				dispatchQueue.dispatchAsync(new Runnable() {	
					public void run() {
						try {
							consumer.close();
						} catch (JMSException e) {
							e.printStackTrace();
							logMessage(e.getMessage());
						}
					}
				});
			}			
		});
        
        sendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final boolean sendBinary = sendBinaryCheckBox.isChecked();
				final String text = messageText.getText().toString();
				logMessage("SEND: " + text);
				dispatchQueue.dispatchAsync(new Runnable() {			
					public void run() {
						try {
							String destinationName = destinationText.getText().toString();
							MessageProducer producer = session.createProducer(getDestination(destinationName));
							Message message;
							if (sendBinary) {
								BytesMessage bytesMessage = session.createBytesMessage();
								bytesMessage.writeUTF(text);
								message = bytesMessage;
							}
							else {
								message = session.createTextMessage(text);
							}
							
							producer.send(message);
							producer.close();
						} catch (JMSException e) {
							e.printStackTrace();
							logMessage(e.getMessage());
						}
					}
				});
			}		
		});
        
        clearBtn.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				logTextView.setText("");
				
			}
		});
    }
    
    public void onPause() {
    	if (connection != null) {
    		dispatchQueue.dispatchAsync(new Runnable() {	
				@Override
				public void run() {
					try {
						connection.stop();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
    	}
    	super.onPause();
    }
    
    public void onResume() {
    	if (connection != null) {
    		dispatchQueue.dispatchAsync(new Runnable() {	
				@Override
				public void run() {
					try {
						connection.start();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
    	}
    	super.onResume();
    }
    
   public void onDestroy() {
	   if (connection != null) {
		   disconnect();
	   }
	   super.onDestroy();
   }

    private void connect() {
    	
    	logMessage("CONNECTING");
    	
    	// Since createConnection() is a blocking method which will not return until 
    	// the connection is established or connection fails, it is a good practice to 
    	// establish connection on a separate thread so that UI is not blocked.
    	dispatchQueue.dispatchAsync(new Runnable() {
			public void run() {
				try {
					String location = locationText.getText().toString();
					connectionFactory.setGatewayLocation(URI.create(location));
					connection = connectionFactory.createConnection();
					connection.start();
					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					logMessage("CONNECTED");
					connection.setExceptionListener(new ConnectionExceptionListener());	
					updateButtonsForConnected();
				} catch (Exception e) {
					updateButtonsForDisconnected();
					e.printStackTrace();
					logMessage("EXCEPTION: " + e.getMessage());
				}
			}
		});	
    }
    
    private void disconnect() {
    	logMessage("DISCONNECTING");
    	
    	dispatchQueue.removePendingJobs();
    	dispatchQueue.quit();
    	new Thread(new Runnable() {
			public void run() {
				try {
					connection.close();
					logMessage("DISCONNECTED");
				} catch (JMSException e) {
					e.printStackTrace();
					logMessage("EXCEPTION: " + e.getMessage());
				}
				finally {
					connection = null;
					updateButtonsForDisconnected();
				}
			}
		}).start();
    }
    
    private Destination getDestination(String destinationName) throws JMSException {
		Destination destination;
		if (destinationName.startsWith("/topic/")) {
			destination = session.createTopic(destinationName);
		}
		else if (destinationName.startsWith("/queue/")) {
			destination = session.createQueue(destinationName);
		}
		else {
			logMessage("Invalid destination name: " + destinationName);
			return null;
		}
		return destination;
			
    }
    
    
    private void logMessage(final String message) {
    	runOnUiThread(new Runnable() {		
			public void run() {
				// Clear log after 100 messages
				if (logTextView.getLineCount() > 100) {
					logTextView.setText(message);
				}
				else {
					logTextView.setText(message + "\n" + logTextView.getText());
				}
				
			}
		});
    }
    
    private void updateButtonsForConnected() {
    	runOnUiThread(new Runnable() {
			public void run() {
				connectBtn.setEnabled(false);
		    	disconnectBtn.setEnabled(true);
		    	subscribeBtn.setEnabled(true);
		    	unsubscribeBtn.setEnabled(true);
		    	sendBtn.setEnabled(true);
			}
		});
    }
    
    private void updateButtonsForDisconnected() {
    	runOnUiThread(new Runnable() {
			public void run() {
				connectBtn.setEnabled(true);
		    	disconnectBtn.setEnabled(false);
		    	subscribeBtn.setEnabled(false);
		    	sendBtn.setEnabled(false);
		    	unsubscribeBtn.setEnabled(false);
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
                	final LoginDialogFragment loginDialog = new LoginDialogFragment();
                	loginDialog.setListener(new LoginDialogListener() {
						public void onDismissed() {
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
                	
                	// wait until the dialog is dismissed
                	semaphore.acquire();
                	
                	if (loginDialog.isCancelled()) {
                		return null;
                	}
                	
                	username = loginDialog.getUsername();
                	password = loginDialog.getPassword().toCharArray();
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
    
    private class ConnectionExceptionListener implements ExceptionListener {

		public void onException(final JMSException exception) {
			logMessage(exception.getMessage());
			if (exception instanceof ConnectionDisconnectedException) {
				updateButtonsForDisconnected();
			}
		}
    }
    
    private class DestinationMessageListener implements MessageListener {
    	
		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
	                logMessage("RECEIVED TextMessage: " + ((TextMessage)message).getText());
	            }
	            else if (message instanceof BytesMessage) {
	                BytesMessage bytesMessage = (BytesMessage)message;

	                long len = bytesMessage.getBodyLength();
	                byte b[] = new byte[(int)len];
	                bytesMessage.readBytes(b);

	                logMessage("RECEIVED BytesMessage: " + hexDump(b));
	            }
	            else if (message instanceof MapMessage) {
	                MapMessage mapMessage = (MapMessage)message;
	                Enumeration mapNames = mapMessage.getMapNames();
	                while (mapNames.hasMoreElements()) {
	                    String key = (String)mapNames.nextElement();
	                    Object value = mapMessage.getObject(key);

	                    if (value == null) {
	                        logMessage(key + ": null");
	                    } else if (value instanceof byte[]) {
	                        byte[] arr = (byte[])value;
	                        StringBuilder s = new StringBuilder();
	                        s.append("[");
	                        for (int i = 0; i < arr.length; i++) {
	                            if (i > 0) {
	                                s.append(",");
	                            }
	                            s.append(arr[i]);
	                        }
	                        s.append("]");
	                        logMessage(key + ": "+ s.toString() + " (Byte[])");
	                    } else {
	                        logMessage(key + ": " + value.toString() + " (" + value.getClass().getSimpleName() + ")");
	                    }
	                }
	                logMessage("RECEIVED MapMessage: ");
	            }
	            else {
	                logMessage("UNKNOWN MESSAGE TYPE: "+message.getClass().getSimpleName());
	            }

			}
			catch (Exception ex) {
				ex.printStackTrace();
				logMessage("EXCEPTION: " + ex.getMessage());
			}
		}
		
		private String hexDump(byte[] b) {
	        if (b.length == 0) {
	            return "empty";
	        }

	        StringBuilder out = new StringBuilder();
	        for (int i=0; i < b.length; i++) {
	        	out.append(Integer.toHexString(b[i])).append(' ');
	        }
	        return out.toString();
	    }
    	
    }

}

