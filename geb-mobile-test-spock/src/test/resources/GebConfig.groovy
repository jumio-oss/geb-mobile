/*
 Minimum GebConfig
 */

import geb.mobile.GebMobileNavigatorFactory
import geb.mobile.driver.GebMobileDriverFactory

driver = {
   GebMobileDriverFactory.createMobileDriverInstance()
}

navigatorFactory = { browser ->
    new GebMobileNavigatorFactory(browser)
}

baseUrl = ""