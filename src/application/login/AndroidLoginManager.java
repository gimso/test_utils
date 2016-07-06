package application.login;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import application.appium.AppiumManager;
import application.config.ConfigurationManager;
import application.config.ui.AndroidGeneralProperties;
import application.dbManager.JDBCUtil;

/**
 * @author Tamir
 */
public class AndroidLoginManager extends LoginManager{

	public static LoginManager getInstance(){
		if(mLoginManager == null)
			mLoginManager = new AndroidLoginManager();
		return mLoginManager;
	}

	public AndroidLoginManager(){}
	
	/**
	 * Login to Simgo cloud
	 * @param country : User's country
	 * @param phoneNumber : User's phone number
	 * @param manulVerificationCode : if false full login using SMS verification will be applied otherwise manual entered
	 * @return "Passed" if success or corresponded message within the Error alert window if fails
	 */
	@Override
	public String login(String country,String phoneNumber,boolean manulVerificationCode){
		try{
			AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_PHONE_NUMBER_TEXT_BOX)).click();
			AppiumManager.getAppiumManager().getDriver().getKeyboard().sendKeys(ConfigurationManager.getFixedPhoneNumber(phoneNumber)); //same as element.sendKeys but should be faster
			AppiumManager.getAppiumManager().getDriver().hideKeyboard();
			//ApplicationDriver.getApplicationDriver().getDriver().navigate().back(); //close keyboard
		if(manulVerificationCode){
			String code = JDBCUtil.getDbManager().getUserPassword(phoneNumber);
			enterVerificationCode(code);
		}
		else if(findCountry(country)){
			clickLoginButton();
		}
		else
			return AndroidGeneralProperties.TXT_COUNTRY_NOT_FOUND_ERROR;
		
		AppiumManager.getAppiumManager().getWebDriverWaitForAction().
				until(ExpectedConditions.visibilityOf(AppiumManager.getAppiumManager().getDriver().
						findElement(By.id(AndroidGeneralProperties.ID_HELLO_USER_TRIP_TITLE))));
		return ConfigurationManager.TXT_PASSED;
		}catch(NoSuchElementException e){
			System.err.println("Error in Login Method Msg: " + e.getMessage());
			if(manulVerificationCode && phoneNumber.isEmpty() || 
					phoneNumber.length() < AndroidGeneralProperties.DeviceIDMinLenght)
					return AndroidGeneralProperties.TXT_LOGIN_SHORT_PHONE_NUMBER;
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}
	
	/**
	 * Finds login button and click on it.
	 */
	public void clickLoginButton(){
		AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_LOGIN_BUTTON)).click();
		WebElement bth_verify;
		AppiumManager.getAppiumManager().getWebDriverWaitForAction().until(
				ExpectedConditions.elementToBeClickable((bth_verify = AppiumManager.getAppiumManager().getDriver().
																findElement(By.id(AndroidGeneralProperties.ID_BUTTON_OK)))));
		bth_verify.click();
	}
	
	
	/**
	 * Find country within ListView
	 * @param country
	 * @return 
	 */
	public boolean findCountry(String country){
		AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_COUNTRIES_LIST_BUTTON)).click();
		boolean endOfList = false;
		TouchAction tAction=new TouchAction(AppiumManager.getAppiumManager().getDriver());
		String lastCountry = "";
		List<WebElement> webObjectList;
		WebElement firstCountryInList  , lastCountryInList;
		int listSize = 0;
		while(!endOfList){
			//get current visible country list
			webObjectList = AppiumManager.getAppiumManager().getDriver().findElements(By.xpath(AndroidGeneralProperties.ID_CURRENT_COUNTRIES_LIST));
			firstCountryInList = webObjectList.get(0);
			listSize = webObjectList.size() - 1;
			lastCountryInList = webObjectList.get(listSize);
			//if we are at the end of the list
			if(lastCountry.equals(firstCountryInList.getText()) || lastCountry.equals(lastCountryInList.getText())){
				endOfList = true;
				break;
			}
			//check whether the country is in the visible list
			else if(country.compareTo(firstCountryInList.getText()) >= 0 && country.compareTo(lastCountryInList.getText()) <= 0){
				for (WebElement webElement : webObjectList) {
					if(webElement.getText().equals(country)){
						webElement.click();
						return true;
					}
				}
			}
			//scroll list up
			else if(country.compareTo(firstCountryInList.getText()) < 0){
				lastCountry = webObjectList.get(1).getText();	//2 from the top
				tAction.press(firstCountryInList).moveTo(lastCountryInList).perform();
			}
			//scroll list down
			else{
				lastCountry = webObjectList.get(listSize - 2).getText(); 		//2nd form the bottom
				tAction.press(lastCountryInList).moveTo(firstCountryInList).perform();
			}
		}
			return false;
	}
	
	/**
	 * Type verification code manually
	 * @param verificationCode
	 */
	public void enterVerificationCode(String verificationCode){
		AppiumDriver dr = AppiumManager.getAppiumManager().getDriver();
		dr.findElement(MobileBy.AccessibilityId(AndroidGeneralProperties.TXT_MENU_BAR)).click();
		dr.findElement(By.name(AndroidGeneralProperties.TXT_ENTER_VERIFICATION_CODE)).click();
		dr.findElement(By.id(AndroidGeneralProperties.ID_VERIFICATION_CODE_TERXTBOX)).click();
		if(verificationCode != null && !verificationCode.isEmpty()){
			dr.getKeyboard().sendKeys(verificationCode); //same as element.sendKeys but should be faster
			AppiumManager.getAppiumManager().getDriver().hideKeyboard();
			AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_BUTTON_OK)).click();
		}
		else{
			AppiumManager.getAppiumManager().getDriver().navigate().back();
			clickLoginButton();
		}
	}
	
	/**
	 * Log out procedure
	 * @return "Passed" if succeed or corresponded message within the Error alert window
	 */
	public String logout(){
		AppiumDriver dr = AppiumManager.getAppiumManager().getDriver();
		try {
			dr.findElement(MobileBy.AccessibilityId(AndroidGeneralProperties.TXT_MENU_OPTIONS)).click();
			dr.findElement(By.name(AndroidGeneralProperties.TXT_LOG_OUT)).click();
			dr.findElement(By.id(AndroidGeneralProperties.ID_BUTTON_OK)).click();
			AppiumManager.getAppiumManager().getWebDriverWaitForAction()
					.until(ExpectedConditions.visibilityOf(AppiumManager.getAppiumManager().getDriver()
							.findElement(By.id(AndroidGeneralProperties.ID_PHONE_NUMBER_TEXT_BOX))));
			return ConfigurationManager.TXT_PASSED;
		} catch (NoSuchElementException e) {
			System.err.println("Error in Login Method Msg: " + e.getMessage());
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}
	
	//// General \\\\
	
	/**
	 * show contact tech support alert window
	 */
	public String contactTechSupport(){
		try{
			AppiumDriver dr = AppiumManager.getAppiumManager().getDriver();
			dr.findElement(MobileBy.AccessibilityId(AndroidGeneralProperties.TXT_MENU_BAR)).click();
			dr.findElement(By.name(AndroidGeneralProperties.TXT_CONTACT_TECH_SUPPORT)).click();
			AppiumManager.getAppiumManager().getWebDriverWaitForAction()
			.until(ExpectedConditions.visibilityOf(dr.findElement(By.id(AndroidGeneralProperties.ID_ALERT_TITLE))));
			AppiumManager.getAppiumManager().getWebDriverWaitForAction()
			.until(ExpectedConditions.visibilityOf(dr.findElement(By.name(AndroidGeneralProperties.TXT_SIMGO_SUPPORT))));
			AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_BUTTON_OK)).click();
		return ConfigurationManager.TXT_PASSED;
		} catch (NoSuchElementException e) {
			System.err.println("Error in Login Method Msg: " + e.getMessage());
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}
	
	/**
	 * show FAQ
	 */
	public String showFAQ(){
		try{
			AppiumDriver dr = AppiumManager.getAppiumManager().getDriver();
			dr.findElement(MobileBy.AccessibilityId(AndroidGeneralProperties.TXT_MENU_BAR)).click();
			dr.findElement(By.name(AndroidGeneralProperties.TXT_FAQ)).click();
			AppiumManager.getAppiumManager().getWebDriverWaitForAction()
			.until(ExpectedConditions.visibilityOf(dr.findElement(By.id(AndroidGeneralProperties.ID_EXPANDED_FAQ))));
			dr.navigate().back();
		return ConfigurationManager.TXT_PASSED;
		} catch (NoSuchElementException e) {
			System.err.println("Error in Login Method Msg: " + e.getMessage());
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}

}
