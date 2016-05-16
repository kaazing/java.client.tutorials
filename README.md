# Java Client Demos

This repository contains simple applications that use Kaazing WebSocket Libraries for Java.

Applications are built for:
- [J2SE](j2se)
  - [WebSocket](j2se/java-ws-demo)
  - [JMS](j2se/java-jms-demo)
  - [AMQP](j2se/java-amqp-demo)
- [Android](android)
  - [WebSocket](android/ws)
  - [JMS](android/jms)


For Android demo, the easiest way to build the demo with gradle is to use Android Studio.
After installing it, make sure to:
- update local.properties file with path to android sdk
- update project.properties file with android sdk target version 
- update in AndroidManifest.xml the sdk version used
- update in app/build.gradle the compileSdkVersion, buildToolsVersion, minSdkVersion and targetSdkVersion

In case of errors with "Could not find or load main class org.gradle.wrapper.GradleWrapperMain", add a wrapper in the jms-demo/build.gradle file:
"
task wrapper(type: Wrapper) {
gradleVersion = "2.3"
}
"
Run the java.client.demos project, select a virtual device from the list of devices, if created). If not, create a new one with the sdk specified in the above files. 

