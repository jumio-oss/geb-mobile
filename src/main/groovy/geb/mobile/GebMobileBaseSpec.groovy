package geb.mobile

import geb.mobile.helper.GebMobileScreenshotRule
import geb.spock.GebSpec
import groovy.util.logging.Slf4j
import io.appium.java_client.AppiumDriver
import org.junit.Rule
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

    @Rule
    public GebMobileScreenshotRule screenShotRule = new GebMobileScreenshotRule(baseSpec:this)


    BufferedImage getScreenShotAsImage() {
        ImageIO.read(new ByteArrayInputStream(driver.getScreenshotAs(OutputType.BYTES)))
    }

    /**
     * expects that something before startet a camera app
     * switches to native context
     * looks up a camera activity class
     * if your camera-activity is not working, see at the findPage desc
     * your camera activity should have a
     *   static = {
     *      takePicture{ //do something here }
     *   }
     * @return the return value of takePicture
     */
    public def doCameraFlow() {

        withNativeApp {
            isAt( owner.findPage() )
            takePicture
        }
    }

    /**
     * Takes the appPackage, checks if it exists as Class ( it's wierd, but the appPackes inside the pageSource
     * is sometimes a ClassName
     * if only a package, the <pkgName>.CameraActivity is looked up with Class.forName
     *
     * to use this functionality:
     *   - check the packageName from the pageSource
     *   - create a class with that package name and/or .CameraActivity
     *   - make a good at checker see geb documentation
     *
     *
     * @return
     */
    def findPage(){
        String pkg = getPackage()
        def parts = pkg.split(/\./)
        def page
        if( parts[-1].charAt(0).isUpperCase() )
            page =  _getPageForClass(pkg)
        if( !page ){
            page = _getPageForClass(parts[0..-1].join('.')+'.CameraActivity')
        }
        if( !page ){
            driver.getPageSource()
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
        if( driver instanceof AppiumDriver )
            driver.getContextHandles().contains(contextName)
        else
            false
    }

    /**
     * switch appium driver into the given context, if not already set
     * executes the closure
     * switches the context back to what it was before
     * ! if not appium return false
     * @param newContext
     * @param block
     * @return the return value of the closure, or false if not appium or given context not available
     */
    public def withContext(String newContext, Closure block) {
        if (!isContextAvailable(newContext)) {
            log.warn "Context $newContext not available, skipping block execution"
            return false
        }
        def oldContext = ((AppiumDriver) driver).getContext()
        if (newContext != oldContext) driver.context(newContext)
        def ret
        try {
            //TODO: wait that the new context is ready: how to check with waitFor
            sleep(1000)
            ret = block.call()
        } catch (e) {
            log.error("Error calling block: $e.message", e)
        }
        driver.context(oldContext)
        return ret
    }

    /**
     * Run closure in the native mode,
     * switches the appium driver before into the NATIVE_APP context if not already there
     * @param closure
     * @return
     */
    public def withNativeApp(Closure closure) {
        withContext(CONTEXT_NATIVE_APP, closure)
    }

    /**
     * Run closure in the webView Mode
     * switches the appium driver into the WEBVIEW_1 context
     * @param closure
     * @return
     */
    public def withWebView(Closure closure) {
        withContext(CONTEXT_WEBVIEW, closure)
    }

    public String getPackage() {
        def hierarchy = new XmlSlurper().parseText(driver.pageSource)
        hierarchy.'android.widget.FrameLayout'.@package.text()
    }


}

