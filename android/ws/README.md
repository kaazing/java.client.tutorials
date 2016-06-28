# Kaazing Android WebSocket Echo Tutorial

This Android app communicates with a remote `Echo` service hosted by Kaazing WebSocket Gateway. The app sends and receives text messages with the `Echo` service over WebSocket.

## Minimum Requirements for Running or Building

* Java 1.8
* Gradle 2.10 or higher
* Android Studio 2.10 or higher
* Android SDK 4.4 or higher

## Steps for Building and Running the Project

- Build the application using Android Studio
   - Open the project in Android Studio by navigating to the project directory
   - Build the project by selecting Build/Rebuild Project
- Run the application in Android Simulator 
   - Create an Android Virtual Device (AVD) for API level 19 (KitKat) or higher. For more information refer to [Create and Manage Virtual Devices](https://developer.android.com/studio/run/managing-avds.html)
   - Run the application from Android Studio by selecting Run/Run Project. Refer to [Running Your App](https://developer.android.com/training/basics/firstapp/running-app.html) for more information.

After installing Android Studio, update the following files with the Android SDK path or version (if necessary):
- local.properties
- project.properties
- AndroidManifest.xml
- app/build.gradle

In you experience the error `Could not find or load main class org.gradle.wrapper.GradleWrapperMain`, add a wrapper in the build.gradle file:

```
task wrapper(type: Wrapper) {
gradleVersion = <gradle-version-installed>
}
```
	
## Interact with Kaazing Android WebSocket Client API

Documentation that explains how to create Kaazing Android WebSocket apps from scratch can be found [here](http://kaazing.com/doc/5.0/websocket_client_docs/dev-android/o_dev_android.html).

## API Documentation

API Documentation for Kaazing Java WebSocket Client library is available:

* [Kaazing Android WebSocket API](http://kaazing.com/doc/5.0/websocket_client_docs/apidoc/client/android/gateway/index.html)

