package geb.mobile.android.uiautomator.activities

import geb.mobile.android.AndroidBaseActivity

/**
 * Created by gmueksch on 10.07.14.
 */
class RegisterUserActivity extends AndroidBaseActivity{
    static content = {
        username { $('#inputUsername') } /* Type: EditText ,clickable  */
        email { $('#inputEmail') } /* Type: EditText ,clickable  */
        password { $('#inputPassword') } /* Type: EditText ,clickable  */
        name { $('#inputName') } /* Type: EditText ,clickable  */
        preferedProgrammingLanguage { $('#input_preferedProgrammingLanguage') } /* Type: Spinner ,clickable  */
        adds { $('#input_adds') } /* Type: CheckBox ,clickable  */
        registerUserButton { $('#btnRegisterUser') } /* Type: Button ,clickable  */
    }
}
