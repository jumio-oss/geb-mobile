package geb.mobile.allinone

import geb.mobile.GebMobileBaseSpec
import geb.mobile.android.AndroidBaseActivity
import geb.mobile.driver.GebMobileDriverFactory
import io.appium.java_client.android.AndroidDriver
import spock.lang.Stepwise

class LoginActivity extends AndroidBaseActivity {
    static content = {
        login{ $("#login") }
        password{ $("#password") }
        okButton{ $("#button") }
    }
}
class SuccessActivity extends AndroidBaseActivity {
    static content = {
        creditCard{ $("#enter") }
        exit{ $("#exit") }
        ok{ $("#ok") }
    }
}
class ErrorActivity extends AndroidBaseActivity {
    static content = {
        retry{ $("#retry") }
    }
}

@Stepwise
class JumioLoginSpec extends GebMobileBaseSpec{

    static{
        System.setProperty('selenium.url','https://app.testobject.com:443/api/appium/wd/hub')
        GebMobileDriverFactory.setAppiumAndroid(
                appPackage:'com.example.jumiologin',
                appActivity: 'LoginActivity',
                testobject_api_key : System.getProperty("testobjectApiKey", System.getenv("TESTOBJECT_APIKEY")),
                testobject_app_id :'1',
                testobject_device : 'LG_Nexus_5_real',
                testobject_suite_name : 'Default Appium Suite',
                testobject_test_name:   'Default Appium Test'
        )
    }

    def setup(){
        if( driver instanceof AndroidDriver ){
            driver.startActivity('com.example.jumiologin','LoginActivity')
        }
    }

    def "test good login "(){
        when:
        waitFor{ at LoginActivity }
        login.value("rudolf")
        password.value('12345')
        okButton.click()
        then:
        waitFor(10,1){
            at SuccessActivity
        }
    }

    def "test bad login "(){
        when:
        waitFor{ at LoginActivity }
        login.value("Rudolf")
        password.value('12345')
        okButton.click()
        then:
        waitFor(10,1){
            at ErrorActivity
        }
    }
}