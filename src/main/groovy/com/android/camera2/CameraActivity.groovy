package com.android.camera2

import geb.mobile.android.AndroidBaseActivity

/**
 * Nexus 4 Camera
 */
class CameraActivity extends AndroidBaseActivity{

    static content = {
        shutter{ $("#com.android.camera2:id/shutter_button")}
        done(required:false){ $("#com.android.camera2:id/done_button")}

        takePicture {
            shutter.click()
            if(!done.isDisplayed() ) back()
            else done.click()
        }


    }

    @Override
    String getActivityName() {
        null
    }


}
