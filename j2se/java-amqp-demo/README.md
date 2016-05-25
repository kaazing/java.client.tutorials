# Kaazing J2SE WebSocket AMQP Tutorial

This tutorial shows how J2SE console application can communicate over the web with an AMQP server via Kaazing WebSocket Gateway using Kaazing Java WebSocket Client library. The application publishes text messages via Kaazing Gateway to an AMQP server and listens to the messages from the AMQP server over WebSocket.
## Minimum Requirements for Running or Building Kaazing J2SE WebSocket AMQP tutorial

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
build/install/java-amqp-demo/bin/java-amqp-demo
```
or
```
build\install\java-amqp-demo\bin\java-amqp-demo.bat
```

## Interact with Kaazing Java WebSocket Client API

Detailed instructions to create Kaazing Java WebSocket AMQP Tutorial from scratch to be able to send and receive AMQP messages over WebSocket can be found [here](http://kaazing.com/doc/amqp/4.0/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java WebSocket AMQP Client library is available:

* [Kaazing AMQP](http://kaazing.com/doc/amqp/4.0/apidoc/client/java/amqp/client/index.html)
