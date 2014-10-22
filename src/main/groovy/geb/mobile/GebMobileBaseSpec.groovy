package geb.mobile

import geb.Page
import geb.spock.GebSpec
import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import io.selendroid.SelendroidDriver
import org.openqa.selenium.OutputType
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

    public boolean doCameraFlow() {

        withNativeApp {
            isAt( owner._findCameraPage() )
            takePicture
        }
    }

    def _findCameraPage(){
        String pkg = getPackage()
        def parts = pkg.split(/\./)
        def page
        if( parts[-1].charAt(0).isUpperCase() )
            page =  _getPageForClass(pkg)
        if( !page ){
            page = _getPageForClass(parts[0..-1].join('.')+'.CameraActivity')
        }
        if( !page ){
            throw new Exception("CameraActivity not found with $pkg")
        }
        return page

    }

    def _getPageForClass(String className){
        try {
            log.debug("Checking for $className")
            return Class.forName(className)
        }catch(e){
            log.debug("Class $className not found")
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
        def oldContext = ((AppiumDriver) driver).getContext()
        if (newContext != oldContext) driver.context(newContext)
        try {
            //TODO: wait that the new context is ready: how to check with waitFor
            sleep(1000)
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

