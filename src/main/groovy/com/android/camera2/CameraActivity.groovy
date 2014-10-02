package com.android.camera2

import geb.mobile.android.AndroidBaseActivity

/**
 * Nexus 4 Camera
 */
class CameraActivity extends AndroidBaseActivity{

    static content = {
        shutter{ $("#shutter_button")}
        done{ $("#done_button")}

        takePicture {
            shutter.click()
            done.click()
        }
    }



}
