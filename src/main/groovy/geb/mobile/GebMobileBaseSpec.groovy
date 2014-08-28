package geb.mobile

import geb.spock.GebSpec
import groovy.util.logging.Slf4j
import io.selendroid.SelendroidDriver
import io.selendroid.SelendroidKeys
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

    def clickHome() {
        new Actions(driver).sendKeys(SelendroidKeys.ANDROID_HOME)
    }

    def clickBack() {
        new Actions(driver).sendKeys(SelendroidKeys.BACK)
    }

    BufferedImage getScreenShotAsImage() {
        ImageIO.read(new ByteArrayInputStream(((SelendroidDriver) driver).getScreenshotAs(OutputType.BYTES)))
    }

    def tapAt(int x, int y) {
        ((SelendroidDriver) driver).getTouch().down(x, y)
    }


}

