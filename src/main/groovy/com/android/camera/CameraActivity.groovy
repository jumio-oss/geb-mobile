package com.android.camera

import geb.mobile.android.AndroidBaseActivity

/**
 * Nexus 5 Camera
 */
class CameraActivity extends AndroidBaseActivity{

    static content = {
        shutter{ $("#capture_button_photo")}
        done{ $("#select_this")}

        takePicture {
            shutter.click()
            done.click()
        }
    }

    @Override
    String getActivityName() {
        null
    }
}
