package geb.mobile

import geb.Browser
import geb.navigator.Locator
import geb.navigator.Navigator
import geb.navigator.factory.NavigatorBackedNavigatorFactory
import geb.navigator.factory.NavigatorFactory
import groovy.util.logging.Slf4j
import org.openqa.selenium.By
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
        this.browser = browser
        this.innerNavigatorFactory = new GebMobileInnerNavigatorFactory(this)
    }

    @Override
    Navigator getBase() {
        if(_base)return _base
        _base = innerNavigatorFactory.createNavigator(browser,null)
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

    @Override
    Locator getLocator() {
        return getBase()
    }
}
