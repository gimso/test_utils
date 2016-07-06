package application.trip;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import application.appium.AppiumManager;
import application.config.ConfigurationManager;
import application.config.ConfigurationManager.CancelTripStages;
import application.config.ui.IOSGeneralProperties;
import io.appium.java_client.AppiumDriver;

public class IOSTripManager extends TripManager{

		private static final int DeviceIDMinLenght  = 4;

		public static TripManager getInstance(){
			if(mTripManager == null)
				mTripManager = new IOSTripManager();
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
				WebElement element;
				AppiumDriver driver = AppiumManager.getAppiumManager().getDriver();
				WebDriverWait wait = AppiumManager.getAppiumManager().getWebDriverWaitForAction();
				element = driver.findElement(By.className(IOSGeneralProperties.CLASS_TEXT_FILED)); 
				element.click();
				element.sendKeys(deviceId);
				driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_START)).click();
				wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_END_TRIP_STAGE_ONE))));
				return ConfigurationManager.TXT_PASSED;
			}catch(NoSuchElementException | TimeoutException e){
				return AppiumManager.getAppiumManager().getAlertWindowMessage();
			}
		}

		/**
		 * 
		 * @param stageNumber - indicated the stage number we cancel the trip
		 * @return "Passed" if success or corresponded message within the Error alert window if fails
		 */
		@Override
		public String cancelTrip(CancelTripStages stageNumber){
			AppiumDriver driver = AppiumManager.getAppiumManager().getDriver();
			WebDriverWait wait = AppiumManager.getAppiumManager().getWebDriverWaitForAction();
			WebElement element;
			try{
			switch(stageNumber){
			case CANCEL_TRIP_STAGE_ONE:
				driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_END_TRIP_STAGE_ONE)).click();
				break;
			case CANCEL_TRIP_STAGE_TWO:
				driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_END_TRIP_STAGE_TWO)).click();
				wait.until(ExpectedConditions.visibilityOf(AppiumManager.getAppiumManager().findSpecificElement
						(By.ByClassName.class.getSimpleName(), IOSGeneralProperties.CLASS_UIBUTTON, 
								IOSGeneralProperties.ATTRIBUTE_NAME, IOSGeneralProperties.NAME_BUTTON_BACK)));
				handleCallForwardingDialog();
			case CANCEL_TRIP_STAGE_THREE:
				break;
			}
			wait.until(ExpectedConditions.visibilityOf(driver.findElement
					(By.name(IOSGeneralProperties.NAME_BUTTON_START))));
			return ConfigurationManager.TXT_PASSED;
			}catch(NoSuchElementException | TimeoutException e){
				return AppiumManager.getAppiumManager().getAlertWindowMessage();
			}
		}

		/**
		 * Enable call forwarding
		 * @return "Passed" if success or corresponded message within the Error alert window if fails
		 */
		@Override
		public String enableCallForwarding(){
			AppiumDriver driver = AppiumManager.getAppiumManager().getDriver();
			WebDriverWait wait = AppiumManager.getAppiumManager().getWebDriverWaitForAction();
			try{
				//Click Forward my calls button
			driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_CF)).click();
			handleCallForwardingDialog();
			wait.until(ExpectedConditions.visibilityOf(driver.findElement
					(By.name(IOSGeneralProperties.NAME_SAFE_TRIP))));
			return ConfigurationManager.TXT_PASSED;
			}catch(ElementNotVisibleException | TimeoutException e){
				return e.getMessage();
			}
		}

		/**
		 * Method handles Call forwarding on IOS
		 * @throws ElementNotFoundException
		 * @throws TimeoutException
		 */
		private void handleCallForwardingDialog() throws ElementNotFoundException, TimeoutException {
			AppiumDriver driver = AppiumManager.getAppiumManager().getDriver();
			WebDriverWait wait = AppiumManager.getAppiumManager().getWebDriverWaitForAction();
			driver.runAppInBackground(1); 	//we go to the background for 1 second to invoke the CF popup
			wait.until(ExpectedConditions.visibilityOf(driver.findElement
					(By.className(IOSGeneralProperties.CLASS_ALERT_WINDOW_SCROLL_VIEW))));
			driver.findElement(By.name(IOSGeneralProperties.NAME_BUTTON_YES)).click();
		}
}
