package geb.mobile.android.instrumention.activities

import geb.mobile.android.AndroidBaseActivity

/**
 * Created by gmueksch on 23.06.14.
 */
class HomeScreenActivity extends AndroidBaseActivity {

    static content = {
        pageTitle { $("#title").text() }
        buttonTest { $("#buttonTest") }
        startWebviewButton { $("name='buttonStartWebviewCD'") }
        startUserRegistrationButton { $("#startUserRegistration") }
        waitingButton { $("#waitingButtonTest") }
        inputAddsCheckbox { $("#input_adds_check_box") }
        visibleButton { $("#visibleButtonTest") }
        visibleTextView { $("#visibleTextView") }
        myTextField { $("#my_text_field") }
    }
}
