# Skeletoid
An utils library for Android Applications made at Mindera

[![Build Status](https://travis-ci.org/Mindera/skeletoid.svg)](https://travis-ci.org/Mindera/skeletoid) [![Release](https://jitpack.io/v/mindera/skeletoid.svg)](https://jitpack.io/#mindera/skeletoid) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/86fd0ce3d3314d4f93999f98dbd96f26)](https://www.codacy.com/app/Skeletoid/skeletoid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Mindera/skeletoid&amp;utm_campaign=Badge_Grade)

## Features
* Analytics abstraction
* Generic stuff
* Logging abstraction
* Threadppoling management 


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

