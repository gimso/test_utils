package application.trip;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import application.appium.AppiumManager;
import application.config.ConfigurationManager;
import application.config.ConfigurationManager.CancelTripStages;
import application.config.ui.AndroidGeneralProperties;

public class AndroidTripManager extends TripManager {

	
	private static final int DeviceIDMinLenght  = 4;
	
	/**
	 * @return an AndroidTripManager instance
	 */
	public static TripManager getInstance(){
		if(mTripManager == null)
			mTripManager = new AndroidTripManager();
		return mTripManager;
	}

	/**
	 * creates trip on Simgo Cloud using device ID
	 * @param deviceId
	 * @return "Passed" if success or corresponded message within the Error alert window if fails
	 */
	@Override
	public String createTrip(String deviceId){
		try{
			WebElement deviceIDBox = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_DEVICE_ID_TEXT_BOX)),
					startTripButton = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_START_TRIP_BUTTON));
			deviceIDBox.click();
			AppiumManager.getAppiumManager().getDriver().getKeyboard().sendKeys(deviceId);
			//ApplicationDriver.getApplicationDriver().getDriver().navigate().back(); //close keyboard
			AppiumManager.getAppiumManager().getDriver().hideKeyboard();

			AppiumManager.getAppiumManager().getWebDriverWaitForAction().
			until(ExpectedConditions.elementToBeClickable(startTripButton));
			startTripButton.click();
			AppiumManager.getAppiumManager().getWebDriverWaitForAction().
			until(ExpectedConditions.visibilityOf(AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_FORWARD_MY_CALLS_BUTTON))));
			return ConfigurationManager.TXT_PASSED;
		}catch(NoSuchElementException e){
			System.err.println("Error in createTrip Method MSG: " + e.getMessage());
			if(0 <= deviceId.length() && deviceId.length() <= DeviceIDMinLenght)
				return AndroidGeneralProperties.TXT_TRIP__SHORT_DEVICE_ID;
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}

	/**
	 * 
	 * @param isFirstStage - indicated whether we cancel the trip on 1st stage or later
	 * @return "Passed" if success or corresponded message within the Error alert window if fails
	 */
	@Override
	public String cancelTrip(CancelTripStages stageNumber){
		try{
			WebElement cancelTrip = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_CANCEL_TRIP_SWIPE));
			int timeToSwipe = 1000;
			int xStart = cancelTrip.getLocation().x + cancelTrip.getSize().getWidth(), yStart = cancelTrip.getLocation().y + cancelTrip.getSize().getHeight() / 2;
			AppiumManager.getAppiumManager().getDriver().swipe(xStart,yStart,1,yStart,timeToSwipe);
			AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_CANCEL_TRIP_YES_BUTTON)).click();
			switch(stageNumber){
			case CANCEL_TRIP_STAGE_ONE:
				AppiumManager.getAppiumManager().getWebDriverWaitForAction().until(ExpectedConditions.
						visibilityOf(AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_DEVICE_ID_TEXT_BOX))));
				break;
			case CANCEL_TRIP_STAGE_TWO:
				AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_BUTTON_OK)).click();
				return AndroidGeneralProperties.TXT_CALL_FORWARDING_CANCELED;
			case CANCEL_TRIP_STAGE_THREE:
				break;
			}
			return ConfigurationManager.TXT_PASSED;
		}catch(NoSuchElementException e){
			System.err.println("Error from cancelTrip msg:" + e.getMessage());
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}

	/**
	 * Enable call forwarding
	 * @return "Passed" if success or corresponded message within the Error alert window if fails
	 */
	@Override
	public String enableCallForwarding(){
		try{
			WebElement element = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_FORWARD_MY_CALLS_BUTTON));
			element.click();
			AppiumManager.getAppiumManager().getWebDriverWaitForAction().until(ExpectedConditions.
					visibilityOf((element = AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_MESSAGE)))));
			AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_BUTTON_OK)).click();
			if(AppiumManager.getAppiumManager().getDriver().findElement(By.id(AndroidGeneralProperties.ID_TRIP_TITLE_STEP_TWO)) != null){
				return ConfigurationManager.TXT_PASSED;
			}
			return element.getText();
		}catch(NoSuchElementException e){
			System.err.println("Error from enableCallForwarding msg:" + e.getMessage());
			return AppiumManager.getAppiumManager().getAlertWindowMessage();
		}
	}
}
