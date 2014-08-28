package geb.mobile

import geb.spock.GebSpec
import groovy.util.logging.Slf4j
import io.selendroid.SelendroidConfiguration
import io.selendroid.SelendroidDriver
import io.selendroid.SelendroidKeys
import io.selendroid.SelendroidLauncher
import org.openqa.selenium.OutputType
import org.openqa.selenium.interactions.Actions
import spock.lang.Ignore
import spock.lang.Shared

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Created by gmueksch on 26.06.14.
 */
@Ignore
@Slf4j
class GebMobileBaseSpec extends GebSpec {

    public static String appUT_absolutePath = "appUT.absolutePath"

    @Shared
    def selendroidServer

    def setupSpec() {

        def appUTpath = System.getProperty(appUT_absolutePath, null);
        if (appUTpath) {
            SelendroidConfiguration config = new SelendroidConfiguration();
            config.addSupportedApp(appUTpath);
            config.setForceReinstall(true)
            def p = ~/^appUT_(\w+)$/
            System.properties.each{ k,v->
                def m = p.matcher(k)
                if( m.matches() ){
                    try {
                        log.info("Setting set${m[0][1]}($v)")
                        config."set${m[0][1]}"(v)
                    }catch(e){
                        log.warn("problem setting value to config: $e.message")
                    }
                }
            }
            //config.
            selendroidServer = new SelendroidLauncher(config);
            selendroidServer.launchSelendroid();
        }
    }

    def cleanupSpec() {
        if (selendroidServer != null) {
            selendroidServer.stopSelendroid();
        }
    }

    def clickHome(){
        new Actions(driver).sendKeys(SelendroidKeys.ANDROID_HOME)
    }

    def clickBack(){
        new Actions(driver).sendKeys(SelendroidKeys.BACK)
    }

    BufferedImage getScreenShotAsImage(){
        ImageIO.read(new ByteArrayInputStream(((SelendroidDriver)driver).getScreenshotAs(OutputType.BYTES)))
    }

    def tapAt(int x,int y){
        ((SelendroidDriver)driver).getTouch().down(x,y)
    }


}

