package com.sec.android.app.camera

import geb.mobile.android.AndroidBaseActivity
import groovy.util.logging.Slf4j

/**
 * Samsung Galaxy S4/S5
 */
@Slf4j
class CameraActivity extends AndroidBaseActivity {


    static content = {
        save1(required: false) { $('#save') }
        save2(required: false) { $('text("Save")') }
        ok1(required: false) { $('#ok')  }
        ok2(required: false) { $("text('Ok')") }
        ok3(required: false) { $("name='OK'") }
        discard(required: false) { $('#discard') }

        takePicture {
            sleep(1000)
            shutter()
            waitFor(30) {
                if (save1.isDisplayed()){
                    save1.click()
                }else if (save2.isDisplayed()){
                     save2.click()
                }else if (ok1.isDisplayed()){
                     ok1.click()
                }else if (ok2.isDisplayed()){
                     ok2.click()
                }else if (ok3.isDisplayed()){
                    ok3.click()
                }
            }
        }
    }

    def shutter() {
        def (x, y) = getCameraShutterButtonCoordinates()
        performTap(x, y)
    }

    @Override
    String getActivityName() {
        null
    }


}
