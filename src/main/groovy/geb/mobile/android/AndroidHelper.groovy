package geb.mobile.android

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.selendroid.SelendroidKeys
import org.openqa.selenium.interactions.Actions

/**
 * Created by gmueksch on 21/01/15.
 */
class AndroidHelper {

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
}
