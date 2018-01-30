package geb.mobile.android

import geb.Browser
import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidElement
import io.appium.java_client.android.AndroidKeyCode
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions

/**
 * Provides some basic Androrid
 * Created by gmueksch on 21/01/15.
 */
@Slf4j
class AndroidHelper {

    private Browser browser

    private WebDriver getDriver(){
        browser.driver
    }

    void back() {
        if (driver instanceof AndroidDriver) androidDriverBackButton(driver)
        else new Actions(driver).sendKeys(SelendroidKeys.BACK).perform()
    }
    void menu() {
        if( driver instanceof AndroidDriver ) driver.pressKeyCode(AndroidKeyCode.MENU)
        else new Actions(driver).sendKeys(SelendroidKeys.MENU).perform()
    }
    void home() {
        if( driver instanceof AndroidDriver ) androidDriverHomeButton(driver)
        else new Actions(driver).sendKeys(SelendroidKeys.ANDROID_HOME).perform()
    }

    /**
     * static method for usage in Navigator
     * @param driver
     */
    public static void androidDriverBackButton(AndroidDriver driver){
        driver.pressKeyCode(AndroidKeyCode.BACK) //  pressKeyCode(AndroidKeyCode.BACK);
    }

    public static void androidDriverHomeButton(AndroidDriver driver){
        driver.pressKeyCode(AndroidKeyCode.HOME)
    }

    public String getMessage(){
        browser.find("#android:id/message").text()

    }

    /**
     * Click the Retry on the modal system message dialogo
     * first looks for resource-id button1, than for retry with caseinsesitivenes
     * @return
     */
    public def systemRetry(){
        def button = browser.find("#android:id/button1")
        if( button.size()==0 )
            button = browser.find("#textMatches('(?i:retry)')")
        if( button.size()==0 )
            button = browser.find("#textMatches('(?i:try again)')")

        button.click()

    }

    /**
     * Click the Retry on the modal system message dialogo
     * first looks for resource-id button2, than for cancel with caseinsesitivenes
     * @return
     */
    public def systemCancel(){
        def button = browser.find("#android:id/button2")
        if( button.size()==0 )
            button = browser.find("#textMatches('(?i:cancel)')")

        button.click()
    }

    /**
     * Handles a system message modal dialog
     * Reads the message, then presses retry(default) or cancel if available
     * @param retry defaults to true
     * @return the system message
     */
    public String handleSystemMessage(def retry = true ){
        def msg = getMessage()
        if( msg )
            log.info("Got System Message popup: '$msg' ")
        else
            return null

        if( msg == "We have encountered a network communication problem" ) {
            retry ? systemRetry() : systemCancel()
        }else if( msg == "No internet connection available"){
            retry ? systemRetry() : systemCancel()
        }else if( msg =~ /previous crashes/ ){
            retry ? systemRetry() : systemCancel()
        }

        return msg
    }
    /**
     * Swipe the screen to left, scrollable object should be visible
     * uses window().size to figure the start and end points
     * @return
     */
    public boolean swipeToLeft() {

        if (driver instanceof AppiumDriver) {
            def dim = driver.manage().window().size
            int startX = dim.width - 5
            int startY = dim.height / 2
            int endX = 5
            int endY = startY
            try {
                driver.swipe(startX, startY, endX, endY, 200)
                return true
            } catch (e) {
                log.warn("Error on left swipe($startX, $startY, $endX, $endY, 200) : $e.message")
            }
        }
        false

    }

    /**
     * Swipe the screen to right, scrollable object should be visible
     * uses window().size to figure the start and end points
     * @return
     */
    public boolean swipeToRight() {
        if (driver instanceof AppiumDriver) {

            def dim = driver.manage().window().size
            int startX = 5
            int startY = dim.height / 2
            int endX = dim.width - 5
            int endY = startY
            try {
                driver.swipe(startX, startY, endX, endY, 200)
                return true
            } catch (e) {
                log.warn("Error on right swipe($startX, $startY, $endX, $endY, 200) : $e.message")
            }
        }
        false
    }

    public boolean swipeUp() {
        if (driver instanceof AppiumDriver) {

            def dim = driver.manage().window().size
            int startX = dim.width / 2
            int startY = dim.height - 5
            int endX = startX
            int endY = dim.height / 2
            try {
                driver.swipe(startX, startY, endX, endY, 200)
                return true
            } catch (e) {
                log.warn("Error on up swipe($startX, $startY, $endX, $endY, 200) : $e.message")
            }
        }
        false
    }

    public boolean swipeDown() {
        if (driver instanceof AppiumDriver) {

            def dim = driver.manage().window().size
            int startX = dim.width / 2
            int startY = dim.height / 2
            int endX = startX
            int endY = dim.height -5
            try {
                driver.swipe(startX, startY, endX, endY, 200)
                return true
            } catch (e) {
                log.warn("Error on up swipe($startX, $startY, $endX, $endY, 200) : $e.message")
            }
        }
        false
    }

    public static boolean isOnListView(AndroidDriver driver){
        driver.findElementsByXPath("//android.widget.FrameLayout/android.widget.ListView").size()==1
    }

    public static boolean closeListView(AndroidDriver driver){
        if( isOnListView(driver) )
            androidDriverBackButton(driver)
    }

}
