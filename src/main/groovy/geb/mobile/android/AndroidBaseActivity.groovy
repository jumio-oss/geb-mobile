package geb.mobile.android

import geb.Page
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidKeys
import org.openqa.selenium.interactions.Actions

/**
 * Created by gmueksch on 26.06.14.
 *
 */
abstract class AndroidBaseActivity extends Page {

    static at = {
        waitFor {
            getActivityName() ? currentActivity == getActivityName() : true
        }
    }

    static content = {
        back { new Actions(driver).sendKeys(SelendroidKeys.BACK) }
        menu { new Actions(driver).sendKeys(SelendroidKeys.MENU) }
        home { new Actions(driver).sendKeys(SelendroidKeys.ANDROID_HOME) }
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




}
