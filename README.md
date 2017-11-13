# Skeletoid
An utils library for Android Applications made at Mindera.

[![Build Status](https://travis-ci.org/Mindera/skeletoid.svg)](https://travis-ci.org/Mindera/skeletoid)
[![Release](https://jitpack.io/v/mindera/skeletoid.svg)](https://jitpack.io/#mindera/skeletoid)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/86fd0ce3d3314d4f93999f98dbd96f26)](https://www.codacy.com/app/Skeletoid/skeletoid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Mindera/skeletoid&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/86fd0ce3d3314d4f93999f98dbd96f26)](https://www.codacy.com/app/Skeletoid/skeletoid?utm_source=github.com&utm_medium=referral&utm_content=Mindera/skeletoid&utm_campaign=Badge_Coverage)

### Why should you use it?
Instead of copy pasting the same utils packages to each new project (and not having improvements propagated), just have this lib to help you! 


## Features

### Logging abstraction
Have multiple implementation of logs, with a common interface. **Log once, propagate it to multiple appenders.** 

This way you can add/remove/enable/disable appenders seamlessly.
You can have multiple log appenders, you can event implement your own. We provide the out-of-the-box: LogCat and LogToFile (Rolling Appending)

1. Init (if you don't call this, every call to LOG will be ignored):

  ```java  
    LOG.init(context);
  ```

2. Add appenders:
    You can have multiple log appenders, you can event implement your own.
      Here are the LogCat appender and File appender.

  ```java
    List<ILogAppender> appenders = new ArrayList();
    appenders.add(new LogcatAppender("TAG")); 
    appenders.add(new LogFileAppender("TAG", "MyFileName"));
    LOG.addAppenders(getContext(), appenders);
  ```

3. Then log things and have each appender write the log:

 ```java
   LOG.d("TAG","Text");
   LOG.e("TAG", new Exception(), "Omg");
   LOG.wtf("TAG", new Exception(), "This", "accepts", "endless", "strings");
 ```


### Connectivity
Determine real device connectivity: if the device is connected to a network and if it really has internet access.
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

Remember to avoid leaks. If you need you can:

```java
   Connectivity.removeConnectivityCallback();
```
   

_Note: To be able to determine the real connectivity, a request to google.com will be made each time the device connects to a network. You need of course to have the INTERNET permission on the Manifest (if you didn't, you wouldn't need this anyway)._

You can change the host to be called via:

```java
   Connectivity.updateConnectivityValidationAddress(uri);
```

### Thread pooling management
With this you can use the the ThreadPoolExecutor and ScheduledThreadPool executor the same as you normally use, so why use it?
- Have threads of each ThreadPool with the naming you want
- Log the exceptions thrown in threadpool threads
- Have task priority in threadpool (just have your own Runnables extend PriorityTask)


### Analytics abstraction
Have multiple implementation of analytics, with a common interface. **Send an event once, and get it push to every appender.** 

This way you can add/remove/enable/disable appenders seamlessly.
You can have multiple Analytic appenders, you can event implement your own. We provide the the Google Analytics appender as a plugin. 

1. Init:

    ```java  
    Analytics.init(context);
    ```

2. Add appenders:
    

    ```java
    List<IAnalyticsAppender> appenders ...
    Analytics.addAppenders(appenders);
    ```
    
3. Then track an event or page hit:

   ```java
   Analytics.trackEvent(eventName, analyticsPayload);
   ```

   ```java
   Analytics.trackPageHit(activity, screenName, screenClassOverride, analyticsPayload);
   ```

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
* [Kotlin Extensions](https://github.com/Mindera/skeletoid-kt-extensions)
* [Firebase Analytics appender](https://github.com/Mindera/skeletoid-firebase-analytics)
* [Google Analytics appender](https://github.com/Mindera/skeletoid-googleanalytics)

