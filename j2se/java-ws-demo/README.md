# Kaazing J2SE WebSocket Echo Tutorial

This J2SE console app communicates over WebSocket with an `Echo` service hosted by Kaazing WebSocket Gateway. The app sends messages and receives messages with the `Echo` service over WebSocket.

## Minimum Requirements for Running or Building Kaazing J2SE WebSocket Echo tutorial

* Java 1.8
* Gradle 2.10 or higher

## Steps for Building and Running the Project

- Install gradle: follow the steps [here](https://gradle.org/gradle-download/).

- Build the application using gradle

```bash
cd <application root directory>
gradle installDist
```

- Run the application 

```bash
build/install/java-ws-demo/bin/java-ws-demo
```
or 
```
build\install\java-ws-demo\bin\java-ws-demo.bat
```
**NOTE** The application can be run in the folowing ways:
	1. If you want to connect to our defult URI:
	```
	/java-ws-demo
	```
	2. If you want to connect to your own local Kaazing Gateway URI (ex: *ws://localhost:8000/echo*):
	```
	/java-ws-demo '{YOUR.GATEWAY.URI}'
	```

**Note:** If you encounter an exception, try running the program as the root user (`sudo`).

## Interact with Kaazing Java AMQP Client API

Documentation that explains how to create a Kaazing Java AMQP application from scratch to send and receive AMQP messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/amqp_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java AMQP Client library is available:

* [Kaazing AMQP](http://kaazing.com/doc/5.0/amqp_client_docs/apidoc/client/java/amqp/client/index.html)

