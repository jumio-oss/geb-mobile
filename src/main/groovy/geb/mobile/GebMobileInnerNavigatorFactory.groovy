package geb.mobile

import geb.mobile.android.AndroidInstrumentationNonEmptyNavigator
import geb.mobile.android.AndroidUIAutomatorNonEmptyNavigator
import geb.mobile.ios.AppiumIosInstrumentationNonEmptyNavigator
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
import org.uiautomation.ios.client.uiamodels.impl.RemoteIOSDriver
import sun.reflect.generics.reflectiveObjects.NotImplementedException

/**
 * This Factory decides, which NonEmptyNavigator will be created for the WebElements from the Driver
 * This is not always clear, cause
 *
 * AppiumDriver could case 3 different implementations:
 * Android:
 *  Native-Apps:
 *  APILevel > 16 --> AndroidUIAutomator
 *  APILevel < 16 --> InstrumentationFramework
 *
 *  Mobile Site:
 *      Instrumentation Framework
 *
 *  Hybrid Apps or Apps where you call the Camera or other stuff:
 *      both   AndroidUIAutomator and InstrumentationFramework
 *
 * IOS:
 *   IosInstrumentation
 *
 * Selendroid:
 *    InstrumentationFramework
 *
 * IosDriver:
 *   IosInstrumentation
 *
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
                                     ios       : AppiumIosInstrumentationNonEmptyNavigator
    ]


    static Map<Browser, Class> _innerNavigators = [:]

    private Class figureCorrectInnerNavigator(browser) {
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
                } else if (browser.driver instanceof RemoteIOSDriver) {
                    clazz = IosInstrumentationNonEmptyNavigator
                } else if (browser.driver instanceof AppiumDriver) {
                    if (platformName == "Android" || platform == Platform.ANDROID) {
                        if (browserName.toLowerCase() in ['browser', 'chromium', 'chrome']) {
                            clazz = AndroidInstrumentationNonEmptyNavigator
                        } else {
                            clazz = AndroidUIAutomatorNonEmptyNavigator
                        }
                    } else {
                        clazz = AppiumIosInstrumentationNonEmptyNavigator
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
        return clazz
    }

    /**
     * If {@code elements != null && elements.size() > 0} a {@link geb.navigator.NonEmptyNavigator is returned, otherwise {@link EmptyNavigator}.
     * @param browser The browse to associate with the navigator
     * @param elements The elements to back the navigator
     * @return The newly created navigator
     */
    Navigator createNavigator(Browser browser, List<WebElement> elements) {
        if (!elements) return new EmptyNavigator(browser)
        return figureCorrectInnerNavigator(browser).newInstance(browser, elements)
    }

}
