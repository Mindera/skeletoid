# Skeletoid
An utils library for Android Applications made at Mindera.

Why should you use it?
Instead of copy pasting the same utils packages to each new project (and not having improvements propagated), just have this lib to help you! 

[![Build Status](https://travis-ci.org/Mindera/skeletoid.svg)](https://travis-ci.org/Mindera/skeletoid)
[![Release](https://jitpack.io/v/mindera/skeletoid.svg)](https://jitpack.io/#mindera/skeletoid)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/86fd0ce3d3314d4f93999f98dbd96f26)](https://www.codacy.com/app/Skeletoid/skeletoid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Mindera/skeletoid&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/86fd0ce3d3314d4f93999f98dbd96f26)](https://www.codacy.com/app/Skeletoid/skeletoid?utm_source=github.com&utm_medium=referral&utm_content=Mindera/skeletoid&utm_campaign=Badge_Coverage)

## Features
### Analytics abstraction

1. Init:

    ```java  
    Analytics.init(context);
    ```

2. Add appenders:
    You can have multiple Analytic appenders, you can event implement your own.
      Check the Google Analytics plugin for an out-of-the-box Analytics appender.

    ```java
    List<IAnalyticsAppender> appenders ...
    Analytics.addAppenders(appenders);
    ```
    
3. Then track an event or page hit:

   ```java
   Analytics.trackEvent(screenName, analyticsPayload);
   ```

   ```java
   Analytics.trackPageHit(screenName, analyticsPayload);
   ```


### Logging abstraction

1. Init:

    ```java  
    LOG.init(context);
    ```

2. Add appenders:
    You can have multiple log appenders, you can event implement your own.
      Out of the box we provide the LogCat appender and File appender.

    ```java
    List<ILogAppender> appenders = new ArrayList();
    appenders.add(new LogcatAppender("TAG")); 
    appenders.add(new LogFileAppender("TAG", "MyFile"));
    Analytics.addAppenders(appenders);
    ```

3. Then log things:

   ```java
   LOG.d("TAG","Text");
   LOG.e("TAG", new Exception(), "Omg");
   LOG.wtf("TAG", new Exception(), "This", "accepts", "endless", "strings");
   ```


### Connectivity

Determine real connectivity: both if the device is connected to a network and if it has internet access

To be able to do that, you'll need to add to the app's manifest:

   ```android
    <receiver android:name="com.mindera.skeletoid.network.ConnectivityReceiver">
     <intent-filter>
     <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
     <action android:name="android.net.wifi.STATE_CHANGE"/>
     <action android:name="android.net.wifi.supplicant.CONNECTION_CHANGE"/>
     </intent-filter>
     </receiver>
   ```
 
And then you can just check via:

   ```java
   boolean isConnectedAndWithInternet = Connectivity.isConnectedAndWithInternetAvailable(context);
   ```
   
If you want to be notified via callback:
   
   ```java
      Connectivity.setConnectivityCallback(callback);
   ```
   

_Note: To be able to determine the real connectivity, a request to google.com will be made each time the device connects to a network. You need of course to have the INTERNET permission on the Manifest (if you didn't, you wouldn't need this anyway)._

You can change the host to be called via:

```java
   Connectivity.updateConnectivityValidationAddress(uri);
   ```

### Thread pooling management

Just use the the ThreadPoolExecutor and ScheduledThreadPool executor the same as you normally use.

Advantages:
- Have threads with proper naming
- Log the exceptions thrown in threadpool threads.
- Have task priority in threadpool (just extend PriorityTask)


## Usage


The plugin is available in [JitPack](https://jitpack.io/). Just add the following to your buildscript dependencies:

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

```
And add the following script to the app dependencies

```groovy
dependencies {
    compile 'com.github.mindera:skeletoid:x.y.z'
}
```


## Plugins available
* [Google Analytics appender](https://github.com/Mindera/skeletoid-googleanalytics)

