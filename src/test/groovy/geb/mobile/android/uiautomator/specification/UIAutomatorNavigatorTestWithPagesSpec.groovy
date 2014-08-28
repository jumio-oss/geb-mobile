package geb.mobile.android.uiautomator.specification

import geb.mobile.android.uiautomator.activities.HomeScreenActivity
import geb.mobile.android.uiautomator.activities.WebViewActivity
import spock.lang.Stepwise

/**
 * Created by gmueksch on 23.06.14.
 */
@Stepwise
class UIAutomatorNavigatorTestWithPagesSpec extends UIAutomatorNavigatorTestBase {

    def "open test-app and enter text "() {
        given:
        at HomeScreenActivity
        when:
        myTextField << "selendroid"
        then:
        myTextField.text() =~ /[sS]elendroid/
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
