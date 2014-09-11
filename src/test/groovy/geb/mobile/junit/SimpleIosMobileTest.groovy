package geb.mobile.junit

import geb.Browser
import geb.mobile.driver.GebMobileDriverFactory
import org.junit.Test

class SimpleIosMobileTest {

    public void runIosUICatalog(){
        GebMobileDriverFactory.setIosDriver('browserName':'safari' )
        Browser.drive(){ Browser browser->
            browser.driver.get("http://www.google.com")
            go "http://www.google.com"
            //println $("#title")
            //at UICatalogAppView
            //mytitle == "UICatalog"
            println browser.driver.pageSource
        }
    }

    @Test
    public void runAppiumGoogleTest(){
        GebMobileDriverFactory.setAppiumIos('browserName': 'safari')
        Browser.drive(){ Browser browser ->
            go "https://www.google.com"

            waitFor{ $("#lst-ib") }.value("hallo wiki")
            $("#tsbb").click()
            $("partialLinkText='Hallo Wikipedia'")
            //new File("pagesource.html").withWriter(){ wr-> wr.write driver.pageSource }

        }
    }

}
