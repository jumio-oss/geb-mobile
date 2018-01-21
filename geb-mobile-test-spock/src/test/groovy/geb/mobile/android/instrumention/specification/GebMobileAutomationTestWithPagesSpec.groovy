package geb.mobile.android.instrumention.specification

import geb.mobile.GebMobileBaseSpec
import geb.mobile.android.instrumention.activities.HomeScreenActivity
import geb.mobile.android.instrumention.activities.RegisterUserActivity
import geb.mobile.android.instrumention.activities.VerifyUserActivity
import geb.mobile.android.instrumention.activities.WebViewActivity
import spock.lang.Stepwise

/**
 * Created by gmueksch on 23.06.14.
 */
@Stepwise
class GebMobileAutomationTestWithPagesSpec extends GebMobileBaseSpec {

//   Remove comments, when you want to run from inside an IDE or set the SystemProperties with -D on the Run-Configuration
//    static {
//        System.setProperty("framework", "selendroid")
//        System.setProperty("appUT.package", "io.selendroid.testapp")
//        System.setProperty("appUT.version", "0.10.0")
//        System.setProperty("selendroid_Emulator", "false")
//    }

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

    def "register a new User"(){
        when:
        at HomeScreenActivity
        startUserRegistrationButton.click()
        then:
        at RegisterUserActivity

        when:
        username = "blablub"
        password = "mysecret"
        name     = "Bla Blub"
        email    = "blabal@blublu.com"
        preferedProgrammingLanguage = "Java"
        adds = "true"
        registerUserButton.click()

        then:
        at VerifyUserActivity
        labelEmailData == "blabal@blublu.com"
        labelUsernameData == "blablub"

        when:
        registerUserButton.click()

        then:
        at HomeScreenActivity

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
        back()
        then:
        waitFor {
            at HomeScreenActivity
        }
    }


}
