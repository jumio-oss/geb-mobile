package geb.mobile.android.uiautomator.activities

import geb.mobile.android.AndroidBaseActivity

/**
 * Created by gmueksch on 23.06.14.
 */
class HomeScreenActivity extends AndroidBaseActivity {

    static content = {
        pageTitle { $("#title").text() }
        buttonTest { $("#buttonTest") }
        startWebviewButton { $("#buttonStartWebview") }
        startUserRegistrationButton { $("#startUserRegistration") }
        waitingButton { $("#waitingButtonTestCD") }
        inputAddsCheckbox { $("#input_adds_check_box") }
        visibleButton { $("#visibleButtonTest") }
        visibleTextView { $("#visibleTextView") }
        myTextField { $("#my_text_field") }
    }
}
