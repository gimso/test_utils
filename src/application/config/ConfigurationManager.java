package application.config;

import java.util.HashMap;
import java.util.Properties;

import application.appium.AppiumManager;
import application.helper.Helper;

/**
 * @author Tamir
 */
public class ConfigurationManager {
	
	public static final String TXT_FAILED = "Failed";
	public static final String TXT_PASSED = "Passed";
	public static final String TXT_SKIPPED = "Skip";
	public static final String KEY_SIMGO_CLOUD = "Cloud";

	private static final String generalSettingsFile = "config.properties";
	public static final String ScreenshotsFolder = System.getProperty("user.dir") +"\\Screenshots\\";
	private static Properties generalProperties; // General Properties for Android and IOS
	
	
	public enum ApplicationUIMessages{
		PASSED, FAILED, SHORT_PHONE_NUMBER, USER_NOT_FOUND, DEVICE_ID_NOT_FOUND,
		SHORT_DEVICE_ID
	};
	
	public enum CancelTripStages{
		CANCEL_TRIP_STAGE_ONE(1), CANCEL_TRIP_STAGE_TWO(2),CANCEL_TRIP_STAGE_THREE(3);
		private int stage_value;
		
		public static HashMap<Integer, CancelTripStages> STAGES_VALUES = new HashMap<>();
		
		static {
	        for(int i=0;i<values().length;i++)
	        {
	        	STAGES_VALUES.put(values()[i].value(), values()[i]);
	        }
	    }

	    public static CancelTripStages getStage(int stageNum) {
	        return STAGES_VALUES.get(stageNum);
	    }

	    public int value() {
	    return stage_value;
	    }
		
		CancelTripStages(int value){
			this.stage_value = value;
		}
	}

	
	public static Properties getProperties(){
		if(generalProperties == null)
			generalProperties = Helper.loadProperties(generalSettingsFile);
		return generalProperties;
	}
	
	/**
	 * add corresponded symbol to number depending the cloud we work on. 
	 * @param original
	 * @return fixed number
	 */
	public static String getFixedPhoneNumber(String original) {
		String cloud = get(ConfigurationManager.KEY_SIMGO_CLOUD);
		StringBuilder fixedNumber = new StringBuilder();
		if (original != null && !original.isEmpty()) {
			switch (ConfigurationManager.QA_CLOUDS.valueOf(cloud.toUpperCase())) {
			case STAGING:
				if (AppiumManager.getAppiumManager().getCurrentMobileDeviePlatform().equals(MobilePlatform.Android))
					fixedNumber.append(".");
				else
					fixedNumber.append(",");
				return fixedNumber.append(original).toString();
			case QA:
				if (AppiumManager.getAppiumManager().getCurrentMobileDeviePlatform().equals(MobilePlatform.Android))
					fixedNumber.append("..");
				else
					fixedNumber.append(",,");
				return fixedNumber.append(original).toString();
			}
		}
		return original;
	}
	
	/**
	 * Functions loads General Properties from file to properties
	 */
	
	public static enum MobilePlatform{
		Android, iOS
	};
	
	public static enum DeviceType{
		REAL,EMULATOR
	};
	
	public static enum QA_CLOUDS{
		QA,STAGING
	}
	
	
	/**
	 * get key from java properties file
	 * @param key
	 * @return the value for given key
	 */
	public static String get(String key){
		if(!key.isEmpty() && getProperties().containsKey(key))
		if(getProperties().containsKey(key))
			return getProperties().getProperty(key);
		return "";
	}
	
	/**
	 * set Property in properties
	 * @param key
	 * @param value
	 */
	public void setProperty(String key,String value){
		if(getProperties().containsKey(key))
			getProperties().remove(key);
		
		 getProperties().setProperty(key, value);
	}
	
	
	/**
	 * Function gets the android version  and return full android platform version
	 * @param androidVersion For Example : (4.1 , 5.0 etc...)
	 * @return Full android platform version Example: 4.2 Jelly Bean (API Level 17)
	 */
	private static String getFullandroidPlatformVersion(String androidVersion){
		return  androidVersion +" " + ConfigurationManager.get(androidVersion) +
				" (API Level " + ConfigurationManager.get(androidVersion) +")";
	}
	
	
	private static String getFullIOSPlatformVersion(String osVersion ){
		return  MobilePlatform.iOS + " " + osVersion;
	}
	
	public static String getFullPlatformVersion(MobilePlatform platform,String version){
		switch(platform){
		case Android:
				return getFullandroidPlatformVersion(version);
		case iOS:
			 return getFullIOSPlatformVersion(version);
		default:
			return "";
		}
	}
}
