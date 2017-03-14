package com.kaazing.amqp.client.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class AmqpDemoClientMain {
    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException {
        JavaAmqpClientDemo demo = null;
        switch (args.length){
            case 0:
                demo = new JavaAmqpClientDemo("ws://sandbox.kaazing.net/amqp091", "guest", "guest");
                break;
            case 1:
                demo = new JavaAmqpClientDemo(args[0], "guest", "guest");
                break;
            case 2:
                demo = new JavaAmqpClientDemo("ws://sandbox.kaazing.net/jms", args[0], args[1]);
                break;
            case 3:
                demo = new JavaAmqpClientDemo(args[0], args[1], args[2]);
                break;
            default:
                System.out.println("Possible usage of the Kaazing Java JMS Demo: \n" +
                        "   1. If you want to connect to our default URI and default credentials (guest/guest): \n" +
                        "       /java-amqp-demo\n" +
                        "   2. If you want to connect to your own local Kaazing Gateway URI (ex: *ws://localhost:8000/jms*):\n" +
                        "       /java-amqp-demo '{YOUR.GATEWAY.URI}' \n" +
                        "   3. If you want to use authentication with our default URI: \n" +
                        "       /java-aqmp-demo 'username' `password` \n" +
                        "   4. If you have setup your gateway for authentication: \n" +
                        "       /java-amqp-demo '{YOUR.GATEWAY.URI}' '{USERNAME}' '{PASSWORD}' \n" +
                        "Please restart your the Kaazing Java AMQP Demo and input the correct parameters as stated above!");
                System.exit(1);
        }
        demo.handleConnection();
        System.out.println("Kaazing Java AMQP Demo App. Copyright (C) 2017 Kaazing, Inc.");
        System.out.println("Type the message to send or <exit> to stop.");
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String text = console.readLine();
            if (text.equalsIgnoreCase("<exit>"))
                break;
            // Send as a text
            demo.sendMessage(text);
        }
        demo.disconnect();
    }
}
