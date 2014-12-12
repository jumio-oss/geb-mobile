package geb.mobile.ios

import geb.Browser
import geb.mobile.AbstractMobileNonEmptyNavigator
import geb.navigator.EmptyNavigator
import geb.navigator.Navigator
import geb.navigator.SelectFactory
import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.appium.java_client.ios.IOSDriver
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * Created by gmueksch on 23.06.14.
 */
@Slf4j
class AppiumIosInstrumentationNonEmptyNavigator extends AbstractMobileNonEmptyNavigator<IOSDriver> {

    AppiumIosInstrumentationNonEmptyNavigator(Browser browser, Collection<? extends WebElement> contextElements) {
        super(browser,contextElements)
    }

    static def pat = ~/(\w+)='?([\w,\- ]+)'?/

    @Override
    Navigator find(String selectorString) {
        log.debug "Selector: $selectorString"

        if( selectorString.startsWith("//") ) {
            return navigatorFor(browser.driver.findElements(By.xpath(selectorString)))
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
            navigatorFor driver.findElements(By."$key"(value))
        }else if(selectorString) {
            log.debug("using uiautomator: $selectorString")
            navigatorFor driver.findElementsByIosUIAutomation(selectorString)
        }else{
            log.warn("Ether key '$key' or value '$value' is not filled")
            new EmptyNavigator()
        }
    }

    @Override
    protected getInputValue(WebElement input) {
        def value = null
        def type = input.getTagName()
        if (type == "UIASelect") {
            log.warn("Select not yet implemented, using fallback")
            value = getValue(input)
        } else if (type in ["UIACheckbox", "UIARadio"]) {
            if (input.isSelected()) {
                value = getValue(input)
            } else {
                if (type == "UIACheckbox") {
                    value = false
                }
            }
        } else {
            value = getValue(input)
        }
        value
    }

    @Override
    void setInputValue(WebElement input, value) {
        def attrType = input.getTagName()
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

    protected getValue(WebElement input) {
        input?.getAttribute("value")
    }


    @Override
    boolean isEnabled() {
        return firstElement().enabled
    }

    @Override
    boolean isDisplayed() {
        return firstElement().displayed
    }

    @Override
    Navigator unique() {
        new AppiumIosInstrumentationNonEmptyNavigator(browser, contextElements.unique(false))
    }
}