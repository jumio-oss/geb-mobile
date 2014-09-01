/*
 Minimum GebConfig
 */

import geb.mobile.GebMobileNavigatorFactory
import geb.mobile.driver.GebMobileDriverFactory
import io.appium.java_client.AppiumDriver


driver = {
   GebMobileDriverFactory.createMobileDriverInstance()
}

navigatorFactory = { browser ->
    new GebMobileNavigatorFactory(browser)
}

baseUrl = ""