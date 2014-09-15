
# Geb Mobile Extension for Appium, Selendroid and IosDriver
---

## Motivation
+ Geb is cool [See here](http://www.gebish.org/)
+ Spock is awesome [See here](http://docs.spockframework.org/en/latest/)
+ Geb/Spock both together is super cool and awesome [See here](http://www.gebish.org/manual/current/testing.html#spock_junit__testng)
+ Mobile Test Automation sucks, cause of so many frameworks
+ Write BDT Style [See here](http://de.slideshare.net/vodqanite/behavior-driven-testing-bdt)


## Used Stuff
+ Android Debug Bridge 
+ Xcode stuff 
+ Geb v0.9.3
+ Spock
+ Gradle
+ Appium >= v1.2.1
+ Appium Java Client v1.6.2
+ Selendroid 
+ IosDriver


## Preconditions
1. install adb for android
2. install xcode for ios
3. for appium you need the nodejs appium installed
4. if you start your servers yourself, the append a '-DskipServer=true' to the commandline 
5. Appium on Mac is tricky, see notes at the end

## Get Started
1. Plugin a mobile device or start a virtual device 
2. check with 'adb devices', that your device or emulator is registered
3. start with './gradlew -i runSeleniumTests' if you have an android devices with API-Level < 17 
4. for API-Level > 17 you can also try './gradlew -i runAppiumTests' 
5. The selendroid or appium server 

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

# IOS - Appium - Real Device TODO's:
- on version 1.2.2 the deviceconsole is not compiled ...
- cd /usr/local/lib/node_modules/appium/build/deviceconsole/
- make
- clone SafariLauncher from github, build it with your dev licence and put it into .../node_module/appium/build/SafariLauncher/ 
- brew install ios-webkit-debug-proxy
- run: ios_webkit_debug_proxy -c <your-udid-from-your-device>:27753

## IOSDriver
`driver.execute("getPageSource").tree`


