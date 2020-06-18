# Base Module
Standard Android features, without the need to import any external library.

## Features

## Logging abstraction
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

## Thread pooling management
With this you can use the the ThreadPoolExecutor and ScheduledThreadPool executor the same as you normally use, so why use it?
- Have threads of each ThreadPool with the naming you want
- Log the exceptions thrown in threadpool threads
- Have task priority in threadpool (just have your own Runnables extend PriorityTask)


## Analytics abstraction
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

## Popup Tooltip Widget
Simple popup tooltip that can be displayed above items.

Example usage: 

```java
        val popupTooltip = PopupTooltip(
                anchorView,
                "My custom popup message",
                layout = R.layout.tooltip_layout,
                textViewId = R.id.tooltip_text,
                tooltipBackgroundId = R.id.tooltip_background
        )
                .horizontalMarginClipped(anchorView.context.resources.getDimension(R.dimen.half_margin).toInt())
                .indefinite(true)
                .arrow(true)
                .arrowBackgroundId(R.id.tooltip_arrow)
                .arrowStrokeColor(android.R.color.white)
                .arrowStrokeId(R.id.tooltip_arrow_stroke)
                .arrowDrawable(R.drawable.tooltip_arrow_down)
                .elevation(R.dimen.default_elevation)
                .touchDelegateTouchAreaIncreaseAmount(10)
                .animationStyle(R.style.popup_window_animation)
                .(...)

        anchorView.setOnClickListener {
            popupTooltip.show()
        }
```

We need to provide an anchor view for the popup to attach to, a message and a layout. Besides,
we also need to provide the id for each view: background, textview, arrow background, so we can
edit them programmatically and do the calculations properly. Only the background of the tooltip is 
mandatory.

## Usage

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
    implementation 'com.github.mindera.skeletoid:base:1.1.0'

}
```
