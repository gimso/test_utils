package application.device;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import application.appium.AppiumManager;
import application.config.ConfigurationManager;
import application.config.ConfigurationManager.ApplicationUIMessages;
import application.config.ConfigurationManager.CancelTripStages;

/**
 * @author Tamir
 */
public abstract class MobileDevice {
	
	private ConfigurationManager.MobilePlatform mOS;
	private String mFullPlatformVersion;
	private String mVersion;
	private String mModel;
	private String mDeviceUDID;
	private ConfigurationManager.DeviceType mDeviceTye;
	
	/**
	 * Function sets settings for device
	 
	 * @param MobilePlatform 		: Android / IOS
	 * @param model  
	 * @param version 	: OS Version (Android : 5.11,6..,  IOS : 8.4,9.1...)
	 * @param udid 	  	: Device ID leave empty if the type is Emulator
	 */
	public  MobileDevice(String os,String model,String version,String udid){
		setDeviceSettings(os,model,version,udid);
		
	}
	
	private void setDeviceSettings(String os,String model,String version,String udid){
		setOS(os);
		mVersion = version;
		setModel(model);
		setFullPlatformVersion(mOS,mVersion);
		setDeviceUDID(udid);
	}
	

	public ConfigurationManager.MobilePlatform getOS() {
		return mOS;
	}

	public void setOS(String os) {
		this.mOS = ConfigurationManager.MobilePlatform.valueOf(os);
	}

	public String getfullPlatformVersion() {
		return mFullPlatformVersion;
	}
	

	public void setFullPlatformVersion(ConfigurationManager.MobilePlatform platform,String osVersion) {
		this.mFullPlatformVersion = ConfigurationManager.getFullPlatformVersion(platform, osVersion);
	}

	public String getModel() {
		return mModel;
	}

	public void setModel(String model) {
		this.mModel = ConfigurationManager.get(model);
	}

	public String getDeviceUDID() {
		return mDeviceUDID;
	}

	public void setDeviceUDID(String udid){
		mDeviceUDID = udid;
		if(udid != null && !udid.isEmpty())
			setDeviceTye(ConfigurationManager.DeviceType.REAL.toString());
		else
			setDeviceTye(ConfigurationManager.DeviceType.EMULATOR.toString());
	}

	public ConfigurationManager.DeviceType getDeviceTye() {
		return mDeviceTye;
	}


	public void setDeviceTye(String deviceTye) {
		this.mDeviceTye = ConfigurationManager.DeviceType.valueOf(deviceTye);
	}
	

	

	@Override
	public String toString() {
		return "MobileDevice [mOS=" + mOS + ", mFullPlatformVersion="
				+ mFullPlatformVersion + ", mVersion=" + mVersion + ", mModel="
				+ mModel + ", mDeviceUDID=" + mDeviceUDID + ", mDeviceTye="
				+ mDeviceTye + "]";
	}

	
	/**
	 * Login to Simgo cloud
	 * @param country : User's country
	 * @param phoneNumber : User's phone number
	 * @param manulVerificationCode : if false full login using SMS verification will be applied otherwise manual entered
	 * @return corresponding message  whether succeed or not
	 */
	public abstract String login(String country,String phoneNumber,boolean manulVerificationCode);
	
	/**
	 * log out from Simgo
	 * @return Relevant Message
	 */
	public abstract String logOut();
	
	
	/**
	 * function creates trip on Simgo cloud
	 * @param deviceId
	 * @return true whether trip created or false if not
	 */
	public abstract String createTrip(String deviceId);
	
	/**
	 * Reset The application
	 */
	public abstract void resetApp();
	
	/**
	 * function cancel Trip
	 * @param isFirstStage - indicates whether the trip is canceled on the first stage or later
	 * on Stage 2 and 3 there is another screen which needs to be found prior the deviceId text box. 
	 * @return Relevant message
	 */
	public abstract String cancelTrip(CancelTripStages stageNumber);
	
	
	/**
	 * method enables call forwarding
	 * @return Relevant message
	 */
	public abstract String enableCallForwarding();
	
	

	/**
	 * Function takes screen shot
	 */
	public void takeScreenShot(String fileName){
		File screenShot = AppiumManager.getAppiumManager().takeScreenShot();
		try {
			FileUtils.copyFile(screenShot, new File(ConfigurationManager.ScreenshotsFolder + fileName + ".jpg"));
		} catch (IOException e) {
			System.out.println("Could Not Take Screenshot, Appium is down");
		}
	}
	
	
	/**
	 * 
	 * @param result - the result from test step
	 * @return corresponded UI application message
	 */
	public String getApplicationUIMessage(ApplicationUIMessages result){
		switch(result){
		case PASSED:
			return ConfigurationManager.TXT_PASSED;
		case FAILED:
			return ConfigurationManager.TXT_FAILED;
		default:
			return "";
		}
	}
}
