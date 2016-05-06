package com.kaazing.amqp.client.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.kaazing.net.ws.amqp.AmqpArguments;
import com.kaazing.net.ws.amqp.AmqpChannel;
import com.kaazing.net.ws.amqp.AmqpClient;
import com.kaazing.net.ws.amqp.AmqpClientFactory;
import com.kaazing.net.ws.amqp.AmqpProperties;
import com.kaazing.net.ws.amqp.ChannelAdapter;
import com.kaazing.net.ws.amqp.ChannelEvent;
import com.kaazing.net.ws.amqp.ConnectionEvent;
import com.kaazing.net.ws.amqp.ConnectionListener;

public class JavaAmqpClientDemo {
	private AmqpClient amqpClient;
	private AmqpChannel publishChannel = null;
	private AmqpChannel consumeChannel = null;
	private final String queueName = "queue" + new Random().nextInt();
	private final String exchangeName = "demo_exchange";
	private final String myConsumerTag = "clientkey";
	private final String routingKey = "broadcastkey";
	private final String virtualHost = "/";
	private String login;

	public JavaAmqpClientDemo(URI url, String login, String password) throws InterruptedException {
		this.login = login;
		AmqpClientFactory amqpClientFactory = AmqpClientFactory.createAmqpClientFactory();
		amqpClient = amqpClientFactory.createAmqpClient();
		System.out.println("CONNECTING: " + url + " " + login + "/" + password);

		final CountDownLatch connectionLatch = new CountDownLatch(1);
		amqpClient.addConnectionListener(new ConnectionListener() {

			public void onConnectionOpen(ConnectionEvent e) {
				System.out.println("CONNECTED...");
				connectionLatch.countDown();

			}

			public void onConnecting(ConnectionEvent e) {
				System.out.println("CONNECTING...");

			}

			public void onConnectionClose(ConnectionEvent e) {
				System.out.println("DISCONNECTING...");
				if (publishChannel != null) {
					publishChannel.closeChannel(0, "", 0, 0);
				}

				if (consumeChannel != null) {
					consumeChannel.closeChannel(0, "", 0, 0);
				}

			}

			public void onConnectionError(ConnectionEvent e) {
				System.err.println("CONNECTION ERROR! " + e.getMessage());
				System.exit(-1);
			}
		});
		amqpClient.connect(url.toString(), virtualHost, login, password);
		connectionLatch.await(10, TimeUnit.SECONDS);

		final CountDownLatch pubChannelLatch = new CountDownLatch(1);
		System.out.println("OPEN: Publish Channel");
		publishChannel = amqpClient.openChannel();
		publishChannel.addChannelListener(new ChannelAdapter() {
			@Override
			public void onClose(ChannelEvent e) {
				System.out.println("CLOSED: Publish Channel");
			}

			@Override
			public void onError(final ChannelEvent e) {
				System.err.println("ERROR: Publish Channel - " + e.getMessage());
				amqpClient.disconnect();
				System.exit(-1);
			}

			@Override
			public void onDeclareExchange(ChannelEvent e) {
				System.out.println("EXCHANGE DECLARED: " + exchangeName);
			}

			@Override
			public void onOpen(ChannelEvent e) {
				System.out.println("OPENED: Publish Channel");
				publishChannel.declareExchange(exchangeName, "fanout", false, false, false, null);
				pubChannelLatch.countDown();
			}
		});
		pubChannelLatch.await(10, TimeUnit.SECONDS);
		System.out.println("OPEN: Consume Channel");
		consumeChannel = amqpClient.openChannel();
		final CountDownLatch consumeChannelLatch = new CountDownLatch(1);
		consumeChannel.addChannelListener(new ChannelAdapter() {
			@Override
			public void onBindQueue(ChannelEvent e) {

				System.out.println("QUEUE BOUND: " + exchangeName + " - " + queueName);
			}

			@Override
			public void onClose(ChannelEvent e) {
				System.out.println("CLOSED: Consume Channel");
			}

			@Override
			public void onConsumeBasic(ChannelEvent e) {
				System.out.println("CONSUME FROM QUEUE: " + queueName);
			}

			@Override
			public void onDeclareQueue(ChannelEvent e) {
				System.out.println("QUEUE DECLARED: " + queueName);
			}

			@Override
			public void onError(final ChannelEvent e) {
				System.err.println("ERROR: Consume Channel - " + e.getMessage());
				amqpClient.disconnect();
				System.exit(-1);
			}

			@Override
			public void onMessage(final ChannelEvent e) {
				byte[] bytes = new byte[e.getBody().remaining()];
				e.getBody().get(bytes);
				final Long dt = (Long) e.getArgument("deliveryTag");
				final String value = new String(bytes, Charset.forName("UTF-8"));

				System.out.println(">>> MESSAGE RECEIVED: " + value);
				AmqpProperties props = e.getAmqpProperties();
				if (props != null) {
					AmqpArguments headers = props.getHeaders();

					if (headers != null) {
						System.out.println("Headers: " + headers.toString());
					}
					System.out.println("Properties " + (String) props.toString());

					// Acknowledge the message as we passed in a 'false' for
					// noAck in AmqpChannel.consumeBasic() call. If the
					// message is not acknowledged, the broker will keep
					// holding the message. And, as more and more messages
					// are held by the broker, it will eventually result in
					// an OutOfMemoryError.
					AmqpChannel channel = e.getChannel();
					channel.ackBasic(dt.longValue(), true);
				}
			}

			@Override
			public void onOpen(ChannelEvent e) {
				System.out.println("OPENED: Consume Channel");
				consumeChannel.declareQueue(queueName, false, false, false, false, false, null).bindQueue(queueName, exchangeName, routingKey, false, null).consumeBasic(queueName, myConsumerTag, false, false, false, false, null);
				consumeChannelLatch.countDown();
			}
		});
		consumeChannelLatch.await(10, TimeUnit.SECONDS);
	}

	public void disconnect() {
		this.amqpClient.disconnect();
	}

	public void sendMessage(String message) {
		
		ByteBuffer buffer = ByteBuffer.allocate(512);
		buffer.put(message.getBytes(Charset.forName("UTF-8")));
		buffer.flip();

		Timestamp ts = new Timestamp(System.currentTimeMillis());
		AmqpProperties props = new AmqpProperties();
		props.setMessageId("1");
		props.setCorrelationId("4");
		props.setAppId("AMQPDemo");
		props.setUserId(this.login);
		props.setContentType("text/plain");
		props.setContentEncoding("UTF-8");
		props.setPriority(6);
		props.setDeliveryMode(1);
		props.setTimestamp(ts);

		AmqpArguments customHeaders = new AmqpArguments();
		customHeaders.addInteger("headerKey1", 100);
		customHeaders.addLongString("headerKey2", "Header value");

		props.setHeaders(customHeaders);

		publishChannel.publishBasic(buffer, props, exchangeName, routingKey, false, false);
		System.out.println("MESSAGE PUBLISHED: " + message);
	}

	public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException {
		JavaAmqpClientDemo demo = new JavaAmqpClientDemo(new URI("ws://sandbox.kaazing.net/amqp091"), "guest", "guest");
		System.out.println("Kaazing Java AMQP Demo App. Copyright (C) 2016 Kaazing, Inc.");
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
