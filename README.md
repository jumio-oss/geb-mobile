
## Geb Mobile Extension for appium ( UIAutomator ) and selendroid ( InstrumentationFramework )  ##

* for easy dev: put break-point on desired method,
* open evalute window: (CTRL-U) , run
* AppiumPageSourceConverter.writeActivityContent(driver)


# Info zu AppiumDriver

getAppiumDriver().findElementsByXPath(".//*").collect{ return "$it.id : $it.tagName: $it.text : ${it.getAttribute("name")} : ${it.getAttribute("className")}"}


## IOSDriver
driver.execute("getPageSource").tree

