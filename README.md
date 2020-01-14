
# Geb Mobile Extension for Appium, Selendroid and IosDriver
---

## Under Development
+ This project is still under development and is used in our automation. So all functions are implemented
---

## Goal
+ Write mobile selenium based tests with features of Geb/Spock
+ Reuse Test for Mobile and normal Web
+ Run same test against Android and Ios
+ Support Geb for Android over Appium
+ Support Geb for Ios over Appium and Iosdriver
+ Support Geb for MobileSafari over Appium

## Actual working
+  Android over Appium
+  native iOS apps with Appium 
+  Safari on iOS-Emulator ( not sure about that )
+  Safari on Real Device
+  Test should work with Cloud Provider like ( saucelabs, testobjects, etc)

## Motivation
+ Geb is cool [See here](http://www.gebish.org/)
+ Spock is awesome [See here](http://docs.spockframework.org/en/latest/)
+ Geb/Spock both together is super cool and awesome [See here](http://www.gebish.org/manual/current/testing.html#spock_junit__testng)
+ Mobile Test Automation sucks, cause of so many frameworks
+ Write BDT Style [See here](http://de.slideshare.net/vodqanite/behavior-driven-testing-bdt)


## Used Stuff
+ Android Debug Bridge 
+ Xcode stuff 
+ Geb/Spock v2.0
+ Groovy v2.3.11
+ Gradle v4.3.1
+ Appium >= v1.7.1
+ Appium Java Client v5.0.4
+ Selendroid v0.11 
+ IosDriver v0.6+



## Preconditions
1. install adb for android
2. install xcode for ios
3. for appium you need the nodejs appium installed
5. Appium on Mac is tricky, see notes at the end


## Get Started with Android ( it is easier and faster than iPhone )
1. Plugin a mobile device or start a virtual device or use genyMotion 
2. check with 'adb devices', that your device or emulator is registered
3. start appium 
4. run './gradlew -i runAppiumTests' 

# IOS - Appium - Real Device TODO's:
1. appium brings the facebook-webdriver which works nice ( nicer than before )
2. Xcode installation hazzard
3. install ideviceinstaller ( brew install ideviceinstaller )
4. some params need to be set for WebDriver:

`    
     "appium_bundleId": "<bundle id>",
          "appium_platform": "MAC",
          "appium_platformName": "iOS",
          "appium_automationName": "XCUITest",
          "appium_xcodeConfigFile": "<path_2_your_xcodeConfigFile.conf>",
          "appium_realDeviceLogger": "/usr/local/lib/node_modules/deviceconsole/deviceconsole",
          "appium_app": "<path_2_your_app.app>",
          "appium_newCommandTimeout" : "300"
` 

## TODO 
+ Improve performance, when checking the attributes or property of a WebElement 
+ more Refactoring
   + use compileStatic as much as possible
+ Write more tests 
+ Find a public jenkins / repository





