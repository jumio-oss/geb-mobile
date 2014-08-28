package geb.mobile.android.instrumention.specification.appium

import geb.mobile.GebMobileBaseSpec

/**
 * Created by gmueksch on 27.06.14.
 * Base class which sets the System properties to use Appium with the selendroid interface
 */
class AppiumWithGebMobileAutomationTestBase extends GebMobileBaseSpec{

    static{
        System.setProperty("appUT.package","io.selendroid.testapp")
        System.setProperty("appium_app", new File(ClassLoader.getSystemResource("testapk/selendroid-test-app-0.9.0.apk").toURI()).absolutePath)
        //To run the test on a specific device
        //System.setProperty("appium_udid","192.168.56.10:5555")
        //System.setProperty("appium_automationName","selendroid")

        //Use Appium
        System.setProperty("framework","appium")
    }
}
