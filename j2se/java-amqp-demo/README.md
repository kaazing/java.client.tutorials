# Kaazing J2SE AMQP Tutorial

This J2SE console app communicates over WebSocket with an AMQP server via Kaazing WebSocket Gateway. The app subscribes to a destination and publishes and receives messages.

## Minimum Requirements for Running or Building Kaazing J2SE AMQP tutorial

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
build/install/java-amqp-demo/bin/java-amqp-demo
```
or
```
build\install\java-amqp-demo\bin\java-amqp-demo.bat
```

**NOTE:** The application can be run in the folowing ways:
- To connect to our default URI and default credentials (guest/guest):
```
/java-amqp-demo
```
- To connect to your own local Kaazing Gateway URI (ex: *ws://localhost:8000/zmqp*):
```
/java-jms-demo '{YOUR.GATEWAY.URI}'
```
- To use credentials with our default URI:
```
/java-aqmp-demo 'username' 'password'
```
- If you have setup your gateway for authentification:
```
/java-jms-demo '{YOUR.GATEWAY.URI}' '{USERNAME}' '{PASSWORD}' 
```

**Note:** If you encounter an exception, try running the program as the root user (`sudo`).

## Interact with Kaazing Java AMQP Client API

Documentation that explains how to create a Kaazing Java AMQP application from scratch to send and receive AMQP messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/amqp_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java AMQP Client library is available:

* [Kaazing AMQP](http://kaazing.com/doc/5.0/amqp_client_docs/apidoc/client/java/amqp/client/index.html)
