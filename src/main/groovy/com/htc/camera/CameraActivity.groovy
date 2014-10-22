package com.htc.camera

import geb.mobile.android.AndroidBaseActivity

/**
 * HTC One Camera
 */
class CameraActivity extends AndroidBaseActivity {

    static content = {
        takePicture {
            def dim = getScreenDimension(driver)
            performTap(dim.width / 2, dim.height / 2)
            sleep 200
            performTap(dim.width - 100, dim.height / 2)
            sleep 200
            performTap(dim.width - 100, dim.height / 2)
        }
    }
    @Override
    String getActivityName() {
        null
    }

}
