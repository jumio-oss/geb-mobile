package geb.mobile.android.instrumention.activities

import geb.mobile.android.AndroidBaseActivity

class VerifyUserActivity extends AndroidBaseActivity {

	static content = { 

		labelName { $('#label_name') } /* Type: TextView   */
		labelNameData { $('#label_name_data') } /* Type: TextView   */
		labelUsername { $('#label_username') } /* Type: TextView   */
		labelUsernameData { $('#label_username_data') } /* Type: TextView   */
		labelPassword { $('#label_password') } /* Type: TextView   */
		labelPasswordData { $('#label_password_data') } /* Type: TextView   */
		labelEmail { $('#label_email') } /* Type: TextView   */
		labelEmailData { $('#label_email_data') } /* Type: TextView   */
		labelPreferedProgrammingLanguage { $('#label_preferedProgrammingLanguage') } /* Type: TextView   */
		labelPreferedProgrammingLanguageData { $('#label_preferedProgrammingLanguage_data') } /* Type: TextView   */
		labelAcceptAdds { $('#label_acceptAdds') } /* Type: TextView   */
		labelAcceptAddsData { $('#label_acceptAdds_data') } /* Type: TextView   */
		registerUserButton { $('#buttonRegisterUser') } /* Type: Button ,clickable  */
	}
}
