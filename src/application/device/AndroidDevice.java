package application.device;

import application.appium.AppiumManager;
import application.config.ConfigurationManager;
import application.config.ConfigurationManager.ApplicationUIMessages;
import application.config.ConfigurationManager.CancelTripStages;
import application.config.ui.AndroidGeneralProperties;
import application.login.AndroidLoginManager;
import application.trip.AndroidTripManager;

/**
 * @author Tamir
 */
public class AndroidDevice extends MobileDevice{
	
	private String mApiLevel;
	private AndroidTripManager mAndroidTripManager;

	/**
	 * 
	 * @param OS 		: Android or IOS
	 * @param model 	: Android model
	 * @param version 	: Android Version (4.2,5.1 etc...)
	 * @param udid 		: Device ID, if tested on emulator leave empty
	 */
	public AndroidDevice(String os,String model,String version,String udid){
		super(os,model,version,udid);
		//get API Level from Properties by android version, it includes 'V' before because properties keys set
		mApiLevel = ConfigurationManager.get("V" + version); 	
		AppiumManager.getAppiumManager().configureMobileDeviceForDriver(this);
		mAndroidTripManager = new AndroidTripManager();
		
	}
	
	@Override
	public String login(String country, String phoneNumber,boolean manulVerificationCode) {
		return AndroidLoginManager.getInstance().login(country, phoneNumber,manulVerificationCode);
	}
	
	@Override
	public String logOut() {
		return AndroidLoginManager.getInstance().logout();
	}

	@Override
	public String createTrip(String deviceId) {
		return mAndroidTripManager.createTrip(deviceId);
	}

	@Override
	public String cancelTrip(CancelTripStages stageNumber) {
		return mAndroidTripManager.cancelTrip(stageNumber);
	}


	@Override
	public String toString() {
		return super.toString() + " AndroidDevice [_apiLevel=" + mApiLevel + "]";
	}


	@Override
	public void resetApp() {
		AppiumManager.getAppiumManager().resetApp();
		
	}

	@Override
	public String enableCallForwarding() {
		return mAndroidTripManager.enableCallForwarding();
	}


	@Override
	public String getApplicationUIMessage(ApplicationUIMessages result) {
		switch(result){
		case SHORT_PHONE_NUMBER:
			return AndroidGeneralProperties.TXT_LOGIN_SHORT_PHONE_NUMBER;
		case USER_NOT_FOUND:
			return AndroidGeneralProperties.TXT_ERROR_USER_NOT_FOUND;
		case DEVICE_ID_NOT_FOUND:
			return AndroidGeneralProperties.TXT_TRIP_DEVICE_ID_ERROR_MESSAGE;
		case SHORT_DEVICE_ID:
			return AndroidGeneralProperties.TXT_TRIP__SHORT_DEVICE_ID;
		default:
			return super.getApplicationUIMessage(result);
		}
	}


	
	
	
	
	
	
	
	
}
