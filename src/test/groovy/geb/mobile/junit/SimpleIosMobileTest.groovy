package geb.mobile.junit

import geb.Browser
import geb.mobile.driver.GebMobileDriverFactory
import org.junit.Test

class SimpleIosMobileTest {

    @Test
    public void runIosUICatalog(){

        GebMobileDriverFactory.setAppiumIos('appUT.package': 'UICatalog')
        Browser.drive{ browser ->
            println $("//*")
            //println $("#title")
            //at UICatalogAppView
            //mytitle == "UICatalog"
            new File("UICatalog.xml").withWriter{ wr->
                wr.write browser.driver.pageSource
            }

        }
    }



}
