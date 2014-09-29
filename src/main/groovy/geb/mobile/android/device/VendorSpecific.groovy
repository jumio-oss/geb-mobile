package geb.mobile.android.device

import io.appium.java_client.AppiumDriver
import org.openqa.selenium.Dimension

/**
 * Created by gmueksch on 29.09.14.
 */
class VendorSpecific {


    static def cameraVendorMapping = [
            'com.android.camera2': nexus4Cam,
            'com.google.android.GoogleCamera': nexus4Cam,
            'com.android.camera': nexus5Cam,
            'com.sec.android.app.camera': samsungGalaxy,
            'com.htc.camera': htcCam
    ]

    static def nexus4Cam = { AppiumDriver driver ->
        waitFor {
            driver.findElementByAndroidUIAutomator('resourceId("com.android.camera2:id/shutter_button")')
        }.click()
        waitFor { driver.findElementByAndroidUIAutomator('resourceId("com.android.camera2:id/done_button")') }.click()
        true
    }

    static def nexus5Cam = { AppiumDriver driver ->
        waitFor {
            driver.findElementByAndroidUIAutomator('resourceId("com.android.camera:id/capture_button_photo")')
        }.click()
        waitFor { driver.findElementByAndroidUIAutomator('resourceId("com.android.camera:id/select_this")') }.click()
        true
    }

    static def samsungGalaxy = { AppiumDriver driver ->
        def (x, y) = getCameraShutterButtonCoordinates(driver)
        new io.appium.java_client.TouchAction(driver).tap(x, y).perform()
        def usePic = driver.findElementByAndroidUIAutomator('text("Save")')
        if (!usePic) usePic = driver.findElementByAndroidUIAutomator('text("OK")')
        usePic.click()
        true
    }

    static def htcCam = { AppiumDriver driver ->
        def dim = getScreenDimension(driver)
        new io.appium.java_client.TouchAction(driver).tap(dim.width / 2, dim.height / 2).perform()
        sleep 200
        new io.appium.java_client.TouchAction(driver).tap(dim.width - 100, dim.height / 2).perform()
        sleep 200
        new io.appium.java_client.TouchAction(driver).tap(dim.width - 100, dim.height / 2).perform()
    }

    public Dimension getScreenDimension(driver) {
        driver.manage().window().getSize()
    }

    public def getCameraShutterButtonCoordinates(driver) {
        def dim = getScreenDimension(driver)
        [dim.width - 100, dim.height / 2]
    }

}
