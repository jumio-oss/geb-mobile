package geb.mobile

import geb.mobile.android.AndroidInstrumentationNonEmptyNavigator
import geb.mobile.android.AndroidUIAutomatorNonEmptyNavigator
import geb.mobile.ios.IosInstrumentationNonEmptyNavigator
import geb.Browser
import geb.navigator.EmptyNavigator
import geb.navigator.Navigator
import geb.navigator.NonEmptyNavigator
import geb.navigator.factory.InnerNavigatorFactory
import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidDriver
import org.openqa.selenium.Capabilities
import org.openqa.selenium.Platform
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import sun.reflect.generics.reflectiveObjects.NotImplementedException

/**
 *
 *
 * Created by gmueksch on 23.06.14.
 *
 */
@Slf4j
class GebMobileInnerNavigatorFactory implements InnerNavigatorFactory {


    static def _defaultNavigators = [selendroid: AndroidInstrumentationNonEmptyNavigator,
                                     android   : AndroidUIAutomatorNonEmptyNavigator,
                                     firefox   : AndroidInstrumentationNonEmptyNavigator,
                                     ios       : IosInstrumentationNonEmptyNavigator
    ]


    static Map<Browser, Class> _innerNavigators = [:]

    private Navigator figureCorrectInnerNavigator(browser, elements) {
        def clazz = _innerNavigators[browser]
        if (!clazz) {

            synchronized (_innerNavigators) {
                String browserName = browser.driver.capabilities.getCapability("browserName")
                String platformName = browser.driver.capabilities.getCapability("platformName")
                Platform platform = browser.driver.capabilities.getCapability("platform")
                log.debug("trying to figure out correct Navigator for $browserName, $platformName, ${platform.name()}")
                if (browser.driver instanceof SelendroidDriver) {
                    //if (browserName == "android") clazz = NonEmptyNavigator else
                    clazz = AndroidInstrumentationNonEmptyNavigator
                } else if (browser.driver instanceof AppiumDriver) {
                    if (platformName == "Android" || platform == Platform.ANDROID) {
                        if (browserName in ['Browser', 'Chromium', 'chrome']) {
                            clazz = AndroidInstrumentationNonEmptyNavigator
                        } else {
                            clazz = AndroidUIAutomatorNonEmptyNavigator
                        }
                    } else {
                        clazz = IosInstrumentationNonEmptyNavigator
                    }
                } else {
                    clazz = _defaultNavigators[browserName.toLowerCase()]
                    if (!clazz) clazz = _defaultNavigators[platformName.toLowerCase()]
                }
                if (clazz) {
                    log.info("Using $clazz.name for $browserName, $platformName, ${platform.name()}")
                    _innerNavigators[browser] = clazz
                } else {
                    throw new RuntimeException("Could not determine correct InnerNavigator for $browserName, $platformName, ${platform.name()}")
                }

            }
        }
        return clazz.newInstance(browser, elements)
    }

    /**
     * If {@code elements != null && elements.size() > 0} a {@link geb.navigator.NonEmptyNavigator is returned, otherwise {@link EmptyNavigator}.
     * @param browser The browse to associate with the navigator
     * @param elements The elements to back the navigator
     * @return The newly created navigator
     */
    Navigator createNavigator(Browser browser, List<WebElement> elements) {
        if (!elements) return new EmptyNavigator(browser)
        return figureCorrectInnerNavigator(browser, elements)
//        String browserName = browser.driver.capabilities.getCapability("browserName")
//
//        //if SelendroidDriver we return always the Instrumentation Impl
//        if (browser.driver instanceof SelendroidDriver) {
//            if (browserName == "android") return new NonEmptyNavigator(browser, elements)
//            else return new AndroidInstrumentationNonEmptyNavigator(browser, elements)
//        } else if (browser.driver instanceof AppiumDriver) {
//            if (browser.driver.capabilities.getCapability("platformName") == "Android" || browser.driver.capabilities.getCapability("platform") == Platform.ANDROID) {
//                if (browserName == 'Browser' || browserName == 'Chromium' || browserName == 'chrome')
//                    return new AndroidInstrumentationNonEmptyNavigator(browser, elements)
//                else
//                    return new AndroidUIAutomatorNonEmptyNavigator(browser, elements)
//            }
//            //else if(browser.driver.capabilities.getCapability("browserName") == "safari" )
//            //    return new NonEmptyNavigator( browser, elements )
//            else
//                return new IosInstrumentationNonEmptyNavigator(browser, elements)
//        } else {
//            switch (browserName.toLowerCase()) {
//                case "selendroid": return new AndroidInstrumentationNonEmptyNavigator(browser, elements)
//                case "android": return new AndroidUIAutomatorNonEmptyNavigator(browser, elements)
//                case "firefox": return new AndroidInstrumentationNonEmptyNavigator(browser, elements)
//                default:
//                    if (browser.driver.capabilities.getCapability("platformName") == "IOS") {
//                        return new IosInstrumentationNonEmptyNavigator(browser, elements)
//                    }
//
//                    throw new NotImplementedException("IOS not implemented yet")
//            }
//        }
    }

}
