# Kaazing J2SE JMS Tutorial

This J2SE console app communicates over WebSocket with a JMS server via Kaazing WebSocket Gateway. The app publishes text messages via Kaazing Gateway to a JMS server and listens for messages from the JMS server.

## Minimum Requirements for Running or Building Kaazing J2SE JMS tutorial

* Java 1.8
* Gradle 2.10 or higher

## Steps for building and running the project

- Install gradle: follow the steps [here](https://gradle.org/gradle-download/).

- Build the application using gradle

```bash
cd <application root directory>
gradle installDist
```
- Run the application 

```bash
build/install/java-jms-demo/bin/java-jms-demo
```
or
```
build\install\java-jms-demo\bin\java-jms-demo.bat
```
**NOTE** The application can be run in the folowing ways:
	1. If you want to connect to our defult URI:
	```
	/java-jms-demo
	```
	2. If you want to connect to your own local Kaazing Gateway URI (ex: *ws://localhost:8000/jms*):
	```
	/java-jms-demo '{YOUR.GATEWAY.URI}'
	```
	3. If you want to use credentials with our default URI:
	```
	/java-jms-demo 'joe' `welcome`
	```
	4. If you have setup your gateway for authentification:
	```
	/java-jms-demo '{YOUR.GATEWAY.URI}' '{USERNAME}' '{PASSWORD}' 
	```

## Interact with Kaazing Java WebSocket Client API

Documentation that explains how to create a Kaazing Java JMS application from scratch to send and receive  messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/jms_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java WebSocket JMS Client library is available:

* [Kaazing JMS](https://kaazing.com/doc/5.0/jms_client_docs/apidoc/client/java/jms/index.html)
