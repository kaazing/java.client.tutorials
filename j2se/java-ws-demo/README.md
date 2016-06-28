# Kaazing J2SE WebSocket Echo Tutorial

This J2SE console app communicates over WebSocket with an `Echo` service hosted by Kaazing WebSocket Gateway. The app sends messages and receives messages with the `Echo` service over WebSocket.

## Minimum Requirements for Running or Building Kaazing J2SE WebSocket Echo tutorial

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
build/install/java-ws-demo/bin/java-ws-demo
```
or 
```
build\install\java-ws-demo\bin\java-ws-demo.bat
```

Interact with Kaazing Java WebSocket Client API

Documentation that explains how to create a Kaazing Java WebSocket application from scratch can be found [here](http://kaazing.com/doc/5.0/websocket_client_docs/dev-java/o_dev_java.html).

## API Documentation

API Documentation for Kaazing Java WebSocket Client library is available:

* [Kaazing WebSocket](http://kaazing.com/doc/5.0/websocket_client_docs/apidoc/client/java/gateway/index.html)

