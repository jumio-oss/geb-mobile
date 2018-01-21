package com.android.camera2

import geb.mobile.android.AndroidBaseActivity

/**
 * Nexus 4 Camera
 */
class CameraActivity extends AndroidBaseActivity{

    static content = {
        shutter(required:false,wait:true){ $("#com.android.camera2:id/shutter_button")}
        done(required:false,wait:true){ $("#com.android.camera2:id/done_button")}

        takePicture {
            waitFor(10,1) {
                shutter.click()
                if (done.isDisplayed())
                    done.click()
                else
                    back()
            }
        }


    }

    @Override
    String getActivityName() {
        null
    }


}
