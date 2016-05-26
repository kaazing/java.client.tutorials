# Kaazing Android WebSocket JMS Tutorial

This tutorial shows how Android application can communicate over the web with an JMS server via Kaazing WebSocket Gateway using Kaazing Java WebSocket Client library. The application publishes text messages via Kaazing Gateway to a JMS server and listens to the messages from the JMS server over WebSocket.

## Minimum Requirements for Running or Building Kaazing Android JMS WebSocket tutorial

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

Tutorial how to create Kaazing Android WebSocket JMS application from scratch, to be able to send and receive JMS messages over WebSocket can be found [here](http://kaazing.com/doc/5.0/jms_client_docs/dev-android/o_dev_android.html).

## API Documentation

API Documentation for Kaazing Java WebSocket JMS Client library is available:

* [Kaazing JMS](https://kaazing.com/doc/jms/4.0/apidoc/client/java/jms/index.html)
