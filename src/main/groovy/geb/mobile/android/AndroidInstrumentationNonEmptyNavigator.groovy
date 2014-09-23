package geb.mobile.android

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
import io.selendroid.SelendroidDriver
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import java.util.regex.Pattern

import static java.util.Collections.EMPTY_LIST

/**
 * Created by gmueksch on 23.06.14.
 *
 Id:  Finds the element by Id.
 name: Finds the element by content description (accessibility label).
 link text:  Finds the element by text.
 partial link text: Finds the element by partial text.
 class: Finds the element by full class name (e.g. android.widget.Button).
 xpath:  Finds the element by a xpath expression.
 tag name:  Finds the element by tag name.
 */
@Slf4j
class AndroidInstrumentationNonEmptyNavigator extends AbstractMobileNonEmptyNavigator<SelendroidDriver> {

    AndroidInstrumentationNonEmptyNavigator(Browser browser, Collection<? extends WebElement> contextElements) {
        super(browser,contextElements)
    }

    static def pat = ~/(\w+)='?([\w ]+)'?/

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
            navigatorFor driver.findElements(By."$key"(value))
        }else{
            log.warn("Ether key '$key' or value '$value' is not filled")
            new EmptyNavigator()
        }
    }



    protected getInputValue(WebElement input) {
        def value = null
        def type = input.getAttribute("type")
        if (input.tagName == "select") {
            def select = new SelectFactory().createSelectFor(input)
            if (select.multiple) {
                value = select.allSelectedOptions.collect { getValue(it) }
            } else {
                value = getValue(select.firstSelectedOption)
            }
        } else if (type in ["checkbox", "radio"]) {
            if (input.isSelected()) {
                value = getValue(input)
            } else {
                if (type == "checkbox") {
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
        if (input.tagName == "select") {
            setSelectValue(input, value)
        } else if (input.getAttribute("type") == "checkbox") {
            if (getValue(input) == value.toString() || value == true) {
                if (!input.isSelected()) {
                    input.click()
                }
            } else if (input.isSelected()) {
                input.click()
            }
        } else if (input.getAttribute("type") == "radio") {
            if (getValue(input) == value.toString() || labelFor(input) == value.toString()) {
                input.click()
            }
        } else if (input.getAttribute("type") == "file") {
            input.sendKeys value as String
        } else {
            input.clear()
            input.sendKeys value as String
        }
    }

    protected getValue(WebElement input) {
        input?.getAttribute("value")
    }


    @Override
    Navigator unique() {
        new AndroidInstrumentationNonEmptyNavigator(browser, contextElements.unique(false))
    }


}