# Skeletoid
An utils library for Android Applications made at Mindera.

### Building and releasing!
[![Build Status](https://travis-ci.org/Mindera/skeletoid.svg)](https://travis-ci.org/Mindera/skeletoid)
[![Release](https://jitpack.io/v/mindera/skeletoid.svg)](https://jitpack.io/#mindera/skeletoid)

### Say "AAA":
[![codebeat badge](https://codebeat.co/badges/fa990b92-e4ef-4882-9e65-52c32bda0a5b)](https://codebeat.co/projects/github-com-mindera-skeletoid-master)
[![sonarcloud badge](https://sonarcloud.io/api/project_badges/measure?project=mydroidisbetterthanyours&metric=alert_status)](https://sonarcloud.io/dashboard?id=mydroidisbetterthanyours)


### Code coverage:
[![sonarcloud Coverage Badge](https://sonarcloud.io/api/project_badges/measure?project=mydroidisbetterthanyours&metric=coverage)](https://sonarcloud.io/dashboard?id=mydroidisbetterthanyours)


### Keep on counting!
[![sonarcloud Badge](https://sonarcloud.io/api/project_badges/measure?project=mydroidisbetterthanyours&metric=ncloc)](https://sonarcloud.io/dashboard?id=mydroidisbetterthanyours)

---

### Why should you use it?
Instead of copy pasting the same utils packages to each new project (and not having improvements propagated), just have this lib to help you! 


This library is divided into modules to enable you to just use those that you need:

## Base Module
Standard Android features, without the need to import any external library.

### Features

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

### Thread pooling management
With this you can use the the ThreadPoolExecutor and ScheduledThreadPool executor the same as you normally use, so why use it?
- Have threads of each ThreadPool with the naming you want
- Log the exceptions thrown in threadpool threads
- Have task priority in threadpool (just have your own Runnables extend PriorityTask)


### Analytics abstraction
Have multiple implementation of analytics, with a common interface. **Send an event once, and get it push to every appender.** 

This way you can add/remove/enable/disable appenders seamlessly.
You can have multiple Analytic appenders, you can event implement your own. We provide the the Firebase and Google Analytics appenders as a "plugin" each on it's own module. 

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


### Performance Rx Operators
Have your own implementation of performance tracking using `PerformanceObservableOperator` and `PerformanceSingleOperator`. Create your tracker by implementing `PerformanceTracker` interface.


### Usage

The plugin is available in [JitPack](https://jitpack.io/). Just add the following to your buildscript dependencies:

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

```

Add the following script to the app dependencies:

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:base:1.0.0'
    
}
```

## Kotlin extensions 

Kotlin extensions to improve your Android code readability

### Features
Adds extensions for:

Android
- Activity
- Context
- Parcel

Android UI
- EditText
- TextView
- View

Kotlin
- Any
- Let
- Calendar


TODO: Add code examples 

### Usage
And add the following script to the app dependencies:

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:kt-extensions:1.0.0'
}
```

## RxJava

Depends on base module.
Operators and extensions to make your RxJava 2 code awesome.

### Features
Adds extensions for:

RxJava
- Completable
- Observable
- Single
- Breadcrumbs


TODO: Add code examples 

### Usage
And add the following script to the app dependencies:

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:rxjava:1.0.0'
}
```


## RxBindings

Depends on RxJava module.

### Features
Adds support for some Views that [Jake's Wharlton original RxBindings](https://github.com/JakeWharton/RxBinding) has issues with. Look at this as an extension of that library.

Views supported:
- RxSearchView (method focusChanges)

TODO: Add code examples 

### Usage
And add the following script to the app dependencies:

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:rxbindings:1.0.0'
}
```

## Analytics Firebase - Support for Firebase 

### Features
An appender for the Analytics abstraction of Base module that supports Firebase of the box

TODO: Add code examples 

### Usage
And add the following script to the app dependencies:

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:analytics-firebase:1.0.0'
}
```


## Analytics GA - Support for Google Analytics

An appender for the Analytics abstraction of Base module that supports GA of the box

TODO: Add code examples

### Usage
And add the following script to the app dependencies:

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:analytics-ga:1.0.0'
}
```


## Performance Firebase - Support for Firebase

### Features
Contains `FirebasePerformanceObservableOperator` and `FirebasePerformanceSingleOperator` that allows to track your reactive calls performance using Firebase Performance.

### Usage

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:performance-firebase:1.0.0'
}
```

To track your call performance use `lift` operator.


## App Rating Dialog

Prompt a dialog for the user to rate your app. There's two ways to use this, using our default dialog or using your own

### Usage

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:apprating:1.0.0'
}
```

### Using your own dialog

1. Init with your preferred conditions:
	`AppRatingInitializer.init(context, callback, Pair(maximumCount, timeRange), timeBetweenPrompts)`

	`callback: AppRatingCallback` - Callback that will be called to show the dialog
	`maximumCount: Int` - Number of times the prompt will show within the `timeRange`
	`timeRange: Long` - Time range to reset the number of counts (in days)
	`timeBetweenPrompts: Long` - Minimum time to prompt the dialog again (in days)

	**e.g**, if you want that the minimal time in days to show the user the prompt again to be 30, but you don't want it to show more than 3 times in a year span(365 days), then you will have:
		**maximumCount = 3**
		**timeRange = 365**
		**timeBetweenPrompts = 30**

2. Call `AppRatingInitializer.promptDialog(context)`. This will check if the conditions are met to show the dialog and call the `showRatingDialog()` of the `AppRatingCallback`.
3. Show dialog inside the `showRatingDialog()` of the `AppRatingCallback`.
4. Handle the response and call the following so the conditions are updated:
`AppRatingInitializer.handleDialogResponse(context, AppRatingDialogResponse)`

	Use one of the following depending on the action:
	`AppRatingDialogResponse.RATE_NOW` - this will update the condition as the user just rated the app and will never prompt the dialog again
	`AppRatingDialogResponse.RATE_LATER` - this will schedule a prompt after the `timeBetweenPrompts` value in days as passed
	`AppRatingDialogResponse.NEVER_RATE` - this will never prompt the dialog again

### Using our dialog

1. Init with your preferred conditions, but this time don't pass the callback here:
	`AppRatingInitializer.init(context, Pair(maximumCount, timeRange), timeBetweenPrompts)`

2. Call `AppRatingInitializer.promptDialog(context, fragmentManager)`, passing it a fragment manager. Make sure that the activity of the passed fragmentManager implements the `AppRatingResponseCallback`. This will check if the conditions are met to show the dialog and call the `showRatingDialog()` of the `AppRatingCallback`.

3. Handle the response of the dialog in the `AppRatingDialogResponseCallback` methods:
	`onRateNowClick()`: This will handle the "Ok, sure" button
	`onRateLaterClick()`: This will handle the "Not now" button
	`onNeverRateClick()`: This will handle the "Don't ask me again" button

## Note

All the module will be converted to Kotlin in due time.

The parts of this lib in Java will soon be migrated to Kotlin
