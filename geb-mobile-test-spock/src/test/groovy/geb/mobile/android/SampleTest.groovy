package geb.mobile.android

import geb.mobile.GebMobileBaseSpec
import spock.lang.Stepwise

class MainActivity extends AndroidBaseActivity {

}

@Stepwise
class HybridLoginSpec extends GebMobileBaseSpec{

    static{
        System.setProperty('appium_appPackage', 'com.ionicframework.micro879924')
        System.setProperty('appium_appActivity', 'MainActivity')
        System.setProperty('appium_appWaitActivity',  'MainActivity')
        //GebMobileDriverFactory.setAppium([appPackage: 'com.ionicframework.micro879924' , appActivity: '.ContactManager'])
        /*
        System.setProperty('selenium.url','https://app.testobject.com:443/api/appium/wd/hub')
        GebMobileDriverFactory.setAppiumAndroid(
                appPackage:'com.example.jumiologin',
                appActivity: 'LoginActivity',
                testobject_api_key : 'EDB76E8F0C9245BBBBBD34F9C4B79EE4',
                testobject_app_id :'1',
                testobject_device : 'LG_Nexus_5_real',
                testobject_suite_name : 'Default Appium Suite',
                testobject_test_name:   'Default Appium Test'
        )
        */

        System.setProperty "framework", "appium"
        System.setProperty "appium_app",'sauce-storage:sunRise.apk'
        System.setProperty "selenium.url", 'http://tstehno:69bdce3a-560d-40eb-a90f-97382fdd02a3@ondemand.saucelabs.com/wd/hub'
        System.setProperty "appium_deviceName","Google Nexus 7 HD Emulator"
        System.setProperty "appium_platformVersion","4.4"
        System.setProperty "appium_appiumVersion","1.5.3"
        System.setProperty "appium_name","sample test by mueke with geb"
        System.setProperty "appium_browserName",""
        System.setProperty "appium_platformName"   , 'Android'


    }

    def setup(){
        println browser.driver
    }

    def "test login "(){
       when:
       at MainActivity
       then:
       withWebView {
           $('#username').value('normal')
           $('#password').value('test123')
           $('#loginwithuser').click()
           println "logged in"
           return true
       }
       when:
       withWebView {
           waitFor {
               $('#go')
           }.click()
           println "Go"
       }
       then:
       true

    }


}