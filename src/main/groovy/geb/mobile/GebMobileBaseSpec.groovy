package geb.mobile

import geb.mobile.android.device.VendorSpecific
import geb.spock.GebSpec
import groovy.transform.Trait
import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidDriver
import io.selendroid.SelendroidKeys
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.interactions.Actions
import spock.lang.Ignore

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Created by gmueksch on 26.06.14.
 */
@Ignore
@Slf4j
class GebMobileBaseSpec extends GebSpec {

    BufferedImage getScreenShotAsImage() {
        ImageIO.read(new ByteArrayInputStream(((SelendroidDriver) driver).getScreenshotAs(OutputType.BYTES)))
    }

    public boolean takePicture(){
        withNativeApp {
            String pkg = getPackage()
            try {
                String clName = "${pkg}.CameraActivity"
                geb.Page p = Class.forName(clName)
                at p
            } catch (e) {
                log.error("")
            }
        }
    }

    /**************** APPIUM Specific Stuff ********************/

    public GebMobileNavigatorFactory getMobileNavigatorFactory() {
        return browser.getNavigatorFactory()
    }
    public static String CONTEXT_NATIVE_APP = "NATIVE_APP"

    public static String CONTEXT_WEBVIEW = "WEBVIEW_1"

    public boolean isContextAvailable(String contextName) {
        ((AppiumDriver) driver).getContextHandles().contains(contextName)
    }

    public def withContext(String newContext, Closure block) {
        if (!isContextAvailable(newContext)) {
            log.warn "Context $newContext not available, skipping block execution"
            return false
        }
        def oldContext = driver.context()
        if (newContext != oldContext) driver.context(newContext)
        try {
            block.call()
        } catch (e) {
            log.error("Error calling block: $e.message", e)
        }
        driver.context(oldContext)
    }

    public def withNativeApp(Closure closure) {
        withContext(CONTEXT_NATIVE_APP, closure)
    }

    public def withWebView(Closure closure) {
        withContext(CONTEXT_WEBVIEW, closure)
    }

    public String getPackage() {
        def hierarchy = new XmlSlurper().parseText(driver.pageSource)
        hierarchy.'android.widget.FrameLayout'.@package.text()
    }



}

