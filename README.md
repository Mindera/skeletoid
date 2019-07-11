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
Also this provides out of the box a broad range of  utils.

This library is **divided into modules** to enable you to just use just those that you need (click on them to get more info on how to use them):


[Base module](https://github.com/Mindera/skeletoid/blob/master/base/README.md)
- This provides vanilla utilities for Android (logging, analytics, threading..) without bring any library attached to it

[Kotlin extensions module](https://github.com/Mindera/skeletoid/blob/master/kt-extensions/README.md)
- This provides some useful Kotlin extensions that we have been using. It is a ever growing module.

[RxJava module](https://github.com/Mindera/skeletoid/blob/master/rxjava/README.md)
- Do you use RxJava 2 & Kotlin? Well, this might be interesting for you! From operators to extensions we are adding it all!

[App Rating module](https://github.com/Mindera/skeletoid/blob/master/apprating/README.md)
- Do you want to ask you users to give rating to your app? Here you go, quick and easy!



### Plugins for previous modules

You are using [Base module](https://github.com/Mindera/skeletoid/blob/master/base/README.md) and want it to be plug and play:

[Analytics Firebase module - Support for Firebase](https://github.com/Mindera/skeletoid/blob/master/analytics-firebase/README.md)
- With Firebase

[Analytics GA module - Support for Google Analytics](https://github.com/Mindera/skeletoid/blob/master/analytics-ga/README.md)
- With Google Analytics

### Wrappers for external libraries:

[Performance Firebase module - Support for Firebase](https://github.com/Mindera/skeletoid/blob/master/performance-firebase/README.md)
- Some utils to help integrate with Firebase performance tooling

[RxBindings module](https://github.com/Mindera/skeletoid/blob/master/rxbindings/README.md)
- This is a simple wrapper around [RxBindings](https://github.com/JakeWharton/RxBinding) that aims to solve some specific issues

## Notes

- We are moving to a full Kotlin world, but you might still find some Java along the road.
- We move fast and we break things.. but we are friendly. Please drop us an issue/feature request and we will address it asap.
