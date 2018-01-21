package geb.mobile.android.uiautomator.activities

import geb.mobile.android.AndroidBaseActivity

/**
 * Created by gmueksch on 23.06.14.
 */
class WebViewActivity extends AndroidBaseActivity {

    static content = {
        pageTitle { $("#title").text() }
        goBack(wait: true) { $("text('Go to home screen')") }
        name { $("#tableRowWebview") }
    }
}
