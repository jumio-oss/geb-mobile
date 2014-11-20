package geb.mobile.android

import geb.Page
import io.appium.java_client.android.AndroidKeyCode
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidDriver
import io.selendroid.SelendroidKeys
import org.openqa.selenium.Dimension
import org.openqa.selenium.interactions.Actions

/**
 * Base for AndroidActivities
 * - Provides auto at checker with the currentActivity
 * - back, menu and home action
 * - performTap
 * - differs between appium and selendroid driver
 * TODO: get rid of the 'instanceof', use mixin or traits, i'm still in the java-thinkin-way...
 *
 */
abstract class AndroidBaseActivity extends Page {

    static at = {
        getActivityName() ? currentActivity == getActivityName() : true
    }

    void back() {
        if (driver instanceof AppiumDriver) driver.sendKeyEvent(AndroidKeyCode.BACK)
        else new Actions(driver).sendKeys(SelendroidKeys.BACK).perform()
    }
    void menu() {
        if( driver instanceof AppiumDriver ) driver.sendKeyEvent(AndroidKeyCode.MENU)
        else new Actions(driver).sendKeys(SelendroidKeys.MENU).perform()

    }
    void home() {
        if( driver instanceof AppiumDriver ) driver.sendKeyEvent(AndroidKeyCode.HOME)
        else new Actions(driver).sendKeys(SelendroidKeys.ANDROID_HOME).perform()

    }

    /**
     * @return the Simple name of this Class, overwrite if classname is not the activityname, or null if it should not be checked
     */
    String getActivityName() {
        return this.getClass().getSimpleName()
    }

    /**
     * Special behavior for Appium
     * @return
     */
    public String getCurrentActivity() {
        if (driver instanceof AppiumDriver) {
            def currAct = driver.currentActivity()
            return currAct.startsWith(".") ? currAct.substring(1) : currAct
        } else {
            def currUrl = driver.currentUrl
            return currUrl.startsWith("and-") ? currUrl.split(/\/\//)[1] : currUrl
        }
    }

    public Dimension getScreenDimension() {
        driver.manage().window().getSize()
    }

    public def getCameraShutterButtonCoordinates() {
        def dim = getScreenDimension()
        [dim.width - 100, dim.height / 2]
    }

    public boolean performTap(x,y){
        if( driver instanceof AppiumDriver)
            new io.appium.java_client.TouchAction(driver).tap(x.intValue(), y.intValue()).perform()
        else if ( driver instanceof SelendroidDriver )
            driver.getTouch().down(x.intValue(), y.intValue())
    }


}
