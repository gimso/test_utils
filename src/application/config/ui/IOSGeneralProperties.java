package application.config.ui;

/**
 * This class keeps UI ID properties and Texts Some of them might be the same
 * Path because in IOS it's per screen
 * 
 * @author Tamir
 *
 */

public class IOSGeneralProperties {
	
	public static final int DeviceIDMinLenght  = 6;
	public static final int MIN_PHONE_NUMBER_LENGTH  = 6;
	
	
	public static final String NAME_BUTTON_NEXT 						= 				"Next";
	public static final String NAME_BUTTON_DONE 						= 				"Done";
	public static final String NAME_BUTTON_EDIT 						= 				"Edit";
	public static final String NAME_BUTTON_CONFIRM 						= 				"Confirm";
	public static final String NAME_BUTTON_OK 							= 				"OK";
	public static final String NAME_BUTTON_CLOSE 						= 				"Close";
	public static final String NAME_BUTTON_REPLY 						= 				"Reply";
	public static final String NAME_BUTTON_START 						= 				"Start";
	public static final String NAME_BUTTON_END_TRIP_STAGE_ONE 			= 				"End Trip";
	public static final String NAME_BUTTON_END_TRIP_STAGE_TWO 			= 				"End trip";
	public static final String NAME_BUTTON_YES		 					= 				"Yes";
	public static final String NAME_BUTTON_NO		 					= 				"No";
	public static final String NAME_BUTTON_BACK							=				"back icon";
	public static final String NAME_SAFE_TRIP							=				"Have a safe trip!";
	public static final String NAME_BUTTON_CF							=				"Button";
	
	
	public static final String ATTRIBUTE_VALUE 							=				"value";
	public static final String ATTRIBUTE_NAME 							=				"name";
	
	public static final String VALUE_PHONE_NUMBER_TEXT_FIELD 			=				"Your Phone Number";
	public static final String VALUE_VERIFICATION_CODE_TEXT_FIELD 		=				"Enter Verification Code";
	public static final String TXT_UIBUTTON_FORWAORD_CALLS 				= 				"Forward My Calls";
	
	public static final String TXT_START_TRIP							=				"Start Trip";
	
	public static final String CLASS_TEXT_FILED 						= 				"UIATextField";
	public static final String CLASS_STATIC_TEXT 						= 				"UIAStaticText";
	public static final String CLASS_ALERT_WINDOW 						= 				"UIAAlert";
	public static final String CLASS_ALERT_WINDOW_SCROLL_VIEW 			= 				"UIAScrollView";
	public static final String CLASS_UIBUTTON				 			= 				"UIAButton";


	
	public static final String ERROR_MESSAGE_SMS_NOT_RECEIVED 			= 				"SMS Not Received";
	public static final String ERROR_MESSAGE_WRONG_SMS 					= 				"SMS is wrong";
	public static final String ERROR_MESSAGE_SHORT_NUMBER 				= 				"Please enter valid phone number";
	public static final String ERROR_MESSAGE_SHORT_ID	 				= 				"Please enter valid Simgo device ID";
	public static final String ERROR_MESSAGE_USER_NOT_EXIST	 			= 				"Your user account is no longer exist";
	public static final String ERROR_MESSAGE_VERIFICATION_CODE	 		= 				"Verification code mismatch";
	public static final String ERROR_MESSAGE_USER_NOT_FOUND 			= 				"We can't find your number in our records. "
																						+ "Simgo is available for registered users only and requires a special protective cover."
																						+ " Visit our website (www.simgo-mobile.com) or contact us at info@simgo-mobile.com to check availability in your country.";
	

	
	public static final String XPATH_BUTTON_CONFIRM 					= 				"//UIAApplication[1]/UIAWindow[5]/UIAAlert[1]/UIACollectionView[1]/UIACollectionCell[2]/UIAButton[1]";
	public static final String XPATH_BUTTON_EDIT 						= 				"//UIAApplication[1]/UIAWindow[5]/UIAAlert[1]/UIACollectionView[1]/UIACollectionCell[1]/UIAButton[1]";
	public static final String XPATH_VERIFICATION_CODE_TEXTBOX 			= 				"//UIAApplication[1]/UIAWindow[1]/UIATextField[1]";
	public static final String XPATH_START_TRIP_TITLE_TEXT 				= 				"//UIAApplication[1]/UIAWindow[1]/UIATextField[1]";
	public static final String XPATH_DEVICE_ID_TEXTBOX 					= 				"//UIAApplication[1]/UIAWindow[2]/UIATextField[1]";

}