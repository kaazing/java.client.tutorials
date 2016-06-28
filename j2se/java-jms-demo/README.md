# Kaazing J2SE JMS Tutorial

This J2SE console app communicates over WebSocket with a JMS server via Kaazing WebSocket Gateway. The app publishes text messages via Kaazing Gateway to a JMS server and listens for messages from the JMS server.

## Minimum Requirements for Running or Building Kaazing J2SE JMS tutorial

* Java 1.8
* Gradle 2.10 or higher

## Steps for building and running the project

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

## Interact with Kaazing Java WebSocket Client API

Documentation that explains how to create a Kaazing Java JMS application from scratch to send and receive  messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/jms_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java WebSocket JMS Client library is available:

* [Kaazing JMS](https://kaazing.com/doc/5.0/jms_client_docs/apidoc/client/java/jms/index.html)
