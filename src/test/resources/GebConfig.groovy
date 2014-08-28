import geb.mobile.GebMobileNavigatorFactory
import geb.mobile.driver.GebMobileDriverFactory


driver = {
//    System.setProperty("appUT.package","io.selendroid.testapp")
//    System.setProperty("appium_app", new File(ClassLoader.getSystemResource("testapk/selendroid-test-app-0.9.0.apk").toURI()).absolutePath)
//    //To run the test on a specific device
//    //System.setProperty("appium_udid","192.168.56.10:5555")
//    //Use Appium
//    System.setProperty("framework","appium")

   GebMobileDriverFactory.createMobileDriverInstance()
}

navigatorFactory = { browser ->
    new GebMobileNavigatorFactory(browser)
}

baseUrl = ""