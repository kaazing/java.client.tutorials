# Kaazing J2SE WebSocket Echo Tutorial

This tutorial shows how J2SE console application can communicate over the web with an `echo` service running within Kaazing WebSocket Gateway using Kaazing Java WebSocket Client library. The application sends text messages to the `echo` service over WebSocket.
The `echo` service, running inside the Kaazing WebSocket Gateway, reflects back the message that is received by the J2SE application. 

## Minimum Requirements for Running or Building Kaazing J2SE WebSocket Echo tutorial

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
build/install/java-ws-demo/bin/java-ws-demo
```
or 
```
build\install\java-ws-demo\bin\java-ws-demo.bat
```

## API Documentation

API Documentation for Kaazing Java WebSocket Client library is available:

* [Kaazing WebSocket](https://kaazing.com/doc/legacy/4.0/apidoc/client/java/gateway/index.html)

