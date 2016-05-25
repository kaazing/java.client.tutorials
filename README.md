# Kaazing Tutorials for Java

This repository contains tutorials that use Kaazing WebSocket Libraries for Java.

Kaazing WebSocket Libraries for Java are available via Maven, Gradle or direct download. Visit [Kaazing Downloads](http://kaazing.com/download/) for more information how to obtain client libraries.


Tutorials are built for:
- [J2SE](j2se)
  - [WebSocket](j2se/java-ws-demo)
  - [JMS](j2se/java-jms-demo)
  - [AMQP](j2se/java-amqp-demo)
- [Android](android)
  - [WebSocket](android/ws)
  - [JMS](android/jms)

For Android demo, the easiest way to build the demo using gradle is with Android Studio. After installing it, make sure to update the following files with the android sdk path or sdk version:
- local.properties
- project.properties
- AndroidManifest.xml
- app/build.gradle

In case of errors with "Could not find or load main class org.gradle.wrapper.GradleWrapperMain", add a wrapper in the jms-demo/build.gradle file:
task wrapper(type: Wrapper) {
gradleVersion = <gradle-version-installed>
}

Run the java.client.demos project, select a virtual device from the list of devices, where the demo will be installed.
