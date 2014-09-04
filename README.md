
# Geb Mobile Extension for Appium, Selendroid and IosDriver
---

## Motivation
+ Geb is cool [See here](http://www.gebish.org/)
+ Spock is awesome [See here](http://docs.spockframework.org/en/latest/)
+ Geb/Spock both together is super cool and awesome [See here](http://www.gebish.org/manual/current/testing.html#spock_junit__testng)
+ Mobile Test Automation sucks, cause of so many frameworks
+ Write BDT Style [See here](http://de.slideshare.net/vodqanite/behavior-driven-testing-bdt) 
+ 
## Used Stuff
+ Android Debug Bridge 
+ Xcode stuff 
+ Geb
+ Spock
+ Gradle
+ Appium
+ Appium Java Client
+ Selendroid
+ IosDriver


## Preconditions
1. install adb for android
2. install xcode for ios

## Get Started
1. Plugin a mobile device
2. check with 'adb devices', that your device or emulator is registered
3. problems with appium and startup 
4.  

## Actual supported stuff

## TODO 
+ Different Dependency Managment for Appium and Selendroid, cause latest Appium Client needs a selenium version that breaks the selendroid server
+ Test the iosdriver on mac with the UIMountain or UICatalog app
+ Improve performance, when checking the attributes or property of a WebElement 
 

## Hints 
 + Don't wait for the Android Emulator , use a real device 
 + for easy dev: put break-point on desired method,
 + open evalute window: (CTRL-U) , run
 + AppiumPageSourceConverter.writeActivityContent(driver)


## Info zu AppiumDriver
`getAppiumDriver().findElementsByXPath(".//*").collect{ return "$it.id : $it.tagName: $it.text : ${it.getAttribute("name")} : ${it.getAttribute("className")}"}`


## IOSDriver
`driver.execute("getPageSource").tree`


