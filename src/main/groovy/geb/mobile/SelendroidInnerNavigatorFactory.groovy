package geb.mobile

import geb.mobile.android.AndroidInstrumentationNonEmptyNavigator
import geb.mobile.android.AndroidUIAutomatorNonEmptyNavigator
import geb.mobile.ios.IosInstrumentationNonEmptyNavigator
import geb.Browser
import geb.navigator.EmptyNavigator
import geb.navigator.Navigator
import geb.navigator.NonEmptyNavigator
import geb.navigator.factory.InnerNavigatorFactory
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidDriver
import org.openqa.selenium.Capabilities
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import sun.reflect.generics.reflectiveObjects.NotImplementedException

/**
 *
 *
 * Created by gmueksch on 23.06.14.
 *
 */
class SelendroidInnerNavigatorFactory implements InnerNavigatorFactory {

    /**
     * If {@code elements != null && elements.size() > 0} a {@link geb.navigator.NonEmptyNavigator is returned, otherwise {@link EmptyNavigator}.
     * @param browser The browse to associate with the navigator
     * @param elements The elements to back the navigator
     * @return The newly created navigator
     */
    Navigator createNavigator(Browser browser, List<WebElement> elements) {
        if (!elements) return new EmptyNavigator(browser)
        String browserName = browser.driver.capabilities.getCapability("browserName")

        //if SelendroidDriver we return always the Instrumentation Impl
        if (browser.driver instanceof SelendroidDriver) {
            if( browserName == "android" ) return new NonEmptyNavigator( browser,elements )
            else return new AndroidInstrumentationNonEmptyNavigator(browser, elements)
        }
        else if(browser.driver instanceof AppiumDriver) return new AndroidUIAutomatorNonEmptyNavigator(browser,elements)
        else {
            switch (browserName.toLowerCase()) {
                case "selendroid": return new AndroidInstrumentationNonEmptyNavigator(browser, elements)
                case "android": return new AndroidUIAutomatorNonEmptyNavigator(browser, elements)
                default:
                    if( browser.driver.capabilities.getCapability("platformName") == "IOS" ){
                        return new IosInstrumentationNonEmptyNavigator(browser,elements)
                    }
                    throw new NotImplementedException("IOS not implemented yet")
            }
        }
    }

}
