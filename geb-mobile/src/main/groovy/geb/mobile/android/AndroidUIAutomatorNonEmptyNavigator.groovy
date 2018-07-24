package geb.mobile.android

import geb.Browser
import geb.mobile.AbstractMobileNonEmptyNavigator
import geb.navigator.EmptyNavigator
import geb.navigator.Navigator
import groovy.util.logging.Slf4j
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidElement
import org.apache.commons.lang3.NotImplementedException
import org.apache.http.MethodNotSupportedException
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * Created by gmueksch on 23.06.14.
 */
@Slf4j
class AndroidUIAutomatorNonEmptyNavigator extends AbstractMobileNonEmptyNavigator<AndroidDriver<AndroidElement>> {

    AndroidUIAutomatorNonEmptyNavigator(Browser browser, Collection<? extends MobileElement> contextElements) {
        super(browser, contextElements)
    }

    private String getAppPackage() {
        driver.capabilities.getCapability("appPackage")
    }

    @Override
    Navigator find(String selectorString) {
        log.debug "Selector: $selectorString"

        if (selectorString.startsWith("//")) {
            return navigatorFor(driver.findElements(By.xpath(selectorString)))
        }

        if (selectorString.startsWith("#")) {
            String value = selectorString.substring(1)
            if( value.indexOf(':')>0 ) {
                try{
                    return navigatorFor(driver.findElementsByAndroidUIAutomator("resourceId(\"$value\")"))
                }catch(e){
                    log.warn("Selector $selectorString: findElementsByAndroidUIAutomator resourceId(\"$value\") : $e.message")
                    return new EmptyNavigator()
                }
            }else {
                def apk = getAppPackage()
                if( !apk ) log.warn("for Selector $selectorString : AppPackage is emtpy, result may not be correct ")
                try {
                    return navigatorFor(driver.findElementsByAndroidUIAutomator("resourceId(\"$appPackage:id/$value\")"))
                }catch(e){
                    if( e.message =~ /BROWSER_TIMEOUT/ ) {
                        log.error("Got Browser-Timeout, browser maybe already closed: $e.message")
                        throw e
                    }
                    log.warn("Selector $selectorString: findElementsByAndroidUIAutomator resourceId(\"$appPackage:id/$value\") ")
                    return new EmptyNavigator()
                }
            }
        } else if( selectorString.startsWith(".") ){
            //This works only on WEB_VIEW
            return navigatorFor(driver.findElementsByCssSelector(selectorString) )
        } else {
            selectorString = selectorString.replaceAll("'", '\"')
            log.debug "Using UIAutomator with: $selectorString"
            navigatorFor(driver.findElementsByAndroidUIAutomator(selectorString))
        }

    }


    @Override
    Navigator unique() {
        new AndroidUIAutomatorNonEmptyNavigator(browser, contextElements.unique(false))
    }

    protected getInputValue(MobileElement input) {
        def value
        def tagName = input.tagName

        if (tagName == "android.widget.Spinner") {
            if( AndroidHelper.isOnListView(driver) )
                value = input.findElementByAndroidUIAutomator("fromParent(new UiSelector().checked(true))").getText()
            else
                value = input.findElementByAndroidUIAutomator("fromParent(new UiSelector())").getText()
        } else if (tagName in ['android.widget.CheckBox','android.widget.Switch']) {
            value = input.getAttribute("checked")
        } else {
            value = input.getText()
        }
        log.debug("inputValue for $tagName : $value ")
        value
    }

    @Override
    void setInputValue(MobileElement input, Object value) {

        def tagName = input.tagName
        log.debug("setInputValue: $input, $tagName")
        if (tagName == "android.widget.Spinner") {
            if (getInputValue(input) == value) return
            setSpinnerValue(input,value)
            AndroidHelper.closeListView(driver)

        } else if (tagName in ['android.widget.CheckBox', 'android.widget.RadioButton' ,'android.widget.Switch']) {
            def checked = input.getAttribute("checked")?.toBoolean()
            if ( !checked && value) {
                input.click()
            } else if (checked && !value ) {
                input.click()
            }
        }else {
            //TODO: hideKeyboard after sendKeys
            //TODO: clear Copy/Paste 
//            input.clear()
            //input.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT,Keys.END),value);
            input.sendKeys value as String

            try{
                driver.hideKeyboard()
            }catch(e){
                log.warn("Hiding keyboard propably worked, but has thrown exc: $e.message")
            }
        }
    }

    private void setSpinnerValueWithScrollTo(MobileElement input, value) {
        try {
            input.click()
            driver.scrollTo(value?.toString())?.click()
        } catch (e) {
            log.warn("Could not set $value to $input.tagName : $e.message")
        }
    }

    private void setSpinnerValueWithScrollToExact(MobileElement input, value) {
        try {
            input.click()
            driver.scrollTo(value?.toString())?.click()
        } catch (e) {
            log.warn("Could not set $value to $input.tagName : $e.message")
        }
    }

    private void setSpinnerValue(MobileElement input, value) {
        try {
            def currVal = getInputValue(input)
            log.debug("Setting $value to Spinner: currentVal: ${currVal}")
            input.click()
            //input.properties
            driver.findElementByAndroidUIAutomator("text(\"$value\")").click()
            //input.findElementByAndroidUIAutomator("fromParent(new UiSelector().text(\"$value\"))")?.click()
            if (getInputValue(input) == value) return
            if( AndroidHelper.isOnListView(driver) ) {
                log.debug("Value not set and on ListView: Scrolling to $value")
                browser.driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().className(\"android.widget.ListView\")).flingBackward();")
                driver.findElementByAndroidUIAutomator("text(\"$value\")").click()
            }
            //input.findElementByAndroidUIAutomator("fromParent(new UiSelector().text(\"$value\"))")?.click()
        } catch (e) {
            log.warn("Error selecting with UiAutomator: $e.message")
        }

    }



    private void flingBack(){
        driver.execute("new UiScrollable(new UiSelector().className('android.widget.ListView')).flingBackward();",null)
    }

    @Override
    boolean isDisabled() {
        return !super.isEnabled()
    }

    @Override
    Navigator leftShift(Object value) {
        throw new NotImplementedException()
    }
}
