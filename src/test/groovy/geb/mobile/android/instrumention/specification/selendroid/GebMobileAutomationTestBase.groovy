package geb.mobile.android.instrumention.specification.selendroid

import geb.mobile.GebMobileBaseSpec

/**
 * Created by gmueksch on 27.06.14.
 * Base class which sets the System properties
 */
class GebMobileAutomationTestBase extends GebMobileBaseSpec{

    static{
        System.setProperty(appUT_absolutePath, new File(ClassLoader.getSystemResource("testapk/selendroid-test-app-0.9.0.apk").toURI()).absolutePath)
        System.setProperty("appUT.package","io.selendroid.testapp")
        System.setProperty("appUT.version","0.10.0")
        System.setProperty("appUT_cap_Emulator","false")
        System.setProperty("framework","selendroid")
    }
}
