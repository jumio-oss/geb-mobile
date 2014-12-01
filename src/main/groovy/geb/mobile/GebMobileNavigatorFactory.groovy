package geb.mobile

import geb.Browser
import geb.navigator.Navigator
import geb.navigator.factory.AbstractNavigatorFactory
import geb.navigator.factory.InnerNavigatorFactory
import geb.navigator.factory.NavigatorBackedNavigatorFactory
import geb.navigator.factory.NavigatorFactory
import groovy.util.logging.Slf4j
import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement

/**
 * Created by gmueksch on 23.06.14.
 */
@Slf4j
class GebMobileNavigatorFactory implements NavigatorFactory {


    private final Browser browser
    private final GebMobileInnerNavigatorFactory innerNavigatorFactory

    private Navigator _base ;

    String context

    GebMobileNavigatorFactory(Browser browser) {
        this(browser, new GebMobileInnerNavigatorFactory() )

    }

    GebMobileNavigatorFactory(Browser browser, GebMobileInnerNavigatorFactory innerNavigatorFactory) {
        this.browser = browser
        this.innerNavigatorFactory = innerNavigatorFactory
        this.innerNavigatorFactory.navigatorFactory = this
    }

    @Override
    Navigator getBase() {
        if( _base == null )
            _base = createFromWebElements([new RemoteWebElement()])

        return _base
        //innerNavigatorFactory.createNavigator(browser,null)
        //xpath works with all selenium implementations...
//        log.debug("Create Navigator from Base...")
//        def drv = browser.driver
//        List<WebElement> list
//        if( drv instanceof AndroidDriver ){
//            list = drv.findElementsByAndroidUIAutomator("new UiSelector().className(android.widget.FrameLayout).index(0)")
//            log.debug("Loaded from Base 'new UiSelector().className(android.widget.FrameLayout).index(0)' with ${list.size()} Elements")
//        }else {
//            list = browser.driver.findElementsByXPath("//*") as List
//            log.debug("Loaded from Base '//*' with ${list.size()} Elements")
//        }
//        createFromWebElements(list)
    }

    protected Browser getBrowser() {
        return browser
    }

    Navigator createFromWebElements(Iterable<WebElement> elements) {
        List<WebElement> filtered = []
        elements.each {
            if (it != null) {
                filtered << it
            }
        }
        innerNavigatorFactory.createNavigator(browser, filtered)
    }

    Navigator createFromNavigators(Iterable<Navigator> navigators) {
        List<WebElement> filtered = []
        navigators.each {
            if (it != null) {
                filtered.addAll(it.allElements())
            }
        }
        innerNavigatorFactory.createNavigator(browser, filtered)
    }

    NavigatorFactory relativeTo(Navigator newBase) {
        new NavigatorBackedNavigatorFactory(newBase, innerNavigatorFactory)
    }



}
