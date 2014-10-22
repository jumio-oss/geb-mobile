package com.sec.android.app.camera

import geb.mobile.android.AndroidBaseActivity

/**
 * Samsung Galaxy S4/S5
 */
class CameraActivity extends AndroidBaseActivity{


    static content = {
        save(required:false) { $('#save') }
        ok(required:false){ $('#discard')}

        takePicture {
            sleep( 1000 )
            shutter()
            waitFor{ save.isEnabled() }
            save.click() || ok.click()

        }
    }

    def shutter(){
        def (x, y) = getCameraShutterButtonCoordinates()
        performTap(x,y)
    }

    @Override
    String getActivityName() {
        null
    }


}
