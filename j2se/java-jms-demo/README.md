# Kaazing J2SE WebSocket JMS Tutorial

This tutorial shows how J2SE console application can communicate over the web with an JMS server via Kaazing WebSocket Gateway using Kaazing Java WebSocket Client library. The application publishes text messages via Kaazing Gateway to a JMS server and listens to the messages from the JMS server over WebSocket.

## Minimum Requirements for Running or Building Kaazing J2SE WebSocket JMS tutorial

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

Detailed instructions to create Kaazing J2SE WebSocket JMS Tutorial from scratch to be able to send and receive JMS messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/jms_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java WebSocket JMS Client library is available:

* [Kaazing JMS](https://kaazing.com/doc/jms/4.0/apidoc/client/java/jms/index.html)
