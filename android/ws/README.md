# Kaazing Android WebSocket Echo Tutorial

This tutorial shows how Android application can communicate over the web with an `echo` service running within Kaazing WebSocket Gateway using Kaazing Java WebSocket Client library. The application sends text messages to the `echo` service over WebSocket.
The `echo` service, running inside the Kaazing WebSocket Gateway, reflects back the message that is received by the Android application. 

## Minimum Requirements for Running or Building Kaazing Android Echo WebSocket tutorial

* Java 1.8
* Gradle 2.10 or higher
* Android Studio 2.10 or higher
* Android SDK 4.4 or higher

## Steps for building and running the project

- Build the application using Android Studio
   - Open the project in Android Studio by navigating to the project directory
   - Build the project by selecting Build/Rebuild Project
- Run the application in Android Simulator 
   - Create Android Virtual Device (AVD) for API level 19 (KitKat) or higher. For more information refer to [Create and Manage Virtual Devices](https://developer.android.com/studio/run/managing-avds.html)
   - Run the application from Android Studio by selecting Run/Run Project. Refer to [Running Your App](https://developer.android.com/training/basics/firstapp/running-app.html) for more information.
	
## Interact with Kaazing Java WebSocket Client API

Checklist how to create Kaazing Android WebSocket Echo application from scratch, to be able to send and receive Echo messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/websocket_client_docs/dev-android/o_dev_android.html).

## API Documentation

API Documentation for Kaazing Java WebSocket Client library is available:

* [Kaazing WebSocket](https://kaazing.com/doc/legacy/4.0/apidoc/client/java/gateway/index.html)

