package application.device;

import application.appium.AppiumManager;
import application.config.ConfigurationManager.ApplicationUIMessages;
import application.config.ConfigurationManager.CancelTripStages;
import application.config.ui.IOSGeneralProperties;
import application.login.IOSLoginManager;
import application.trip.IOSTripManager;

/**
 * @author Tamir
 */
public class IOSDevice extends MobileDevice{
	
	private IOSLoginManager mIOSLoginManager;
	private IOSTripManager mIOSTripManager;
	
	/**
	 * 
	 * @param OS 		: Android or IOS
	 * @param OS 	: Android or IOS
	 * @param model 	: iPhone model (iPhone 5s , iPhone 6...)
	 * @param version 	: Android Version (8.4,9.0,9.1...)
	 * @param udid 		: Device ID, if tested on emulator leave empty
	 */
	public IOSDevice(String os,String model,String version,String udid){
		super(os,model,version,udid);
		AppiumManager.getAppiumManager().configureMobileDeviceForDriver(this);
		mIOSLoginManager = new IOSLoginManager();
		mIOSTripManager = new IOSTripManager();
	}
	
	@Override
	public String login(String country, String phoneNumber,boolean manulVerificationCode) {
		return mIOSLoginManager.login(country, phoneNumber, manulVerificationCode);
	}
	
	@Override
	public String logOut() {
		
		return null;
	}

	@Override
	public String createTrip(String deviceId) {
		return mIOSTripManager.createTrip(deviceId);
	}

	@Override
	public String cancelTrip(CancelTripStages stageNumber) {
		return mIOSTripManager.cancelTrip(stageNumber);
	}

	@Override
	public String toString() {
		return super.toString();
	}


	@Override
	public void resetApp() {
		AppiumManager.getAppiumManager().resetApp();
	}

	@Override
	public String enableCallForwarding() {
		return mIOSTripManager.enableCallForwarding();
	}


	@Override
	public String getApplicationUIMessage(ApplicationUIMessages result) {
		switch(result){
		case SHORT_PHONE_NUMBER:
			return IOSGeneralProperties.ERROR_MESSAGE_SHORT_NUMBER;
		case USER_NOT_FOUND:
			return IOSGeneralProperties.ERROR_MESSAGE_USER_NOT_FOUND;
		case DEVICE_ID_NOT_FOUND:
			return IOSGeneralProperties.ERROR_MESSAGE_USER_NOT_EXIST;
		case SHORT_DEVICE_ID:
			return IOSGeneralProperties.ERROR_MESSAGE_SHORT_ID;
		default:
			return super.getApplicationUIMessage(result);
		}
	}
}
