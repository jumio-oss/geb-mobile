package geb.mobile.ios

import geb.Browser
import geb.Page
import geb.error.UndefinedAtCheckerException
import geb.error.UnexpectedPageException
import geb.mobile.AbstractMobileNonEmptyNavigator
import geb.navigator.AbstractNavigator
import geb.navigator.EmptyNavigator
import geb.navigator.Navigator
import geb.navigator.SelectFactory
import geb.textmatching.TextMatcher
import geb.waiting.WaitTimeoutException
import groovy.util.logging.Slf4j
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.uiautomation.ios.client.uiamodels.impl.RemoteIOSDriver
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import java.util.regex.Pattern

import static java.util.Collections.EMPTY_LIST

/**
 * Created by gmueksch on 23.06.14.
 */
@Slf4j
class IosInstrumentationNonEmptyNavigator extends AbstractMobileNonEmptyNavigator<RemoteIOSDriver> {

    IosInstrumentationNonEmptyNavigator(Browser browser, Collection<? extends WebElement> contextElements) {
        super(browser,contextElements)
    }

    static def pat = ~/(\w+)='?([\w,\- ]+)'?/

    @Override
    Navigator find(String selectorString) {
        log.debug "Selector: $selectorString"

        if( selectorString.startsWith("//") ) {
            return navigatorFor(driver.findElements(By.xpath(selectorString)))
        }
        String all,key,value
        if (selectorString.startsWith("#")) {
            key = "id"
            value = selectorString.substring(1)
        } else {
            def m = pat.matcher(selectorString)
            if (m.matches()) {
                (all,key,value)=m[0]
                log.debug "Match for ${key}='${value}' in $selectorString"
            }
        }
        if( key && value ) {
            log.debug("Key:$key , Value: $value")
            navigatorFor browser.driver.findElements(By."$key"(value))
        }else{
            log.warn("Ether key '$key' or value '$value' is not filled")
            new EmptyNavigator()
        }
    }


    @Override
    void setInputValue(WebElement input, value) {
        def attrType = input.getAttribute("type")
        if (attrType == "UIASelect") {
            setSelectValue(input, value)
        } else if (attrType == "UIACheckbox") {
            if (getValue(input) == value.toString() || value == true) {
                if (!input.isSelected()) {
                    input.click()
                }
            } else if (input.isSelected()) {
                input.click()
            }
        } else if (attrType == "UIARadio") {
            if (getValue(input) == value.toString() || labelFor(input) == value.toString()) {
                input.click()
            }
        } else {
            input.clear()
            input.sendKeys value as String
        }
    }

    @Override
    Navigator unique() {
        new IosInstrumentationNonEmptyNavigator(browser, contextElements.unique(false))
    }
}