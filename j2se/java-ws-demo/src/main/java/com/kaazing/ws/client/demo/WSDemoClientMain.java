package com.kaazing.ws.client.demo;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class WSDemoClientMain {
    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException, JMSException {
        JavaWsClientDemo demo = new JavaWsClientDemo(new URI("ws://sandbox.kaazing.net/echo"));
        switch (args.length){
            case 0:
                demo = new JavaWsClientDemo(new URI("ws://sandbox.kaazing.net/echo"));
                break;
            case 1:
                demo = new JavaWsClientDemo(new URI(args[0]));
                break;
            default:
                System.out.println("Possible usage of the Kaazing Java JMS Demo: \n" +
                        "   1. If you want to connect to our default URI and default credentials (guest/guest): \n" +
                        "       /java-ws-demo\n" +
                        "   2. If you want to connect to your own local Kaazing Gateway URI (ex: *ws://localhost:8000/jms*):\n" +
                        "       /java-ws-demo '{YOUR.GATEWAY.URI}' \n" +
                        "Please restart your the Kaazing Java AMQP Demo and input the correct parameters as stated above!");
                System.exit(1);
        }
        demo.handleConnection();
        System.out.println("Kaazing Java WS Demo App. Copyright (C) 2017 Kaazing, Inc.");
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
