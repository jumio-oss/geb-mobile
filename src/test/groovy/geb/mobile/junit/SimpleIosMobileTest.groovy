package geb.mobile.junit

import geb.Browser
import geb.mobile.driver.GebMobileDriverFactory
import org.junit.Test

class SimpleIosMobileTest {

    @Test
    public void runIosUICatalog(){
        GebMobileDriverFactory.setIosDriver('appUT.package':'Safari' , 'language':'en' )//, 'locale': 'en_GB')
        Browser.drive(new Browser(driver: GebMobileDriverFactory.createMobileDriverInstance() )){ Browser browser->
            browser.driver.get("http://www.google.com")
            go "http://www.google.com"
            //println $("#title")
            //at UICatalogAppView
            //mytitle == "UICatalog"
            println browser.driver.pageSource
        }
    }

}
