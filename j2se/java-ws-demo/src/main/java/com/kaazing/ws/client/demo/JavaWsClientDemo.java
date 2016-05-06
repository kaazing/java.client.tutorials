package com.kaazing.ws.client.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.JMSException;

import com.kaazing.net.http.HttpRedirectPolicy;
import com.kaazing.net.ws.WebSocket;
import com.kaazing.net.ws.WebSocketFactory;
import com.kaazing.net.ws.WebSocketMessageReader;
import com.kaazing.net.ws.WebSocketMessageType;
import com.kaazing.net.ws.WebSocketMessageWriter;

public class JavaWsClientDemo {
	private WebSocketFactory wsFactory;
	private WebSocket webSocket;



	public JavaWsClientDemo(URI url) throws URISyntaxException, IOException {
		wsFactory = WebSocketFactory.createWebSocketFactory();
		wsFactory.setDefaultRedirectPolicy(HttpRedirectPolicy.ALWAYS);
		final URI wsUrl = url;
		Thread wsThread = new Thread() {
			public void run() {
				try {
					webSocket = wsFactory.createWebSocket(wsUrl);

					webSocket.connect();
					final WebSocketMessageReader messageReader = webSocket.getMessageReader();
					WebSocketMessageType type = null;

					while ((type = messageReader.next()) != WebSocketMessageType.EOS) {
						switch (type) {

						case TEXT:
							CharSequence text = messageReader.getText();
							System.out.println(">>> RESPONSE:" + text.toString());
							break;
						default:
							System.err.println("Received a message of unexpected type: " + type);
						}
					}

					System.out.println("CLOSED");
				} catch (Exception e) {
					System.err.println("Exception: " + e.getMessage());
				}
			}
		};
		wsThread.setName("WebSocket ConnectAndReceiveThread");
		wsThread.start();
	}

	public void disconnect() throws IOException {
		 webSocket.close(4002, "Closing with 4002");
	}

	public void sendMessage(String message) throws IOException {
		WebSocketMessageWriter messageWriter = webSocket.getMessageWriter();
		messageWriter.writeText(message);
		System.out.println("MESSAGE PUBLISHED: " + message);
	}

	public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException, JMSException {
		JavaWsClientDemo demo = new JavaWsClientDemo(new URI("ws://sandbox.kaazing.net/echo"));
		System.out.println("Kaazing Java WebSocket	 Demo App. Copyright (C) 2016 Kaazing, Inc.");
		System.out.println("Type the message to send or <exit> to stop.");
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String text = console.readLine();
			if (text.toLowerCase().equals("<exit>"))
				break;
			// Send as a text
			demo.sendMessage(text);
		}
		demo.disconnect();
	}

}
