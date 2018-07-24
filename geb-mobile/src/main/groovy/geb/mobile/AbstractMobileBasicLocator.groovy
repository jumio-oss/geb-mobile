package geb.mobile

import geb.Browser
import geb.navigator.BasicLocator
import geb.navigator.Navigator
import geb.navigator.factory.NavigatorFactory
import org.openqa.selenium.By
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class AbstractMobileBasicLocator implements BasicLocator  {

    Browser browser

    AbstractMobileBasicLocator(Browser browser){
        this.browser = browser
    }

    @Override
    Navigator find(By bySelector) {
        return browser.navigatorFactory.createFromWebElements(browser.driver.findElements(bySelector))
    }

    @Override
    Navigator find(Map<String, Object> attributes, String selector) {
        throw new NotImplementedException(" Navigator find(Map<String, Object> attributes, String selector) ")
    }
}
