package geb.mobile.android

import geb.Browser
import groovy.transform.Trait
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.selendroid.SelendroidKeys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions

/**
 * Created by gmueksch on 21/01/15.
 */
class AndroidHelper {

    private Browser browser

    private WebDriver getDriver(){
        browser.driver
    }

    void back() {
        if (driver instanceof AndroidDriver) driver.sendKeyEvent(AndroidKeyCode.BACK)
        else new Actions(driver).sendKeys(SelendroidKeys.BACK).perform()
    }
    void menu() {
        if( driver instanceof AndroidDriver ) driver.sendKeyEvent(AndroidKeyCode.MENU)
        else new Actions(driver).sendKeys(SelendroidKeys.MENU).perform()

    }
    void home() {
        if( driver instanceof AndroidDriver ) driver.sendKeyEvent(AndroidKeyCode.HOME)
        else new Actions(driver).sendKeys(SelendroidKeys.ANDROID_HOME).perform()

    }

    public String getMessage(){
        browser.find("#android:id/message").text()
    }

    public def systemRetry(){
        browser.find("#android:id/button1").click()
    }

    public def systemCancel(){
        browser.find("#android:id/button2").click()
    }

    public void handleSystemMessage(){
        def msg = getMessage()
        if( msg == "We have encountered a network communication problem" ) {
            systemRetry()
        }else if( msg == "No internet connection available"){
            systemRetry()
        }else if( msg =~ /previous crashes/ ){
            systemRetry()
        }
    }

    public boolean swipeToLeft() {

        if (driver instanceof AppiumDriver) {
            def dim = driver.manage().window().size
            int startX = dim.width - 5
            int startY = dim.height / 2
            int endX = 5
            int endY = dim.height / 2
            try {
                driver.swipe(startX, startY, endX, endY, 200)
                return true
            } catch (e) {
                log.warn("Error on left swipe($startX, $startY, $endX, $endY, 200) : $e.message")
            }
        }
        false

    }

    public boolean swipeToRight() {
        if (driver instanceof AppiumDriver) {

            def dim = driver.manage().window().size
            int startX = 5
            int startY = dim.height / 2
            int endX = dim.width - 5
            int endY = dim.height / 2
            try {
                driver.swipe(startX, startY, endX, endY, 200)
                return true
            } catch (e) {
                log.warn("Error on right swipe($startX, $startY, $endX, $endY, 200) : $e.message")
            }
        }
        false
    }
}
