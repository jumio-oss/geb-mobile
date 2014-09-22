
# Geb Mobile Extension for Appium, Selendroid and IosDriver
---

## Under Development 
---

## Goal 
+ Write mobile selenium based tests with all the features of Geb/Spock
+ Reuse Test for Mobile and normal Web
+ Support Geb for Android over Appium and Selendroid
+ Support Geb for Ios over Appium and Iosdriver
+ Support Geb for MobileSafari over Appium

## Actual working
+  Android over Appium and Selendroid
+  native iOS apps with Appium 
+  Safari on iOS-Emulator
+  Safari on Real Device
+  Mobile Safari over Appium works with Saucelabs

## Motivation
+ Geb is cool [See here](http://www.gebish.org/)
+ Spock is awesome [See here](http://docs.spockframework.org/en/latest/)
+ Geb/Spock both together is super cool and awesome [See here](http://www.gebish.org/manual/current/testing.html#spock_junit__testng)
+ Mobile Test Automation sucks, cause of so many frameworks
+ Write BDT Style [See here](http://de.slideshare.net/vodqanite/behavior-driven-testing-bdt)


## Used Stuff
+ Android Debug Bridge 
+ Xcode stuff 
+ Geb/Spock v0.9.3
+ Groovy v2.3.4
+ Gradle v1.9
+ Appium >= v1.2.1
+ Appium Java Client v1.6.2
+ Selendroid v0.11 
+ IosDriver v0.6+
+ 


## Preconditions
1. install adb for android
2. install xcode for ios
3. for appium you need the nodejs appium installed
5. Appium on Mac is tricky, see notes at the end


## Get Started
1. Plugin a mobile device or start a virtual device 
2. check with 'adb devices', that your device or emulator is registered
3. start with './gradlew -i runSeleniumTests' if you have an android devices with API-Level < 17 
4. for API-Level > 17 you can also try './gradlew -i runAppiumTests' 
5. The selendroid or appium server starts for you , if everything works as designed


## TODO 
+ Test the iosdriver on mac with the UIMountain or UICatalog app
+ Merge the NavigatorFactories, a lot common stuff
+ Improve performance, when checking the attributes or property of a WebElement 
+ Refactoring
+ more Refactoring
+ Write more tests 
+ Find a public jenkins / repository


# IOS - Appium - Real Device TODO's:
- on version 1.2.2 the deviceconsole is not compiled ...
- cd /usr/local/lib/node_modules/appium/build/deviceconsole/
- make
- clone SafariLauncher from github, build it with your dev licence and put it into .../node_module/appium/build/SafariLauncher/ 
- brew install ios-webkit-debug-proxy
- run: ios_webkit_debug_proxy -c <your-udid-from-your-device>:27753



