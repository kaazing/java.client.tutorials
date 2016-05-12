package com.kaazing.jms.client.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.kaazing.gateway.jms.client.JmsConnectionFactory;
import com.kaazing.gateway.jms.client.JmsInitialContext;
import com.kaazing.net.http.HttpRedirectPolicy;
import com.kaazing.net.ws.WebSocketFactory;

public class JavaJMSClientDemo {
	private InitialContext jndiInitialContext;
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;

	private final String subTopicName = "echo";
	private final String pubTopicName = "echo";
	private MessageProducer producer;
	private MessageConsumer consumer;

	public JavaJMSClientDemo(URI url, String login, String password) throws JMSException {

		Properties env = new Properties();
		env.setProperty("java.naming.factory.initial", "com.kaazing.gateway.jms.client.JmsInitialContextFactory");
		try {
			jndiInitialContext = new InitialContext(env);
		} catch (NamingException e1) {
			throw new RuntimeException("Error creating initial context factory for JMS!", e1);
		}
		env.put(JmsInitialContext.CONNECTION_TIMEOUT, "15000");
		try {
			connectionFactory = (ConnectionFactory) jndiInitialContext.lookup("ConnectionFactory");
		} catch (NamingException e) {
			throw new RuntimeException("Error locating connection factory for JMS!", e);
		}
		JmsConnectionFactory jmsConnectionFactory = (JmsConnectionFactory) connectionFactory;
		jmsConnectionFactory.setGatewayLocation(url);
		WebSocketFactory webSocketFactory = jmsConnectionFactory.getWebSocketFactory();
		webSocketFactory.setDefaultRedirectPolicy(HttpRedirectPolicy.ALWAYS);
		try {
			connection = connectionFactory.createConnection(login, password);
		} catch (JMSException e) {
			throw new RuntimeException("Error connecting to gateway with " + url.toString() + ", credentials " + login + "/" + password, e);
		}
		try {
			connection.setExceptionListener(new ExceptionListener() {

				public void onException(JMSException exception) {
					System.err.println("JMS Exception occurred " + exception.getMessage());

				}
			});
		} catch (JMSException e) {
			throw new RuntimeException("Error setting exceptions listener. Connection: " + url.toString() + ", credentials " + login + "/" + password, e);
		}
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			connection.close();
			throw new RuntimeException("Error creating session. Connection: " + url.toString() + ", credentials " + login + "/" + password, e);
		}
		try {
			connection.start();
		} catch (JMSException e) {
			throw new RuntimeException("Error starting connection: " + url.toString() + ", credentials " + login + "/" + password, e);
		}
		System.out.println("Connected to " + url.toString());
		Destination subDestination;
		try {
			subDestination = (Destination) jndiInitialContext.lookup("/topic/" + subTopicName);
		} catch (NamingException e) {
			connection.stop();
			connection.close();
			throw new RuntimeException("Cannot locate subscription topic " + subTopicName, e);
		}
		try {

			consumer = session.createConsumer(subDestination);
		} catch (JMSException e) {
			session.close();
			connection.stop();
			connection.close();
			throw new RuntimeException("Cannot create consumer for subscription topic " + subTopicName, e);
		}
		System.out.println("Created subscription to " + subTopicName + " for connection to " + url);
		try {
			consumer.setMessageListener(new MessageListener() {

				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						try {
							System.out.println(">>> MESSAGE RECEIVED: " + ((TextMessage) message).getText());
						} catch (JMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.err.println("Received message of an unexpected type " + message.getClass().getName());
					}

				}
			});
		} catch (JMSException e) {
			session.close();
			connection.stop();
			connection.close();
			throw new RuntimeException("Cannot create messages listener for subscription topic " + subTopicName, e);

		}
		Destination pubDestination;
		try {
			pubDestination = (Destination) jndiInitialContext.lookup("/topic/" + pubTopicName);
		} catch (NamingException e) {
			consumer.close();
			session.close();
			connection.stop();
			connection.close();

			throw new RuntimeException("Cannot locate publishing topic " + pubTopicName, e);
		}
		try {
			producer = session.createProducer(pubDestination);
		} catch (JMSException e) {
			consumer.close();
			session.close();
			connection.stop();
			connection.close();
			throw new RuntimeException("Cannot create producer for publishing topic " + pubTopicName, e);
		}
	}

	public void disconnect() throws JMSException {
		producer.close();
		consumer.close();
		session.close();
		connection.stop();
		connection.close();

	}

	public void sendMessage(String message) {
		TextMessage textMessage;
		try {
			textMessage = session.createTextMessage(message);
			producer.send(textMessage);
		} catch (JMSException e) {
			System.err.println("Error sending message [" + message + "]! " + e.getMessage());
		}
		System.out.println("MESSAGE PUBLISHED: " + message);
	}

	public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException, JMSException {
		JavaJMSClientDemo demo = new JavaJMSClientDemo(new URI("wss://sandbox.kaazing.net/jms"), "", "");
		System.out.println("Kaazing Java JMS Demo App. Copyright (C) 2016 Kaazing, Inc.");
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
