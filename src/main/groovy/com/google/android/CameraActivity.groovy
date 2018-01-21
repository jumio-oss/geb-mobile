package com.google.android

import geb.mobile.android.AndroidBaseActivity

class CameraActivity extends AndroidBaseActivity{

    static content = {
        shutter{ $("#shutter_button")}
        done{ $("#done_button")}
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
