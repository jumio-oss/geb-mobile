package geb.mobile.android.instrumention.activities

import geb.mobile.android.AndroidBaseActivity

/**
 * Created by gmueksch on 23.06.14.
 */
class WebViewActivity extends AndroidBaseActivity {

    static content = {
        pageTitle  { $("#title").text() }
        goBack  { $("linkText='Go to home screen'") }
        name  { $("#tableRowWebview") }
    }
}
