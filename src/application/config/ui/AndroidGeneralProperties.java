package application.config.ui;

/**
 * This class keeps Android UI ID properties and Texts
 * @author Tamir
 */
public class AndroidGeneralProperties {
	
	public static final int DeviceIDMinLenght  = 6;
	public static final String TXT_SIMGO							= 			"Simgo";
		
	/// General
	public static final String ID_BUTTON_OK 						= 			"android:id/button1";
	public static final String ID_BUTTON_CANCEL 					= 			"android:id/button2";
	public static final String ID_BUTTON_HELP 						= 			"android:id/button3";
	public static final String ID_ALERT_TITLE 						= 			"android:id/alertTitle";
	public static final String ID_MESSAGE 							= 			"android:id/message";
	public static final String TXT_MENU_OPTIONS 					= 			"More options";
	public static final String TXT_MENU_HELP_TITLE 					= 			"Simgo Support";
	
	//Menu Bar
	public static final String TXT_MENU_BAR			 				= 			"More options";
	public static final String TXT_ENTER_VERIFICATION_CODE 			= 			"Enter Verification Code";
	public static final String TXT_CONTACT_TECH_SUPPORT 			= 			"Contact Tech Support";
	public static final String TXT_FAQ					 			= 			"FAQ";
	
	//General Functions
	public static final String TXT_SIMGO_SUPPORT		 			= 			"Simgo Support";
	public static final String ID_EXPANDED_FAQ			 			= 			"com.simgo.simgoapp:id/expandedFAQ";
	
	
	
	//Login
	public static final String ID_PHONE_NUMBER_TEXT_BOX 			= 			"com.simgo.simgoapp:id/editTextPhoneNumber";
	public static final String ID_LOGIN_BUTTON 						= 			"com.simgo.simgoapp:id/imageViewLogin";
	public static final String ID_COUNTRIES_LIST_BUTTON 			= 			"com.simgo.simgoapp:id/spinnerCountries";
	public static final String ID_CURRENT_COUNTRIES_LIST 			= 			"//android.widget.ListView[1]//android.widget.CheckedTextView";
	public static final String ID_VERIFICATION_CODE_TERXTBOX 		= 			"com.simgo.simgoapp:id/editTextVerificationCode";
	public static final String TXT_LOGIN_ERROR 						= 			"Login Error";
	public static final String TXT_COUNTRY_NOT_FOUND_ERROR 			= 			"Country could not be found";
	
	public static final String TXT_LOG_OUT 							= 			"Log Out";
	public static final String TXT_ERROR_VERIFICATION_FAILED 		= 			"Verification failed.?If problem persist, please contact tech support.";
	public static final String TXT_ERROR_INVALID_PHONE_NUMBER 		= 			"Invalid phone number";
	public static final String TXT_ERROR_EMPTY_PHONE_NUMBER 		= 			"Please enter your phone number first.";
	public static final String TXT_ERROR_USER_NOT_FOUND 			= 			"We can't find your number in our records. "
																			  + "Simgo is available for registered users only and requires a special protective cover. "
																			  + "Visit our website (www.simgo-mobile.com) or contact us at info@simgo-mobile.com to check availability in your country.";
	public static final String TXT_ERROR_CONNECTION_TO_SIMGO 		= 			"Please make sure Internet connection is available.";
	public static final String CLASS_IMAGE_VIEW_OPTION_LOGIN_SCREEN = 			"android.widget.ImageView";
	
	
	//Trip
	public static final String ID_HELLO_USER_TRIP_TITLE 			= 			"com.simgo.simgoapp:id/txtFrgStartTripHiThere";
	public static final String ID_DEVICE_ID_TEXT_BOX 				= 			"com.simgo.simgoapp:id/edtFrgStartTripPlugId";
	public static final String ID_START_TRIP_BUTTON 				= 			"com.simgo.simgoapp:id/imgFrgStartTripStartTrip";
	public static final String ID_FORWARD_MY_CALLS_BUTTON 			= 			"com.simgo.simgoapp:id/imageViewCallForwarding";
	public static final String ID_CANCEL_TRIP_SWIPE 				= 			"com.simgo.simgoapp:id/textViewSwipeToCancelTrip";
	public static final String ID_CANCEL_TRIP_YES_BUTTON 			= 			"com.simgo.simgoapp:id/imgFrgCancelTripYes";
	public static final String ID_CANCEL_TRIP_NO_BUTTON 			= 			"com.simgo.simgoapp:id/imgFrgCancelTripNo";
	public static final String ID_TRIP_TITLE_STEP_TWO 				= 			"com.simgo.simgoapp:id/textViewStepTwoStillAtHomeTitle";
	
	public static final String TXT_TRIP_ERROR_TITLE 				= 			"Trip Error";
	public static final String TXT_TRIP_DEVICE_ID_ERROR_MESSAGE 	= 			"Simgo device ID not found";
	public static final String TXT_TRIP__SHORT_DEVICE_ID 			= 			"DeviceID is too short";
	public static final String TXT_LOGIN_SHORT_PHONE_NUMBER 		= 			"Phone Number is too short";
	public static final String TXT_TRIP_ACCESS_NUMBER_ERROR 		= 			"Can't start trip. Local Access Number not availble.";
	public static final String TXT_CREATE_TRIP_ERROR 				= 			"Failed to start trip.?If problem persist, please contact tech support.";
	public static final String TXT_CALL_FORWARDING_SUCCESS 			= 			"Call forwarding?unconditionally.?Registration was successful.";
	public static final String TXT_CALL_FORWARDING_CANCELED 		= 			"Call forwarding?unconditionally.?Erasure was successful.";
	
	
	//Consumption
	public static final String ID_SETTINGS_BUTTON		 			= 			"com.simgo.simgoapp:id/action_notification_center";
	public static final String ID_CONSUMPTION_FRG		 			= 			"com.simgo.simgoapp:id/pagerFrgTileTmlpPager";
	public static final String ID_CONSUMPTION_TYPE		 			= 			"com.simgo.simgoapp:id/textViewType";
	public static final String ID_CONSUMPTION_USED		 			= 			"com.simgo.simgoapp:id/textViewUsed";
	public static final String ID_CONSUMPTION_LEFT		 			= 			"com.simgo.simgoapp:id/textViewLeft";
	public static final String TXT_CONSUMPTION_INTERNET 			= 			"Internet";
	public static final String TXT_CONSUMPTION_CALLS	 			= 			"Calls";
	
	
	//Chrome Browswer
	public static final String TXT_CHROME							= 			"Chrome";
	public static final String ID_CHROME_SEARCHING_BOX				= 			"com.android.chrome:id/search_box_text";
	public static final String ID_CHROME_URL_BAR					= 			"com.android.chrome:id/url_bar";
	public static final String CLASS_CHROME_IMAGE					= 			"android.widget.Image";
	public static final String TXT_CHROME_SAVE_FILE					= 			"Save image";
}
