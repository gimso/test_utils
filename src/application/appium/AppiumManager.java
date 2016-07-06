package application.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import application.config.ConfigurationManager;
import application.config.ConfigurationManager.MobilePlatform;
import application.config.ui.AndroidGeneralProperties;
import application.config.ui.IOSGeneralProperties;
import application.device.MobileDevice;
import application.helper.Helper;

/**
 * @author Tamir
 * The class is design as singleton pattern. it handles calls for Appium driver.
 * the driver is also a singleton instance.
 */
public class AppiumManager {

	private static final String TAG 												= 			"Appium Manager";
	
	private static AppiumManager appiumManager;

	
	private static final String key_Appium_Server_Settings_Address 					= 			"Server_Address";
	private static final String key_Appium_Server_Settings_Port 					= 			"Server_Port";
	private static final String key_Appium_Server_Settings_TimeOut 					= 			"Server_Timeout";
	private static final String key_Appium_Server_Settings_ImplicitWait 			= 			"Implicitly_Wait_TimeOut";
	private static final String key_Appium_Server_Settings_WebDriverTimeOut 		= 			"Web_Driver_Wait_TimeOut";
	private static final String key_Simgo_App_Location								= 			"default_application_location_";
	private static final String Key_Appium_Node_File			 					= 			"appium_node_file_";
	private static final String Key_AppiumJS_File				 					= 			"appium_appiumJS_file_";
	
	
	private ConfigurationManager.MobilePlatform mPlatform;
	
	
	//Server Capabilities
	private DesiredCapabilities mCapabilities;
	
	// Server Properties
	private String mFullAppPath;
	private String mDevice;
	private String mDeviceUDID;
	private String mAppiumServerAddress;						//Server Address
	private int mAppiumServerPort;							//Server port number
	private String mServerTimeOut;								//Server Time out
	private String mAppiumRemoteAccess;							//Server Remote Address
	private int mImplicitlyWaitTimeOut;							//Implicit timeout
	private int mWebDriverWaitTimeOut;							//explicit timeout
	private AppiumDriverLocalService mAppiumService;			//a service to start and stop Appium server
	private boolean mIsServerRunning;
	private AppiumDriver mAppiumDriver; 						// Appium Driver either IOS or Android
	private WebDriverWait mWaitForAction;						//Wait element
	private boolean isDriverConnected;

	private AppiumManager(){}
	
	
	public static AppiumManager getAppiumManager(){
		if(appiumManager == null)
			appiumManager = new AppiumManager();
		return appiumManager;
	}
	
	/**
	 * Function configures mobile settings for current driver
	 * @param device
	 */
	public void configureMobileDeviceForDriver(MobileDevice device){
		mDeviceUDID = device.getDeviceUDID();  			
		mPlatform = device.getOS(); 				
		mDevice = device.getDeviceTye().toString(); 	 
	}
	
	/**
	 * configure Appium Server Params
	 */
	public void configureDriverSetting (){
		mAppiumServerAddress = ConfigurationManager.get(key_Appium_Server_Settings_Address); // get Appium Server Address
		mAppiumServerPort = Integer.parseInt(ConfigurationManager.get(key_Appium_Server_Settings_Port));
		mAppiumRemoteAccess = "http://" + mAppiumServerAddress + ":" + mAppiumServerPort +"/wd/hub";
		String appLocationFromJenkins = System.getenv("APPLICATION_PATH");
		if(appLocationFromJenkins != null && !appLocationFromJenkins.isEmpty())
			mFullAppPath = System.getenv("APPLICATION_PATH"); 
		else{
			File fileParentPath = new File(System.getProperty("user.dir"));
			mFullAppPath = fileParentPath.getParent();
			mFullAppPath += ConfigurationManager.get(key_Simgo_App_Location + mPlatform);
		}
		
		System.out.println("path: " + mFullAppPath);
		mServerTimeOut = ConfigurationManager.get(key_Appium_Server_Settings_TimeOut);
		mImplicitlyWaitTimeOut = Integer.parseInt(ConfigurationManager.get(key_Appium_Server_Settings_ImplicitWait));
		mWebDriverWaitTimeOut = Integer.parseInt(ConfigurationManager.get(key_Appium_Server_Settings_WebDriverTimeOut));
	}
	
	
	/**
	 * set Appium Server settings
	 */
	private void setDesireCapabilities (){
		mCapabilities = new DesiredCapabilities();
		mCapabilities.setCapability(CapabilityType.PLATFORM, mPlatform);
		mCapabilities.setCapability(MobileCapabilityType.APP,mFullAppPath);// "/Users/nirberman/automation/Util/files/Simgo.ipa"); //new File("src",mFullAppPath).getAbsolutePAth());
		mCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, mServerTimeOut);
		mCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME,mDevice);
		if(mDeviceUDID != null && !mDeviceUDID.isEmpty())
			mCapabilities.setCapability(MobileCapabilityType.UDID,mDeviceUDID);
	}
	
	/**
	 * Create Appium Driver
	 */
	private void createDriver() {
		try{
		setDesireCapabilities();
		if (mPlatform.toString().equals(ConfigurationManager.MobilePlatform.Android.toString()))
			mAppiumDriver = new AndroidDriver(mAppiumService.getUrl(), mCapabilities); //(new URL(mAppiumRemoteAccess),mCapabilities);//
		else
			mAppiumDriver = new IOSDriver(mAppiumService.getUrl(), mCapabilities);//(new URL(mAppiumRemoteAccess),mCapabilities);//
		
		mAppiumDriver.manage().timeouts().implicitlyWait(mImplicitlyWaitTimeOut, TimeUnit.SECONDS);
		mWaitForAction = new WebDriverWait(mAppiumDriver, mWebDriverWaitTimeOut);
		isDriverConnected = true;
		}catch(Exception e){
			System.out.println("Check if Appium is up");
			System.err.println(this.getClass().getName() + "Method: getDriver " + e.getMessage());
			isDriverConnected = false;
		}
	}
	
	/**
	 * indicates whether driver is connected or not
	 * @return
	 */
	public boolean isDriverConnected(){
		return isDriverConnected;
	}
	
	public AppiumDriver getDriver(){
		if (mAppiumDriver == null || !isDriverConnected)
			createDriver();
		return mAppiumDriver;
	}
	
	public WebDriverWait getWebDriverWaitForAction(){
		if (mWaitForAction == null)
			createDriver();
			return mWaitForAction;
	}
	
	public void closeDriver(){
		mAppiumDriver.close();
	}
	
	/**
	 * fetch the message text from Error window
	 * @return corresponded message
	 */
	public String getAlertWindowMessage(){
		
		WebElement alertWindow, lognErrorMessage;
		
		if(getCurrentMobileDeviePlatform().equals(MobilePlatform.Android)){
			alertWindow = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_ALERT_TITLE));
			lognErrorMessage = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_MESSAGE));
			return lognErrorMessage.getText();
		}else{
			alertWindow = AppiumManager.getAppiumManager().getDriver().
					findElement(By.className(IOSGeneralProperties.CLASS_ALERT_WINDOW));
			return getTextFromUIAlertWindow(alertWindow);
		}
		
	}
	
	/**
	 * get the text from alert window
	 * might be more than 1 static text so we have to fetch all text from the window
	 * @param alertWindow
	 * @return text from alert Window
	 */
		public String getTextFromUIAlertWindow(WebElement alertWindow){
			StringBuilder textInAlertWindow = new StringBuilder();
			WebElement scrollView = alertWindow.findElement(By.className(IOSGeneralProperties.CLASS_ALERT_WINDOW_SCROLL_VIEW));
			List<WebElement> textView = scrollView.findElements(By.className(IOSGeneralProperties.CLASS_STATIC_TEXT));
			for(WebElement we : textView){
				textInAlertWindow.append(we.getAttribute("value") + " ");
			}
			return textInAlertWindow.toString().trim();
	}
	
	/**
	 * restart the application
	 */
	public void resetApp(){
		if(mAppiumDriver != null && isDriverConnected)
			mAppiumDriver.resetApp();
	}
	
	
	public ConfigurationManager.MobilePlatform getCurrentMobileDeviePlatform(){
		return mPlatform;
	}
	
	/**
	 * Take screen shot
	 * @return
	 */
	public File takeScreenShot() {
		return  ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
	}
	
	
	/**
	 * we use this method to replace searching by XPATH on IOS, 
	 * we get list of elements and we locate the specific one with attribute
	 * @param by Find element byClass, ByName etc..
	 * @param typeName type Of Class, UIButton, UITextField etc...
	 * @param attribute , Value, Name , Label, Id etc..
	 * * @param value - element attribute value
	 * @return found element, or null if there is no such element
	 */
	public WebElement findSpecificElement(String by, String typeName, String attribute, String value) throws ElementNotVisibleException{
			List<WebElement> elementsInScreen = null;
			switch (by) {
			case "ByClassName":
				elementsInScreen = getDriver().findElements(By.className(typeName));
				break;
			case "ByName":
				elementsInScreen = getDriver().findElements(By.name(typeName));
				break;
			default:
				break;
			}
			//if not null, we look for the element
			if(elementsInScreen != null){
				String str;
				for(WebElement el : elementsInScreen){
					if( (str = el.getAttribute(attribute)) != null &&
							str.equals(value))
						return el;
				}
			}
			//if list is null or we did not find our element within the list
			throw new NoSuchElementException("Element Not Found");
			}
	
	
	
	/**
	 * This method configures Appium server service
	 */
	private void configureAppiumService(){
		
		String os = Helper.getCurrentOS();
		if(os.equals(Helper.OS.Windows.toString()) || os.equals(Helper.OS.Mac.toString())){
			configureDriverSetting();
			
			//get Appium Node file and JS file path
			String appiumNodeFile = ConfigurationManager.get(Key_Appium_Node_File + os);
			String appiumJSFile = ConfigurationManager.get(Key_AppiumJS_File + os);
			
			
			mAppiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
		     .usingDriverExecutable(new File(appiumNodeFile))
		     .withAppiumJS(new File(appiumJSFile))
		     .withIPAddress(mAppiumServerAddress).usingPort(mAppiumServerPort));
		     //.withLogFile(new File(new File(classPathRoot, File.separator + "log"), "androidLog.txt"));
		}
		
	}
	
	/**
	 * Start Appium Server
	 */
	public void startAppiumServer(){
		if(mAppiumService == null)
			configureAppiumService();
		
		if(!mAppiumService.isRunning()){
			System.out.println(TAG + " Starting Appium Server....");
			mAppiumService.start();
			mIsServerRunning = true;
			
		}
		else{
			System.out.println(TAG + "Appium Server Is Already Running...");
		}
	}
	
	/**
	 * Stop Appium Server
	 */
	public void stopAppiumServer(){
		if(mAppiumService != null){
			if(mAppiumService.isRunning()){
				System.out.println(TAG + " Stopping Appium Server...");
				mAppiumDriver.quit();			//close the driver		
				mAppiumService.stop();			//close the server
				mIsServerRunning = false;
				System.out.println(TAG + "Appium Server Stopped");
			}
			else{
				System.out.println(TAG + "Appium Server has been stopped already");
			}
		}
	}
}