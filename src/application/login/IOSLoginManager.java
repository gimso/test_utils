package application.login;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import application.appium.AppiumManager;
import application.config.ConfigurationManager;
import application.config.ui.IOSGeneralProperties;
import application.dbManager.JDBCUtil;

/**
 * 
 * @author Tamir
 *
 */
public class IOSLoginManager extends LoginManager {
	
	public static LoginManager getInstance(){
		if (mLoginManager == null)
			mLoginManager = new IOSLoginManager();
		return mLoginManager;
	}

	public IOSLoginManager() {
	}

	/**
	 * Login to Simgo cloud
	 * 
	 * @param country
	 *            : User's country
	 * @param phoneNumber
	 *            : User's phone number
	 * @param verificationCode
	 *            :
	 * @return corresponding message whether succeed or not
	 */
	@Override
	public String login(String country, String phoneNumber,boolean manualVerification) {
		final String code;
		boolean smsReceived = false, moveToSMS = false;
		AppiumDriver driver = AppiumManager.getAppiumManager().getDriver();		//get the driver
		WebDriverWait wait = AppiumManager.getAppiumManager().getWebDriverWaitForAction();
		WebElement element;
		try{
			element = AppiumManager.getAppiumManager().findSpecificElement
					(By.ByClassName.class.getSimpleName(), IOSGeneralProperties.CLASS_TEXT_FILED, 
							IOSGeneralProperties.ATTRIBUTE_VALUE, IOSGeneralProperties.VALUE_PHONE_NUMBER_TEXT_FIELD);
			element.sendKeys(ConfigurationManager.getFixedPhoneNumber(phoneNumber));
			driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_NEXT)).click();
			driver.findElement(
					By.name(IOSGeneralProperties.NAME_BUTTON_CONFIRM)).click();
			//wait for device id text field to appear
			wait.until(ExpectedConditions.visibilityOf(driver.findElement
					(By.name(IOSGeneralProperties.NAME_BUTTON_DONE))));
			moveToSMS = true;
			//we fetch the verification code from DB
			code = JDBCUtil.getDbManager().getUserPassword(phoneNumber);
			//wait for SMS dialog to present
			wait.until(ExpectedConditions.visibilityOf(driver.findElement
					(By.className(IOSGeneralProperties.CLASS_ALERT_WINDOW_SCROLL_VIEW))));
			smsReceived = true;
			//fetch the SMS message
			String sms = AppiumManager.getAppiumManager().getAlertWindowMessage();
			//System.out.println("login: Verification Code received : " + code + " SMS : " + sms);
			if(!sms.contains(code))
				return IOSGeneralProperties.ERROR_MESSAGE_WRONG_SMS;
			driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_CLOSE)).click();
			driver.findElement(By.className(IOSGeneralProperties.CLASS_TEXT_FILED)).sendKeys(code);;
			driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_DONE)).click();
			wait.until(ExpectedConditions.visibilityOf(driver.findElement
					(By.name(IOSGeneralProperties.NAME_BUTTON_START))));
			return ConfigurationManager.TXT_PASSED;
		}catch(NoSuchElementException | TimeoutException e){
			if(phoneNumber.length() < IOSGeneralProperties.MIN_PHONE_NUMBER_LENGTH)
				return IOSGeneralProperties.ERROR_MESSAGE_SHORT_NUMBER;
			else if(!moveToSMS)
				return AppiumManager.getAppiumManager().getAlertWindowMessage();
			else if(!smsReceived)
				//SMS dialog has not been shown
				return IOSGeneralProperties.ERROR_MESSAGE_SMS_NOT_RECEIVED;
			else return ConfigurationManager.TXT_FAILED;
		}
	}

	@Override
	public void clickLoginButton() {
		
	}

	@Override
	public boolean findCountry(String country) {return false;}

	@Override
	public void enterVerificationCode(String verificationCode) {
		
	}

	@Override
	public String logout() {
		return null;
	}

}
