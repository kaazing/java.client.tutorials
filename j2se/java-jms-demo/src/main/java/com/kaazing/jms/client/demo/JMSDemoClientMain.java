package com.kaazing.jms.client.demo;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by azaharia on 02.02.2017.
 */
public class JMSDemoClientMain {

    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException, JMSException {
        JavaJMSClientDemo demo = null;
        switch (args.length){
            case 0:
                demo = new JavaJMSClientDemo(new URI("ws://demos.kaazing.com/jms"), "", "");
                break;
            case 1:
                demo = new JavaJMSClientDemo(new URI(args[0]), "", "");
                break;
            case 2:
                demo = new JavaJMSClientDemo(new URI("ws://demos.kaazing.com/jms"), args[0], args[1]);
                break;
            case 3:
                demo = new JavaJMSClientDemo(new URI(args[0]), args[1], args[2]);
                break;
            default:
                System.out.println("Possible usage of the Kaazing Java JMS Demo: \n" +
                        "   1. If you want to connect to our default URI: \n" +
                        "       /java-jms-demo\n" +
                        "   2. If you want to connect to your own local Kaazing Gateway URI (ex: *ws://localhost:8000/jms*):\n" +
                        "       /java-jms-demo '{YOUR.GATEWAY.URI}' \n" +
                        "   3. If you want to use authentication with our default URI: \n" +
                        "       /java-jms-demo 'joe' `welcome` \n" +
                        "   4. If you have setup your gateway for authentication: \n" +
                        "       /java-jms-demo '{YOUR.GATEWAY.URI}' '{USERNAME}' '{PASSWORD}' \n" +
                        "Please restart your the Kaazing Java JMS Demo and input the correct parameters as stated above!");
                System.exit(1);
        }
        demo.handleConnection();
        System.out.println("Kaazing Java JMS Demo App. Copyright (C) 2017 Kaazing, Inc.");
        System.out.println("Type the message to send or <exit> to stop.");
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("User input: ");
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
