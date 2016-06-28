# Kaazing J2SE AMQP Tutorial

This J2SE console app communicates over WebSocket with an AMQP server via Kaazing WebSocket Gateway. The app subscribes to a destination and publishes and receives messages.

## Minimum Requirements for Running or Building Kaazing J2SE AMQP tutorial

* Java 1.8
* Gradle 2.10 or higher

## Steps for Building and Running the Project

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
**Note:** If you encounter an exception, try running the program as the root user (`sudo`).

## Interact with Kaazing Java AMQP Client API

Documentation that explains how to create a Kaazing Java AMQP application from scratch to send and receive AMQP messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/amqp_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java AMQP Client library is available:

* [Kaazing AMQP](http://kaazing.com/doc/5.0/amqp_client_docs/apidoc/client/java/amqp/client/index.html)
