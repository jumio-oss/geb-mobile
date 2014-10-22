package geb.mobile

import geb.Browser
import geb.navigator.Navigator
import geb.navigator.factory.AbstractNavigatorFactory
import geb.navigator.factory.InnerNavigatorFactory
import geb.navigator.factory.NavigatorBackedNavigatorFactory
import geb.navigator.factory.NavigatorFactory
import org.openqa.selenium.WebElement

/**
 * Created by gmueksch on 23.06.14.
 */
class GebMobileNavigatorFactory implements NavigatorFactory {


    private final Browser browser
    private final GebMobileInnerNavigatorFactory innerNavigatorFactory

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
        //xpath works with all selenium implementations...
        List<WebElement> list = browser.driver.findElementsByXPath("//*") as List
        createFromWebElements(list)
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
