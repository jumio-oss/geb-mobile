package geb.mobile.android.instrumention.specification.appium

import geb.mobile.android.instrumention.activities.HomeScreenActivity
import geb.mobile.android.instrumention.activities.WebViewActivity
import spock.lang.Stepwise

/**
 * Created by gmueksch on 23.06.14.
 */
@Stepwise
class AppiumWithGebMobileAutomationTestWithPagesSpec extends AppiumWithGebMobileAutomationTestBase {

    def "open test-app and enter text "() {
        given:
        at HomeScreenActivity
        when:
        myTextField = "selendroid"
        then:
        myTextField.text() == "selendroid"
    }

    def "find Button, click and return"() {
        when:
        waitFor {
            at HomeScreenActivity
        }
        startWebviewButton.click()
        then:
        waitFor {
            at WebViewActivity
        }
        when:
        goBack.click()
        then:
        waitFor {
            at HomeScreenActivity
        }
    }

    def "find Button, click and return with back"() {
        when:
        at HomeScreenActivity
        startWebviewButton.click()
        then:
        waitFor {
            at WebViewActivity
        }
        when:
        back.perform()
        then:
        waitFor {
            at HomeScreenActivity
        }
    }


}
