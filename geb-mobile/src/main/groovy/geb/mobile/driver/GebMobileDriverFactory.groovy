package geb.mobile.driver

import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidElement
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.ios.IOSElement
import org.openqa.selenium.Platform
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver
import org.uiautomation.ios.IOSCapabilities
import org.uiautomation.ios.client.uiamodels.impl.RemoteIOSDriver
import org.uiautomation.ios.communication.device.DeviceType

/**
 * Created by gmueksch on 12.08.14.
 * Based on the your System property settings this class creates the remote driver impl
 * to connect to your selenium server
 * is used in the GebConfig.groovy by geb or directly if you prefere writing old style unit tests
 * TODO: automatically figure out what type of server is running, could be done with json requests
 * TODO: refactor the if/then construct
 */
@Slf4j
class GebMobileDriverFactory {

    public static String FRAMEWORK_APPIUM = "appium"
    public static String FRAMEWORK_SELENDRIOD = "selendroid"
    public static String FRAMEWORK_IOSDRIVER = "iosdriver"
    public static String FRAMEWORK_SELENIUM = "selenium"

    public static URL getURL(String url) {
        String seleniumUrl = System.getProperty("selenium.url")
        if (seleniumUrl) return new URL(seleniumUrl)
        else return new URL(url)
    }

    public static RemoteWebDriver createMobileDriverInstance() {
        log.info("Create Mobile Driver Instance for Framework ${System.properties.framework} ... ")
        if (useAppium()) {
            DesiredCapabilities capa = new DesiredCapabilities()
            //set default platform to android
            capa.setCapability("platformName", "Android");

            System.properties.each { String k, v ->
                def m = k =~ /^appium_(.*)$/
                if (m.matches()) {
                    log.info "setting appium property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }
            def driver

            if (capa.getCapability("platformName") == "Android") {
                capa.setCapability("platform", Platform.ANDROID)
                if( appPackage() ) capa.setCapability("appPackage", appPackage())
                if (!capa.getCapability("deviceName")) capa.setCapability("deviceName", "Android");

                log.info("Create AppiumDriver ")
                try {
                    driver = new AndroidDriver<AndroidElement>(getURL("http://localhost:4723/wd/hub"), capa)
                    //driver.setFileDetector(new LocalFileDetector())
                    //sleep(1000)
                    log.info("Driver created: $driver.capabilities")
                    return driver
                } catch (e) {
                    //
                    log.error("eXC: $e.message", e)
                }
            }else{
                log.info("Create Appium IOSDriver ")
                driver = new IOSDriver<IOSElement>(getURL("http://localhost:4723/wd/hub"), capa)
                driver.setFileDetector(new LocalFileDetector())
                return driver

            }



            if (!driver) throw new RuntimeException("Appiumdriver could not be started")
        } else if (useIosDriver()) {
            DesiredCapabilities capa = new DesiredCapabilities()
            capa.setCapability(IOSCapabilities.BUNDLE_NAME, appPackage())
            if (appVersion())
                capa.setCapability(IOSCapabilities.BUNDLE_VERSION, appVersion())
            capa.setCapability(IOSCapabilities.DEVICE, DeviceType.iphone)

            System.properties.each { String k, v ->
                def m = k =~ /^iosdriver_(.*)$/
                if (m.matches()) {
                    log.info "setting ios property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }

            IOSCapabilities iCapa = new IOSCapabilities(capa.asMap())

            new RemoteIOSDriver(getURL("http://localhost:5555/wd/hub/"), iCapa)
            //new RemoteWebDriver(getURL("http://localhost:5555/wd/hub/"), capa)

        } else if (System.properties.framework == FRAMEWORK_SELENIUM) {
            DesiredCapabilities capa = DesiredCapabilities.firefox()
            System.properties.each { String k, v ->
                def m = k =~ /^selenium_(.*)$/
                if (m.matches()) {
                    log.info "setting ios property: $k , $v"
                    capa.setCapability(m[0][1], v)
                }
            }
            def selenium = new RemoteWebDriver(getURL("http://localhost:4444/wd/hub/"),capa)
            selenium.setFileDetector(new LocalFileDetector())
            return selenium
        } else {
            throw new Exception("Set Systemproperty 'framework' to selendroid or appium")
        }

    }

    public static boolean useAppium() {
        System.properties.framework == FRAMEWORK_APPIUM
    }

    public static boolean useSelendroid() {
        System.properties.framework == FRAMEWORK_SELENDRIOD
    }

    public static boolean useIosDriver() {
        System.properties.framework == FRAMEWORK_IOSDRIVER
    }

    public static String appPackage() {
        System.properties.'appUT.package'
    }

    public static String appVersion() {
        System.properties.'appUT.version'
    }

    /*  Test Helper Methods */
    /**
     *
     * @param framework
     * @param map the capabilities to add
     */
    public static void setFrameWork(String framework, def map = null) {
        System.setProperty("framework", framework)
        map?.each { k, v ->
            def key = "${framework}_${k}"
            if (k in ['appUT.package', 'appUT.version']) System.setProperty(k, v)
            else if(System.getProperty(key,null)==null) System.setProperty(key, v)
        }
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setIosDriver(def map) {
        setFrameWork(FRAMEWORK_IOSDRIVER, map)
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setAppium(def map) {
        setFrameWork(FRAMEWORK_APPIUM, map)
    }

    public static void setAppiumAndroid(def map = []){
        map.platformName = map.platformName ?: 'Android'
        map.appActivity = map.appActivity ?: "MainActivity"
        setAppium(map)
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setAppiumIos(def map) {
        if (!map) map = []
        map.platformName = map.platformName ?: 'iOS'
        map.deviceName = map.deviceName ?: 'iPhone 6'
        setFrameWork(FRAMEWORK_APPIUM, map)
    }

    /**
     * Convinient Method to set Framework and Capabilities for ...
     * @param map
     */
    public static void setSelendroid(def map) {
        setFrameWork(FRAMEWORK_SELENDRIOD, map)
    }


}
