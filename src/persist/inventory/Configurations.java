package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;
/**
 * This class is  Modified Configuration in
 * usage/configuration page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Configurations {
	private static final String ID_NIGHT_TIME_END = "id_night_time_end";
	private static final String ID_NIGHT_TIME_START = "id_night_time_start";
	private static final String ID_HIGH_CHARGING_THRESHOLD = "id_high_charging_threshold";
	private static final String ID_LOW_CHARGING_THRESHOLD = "id_low_charging_threshold";
	private static final String ID_APNS_MODE = "id_apns_mode";
	private static final String ID_SUPPORT_WHATSAPP = "id_support_whatsapp";
	private static final String ID_SUPPORT_SKYPE = "id_support_skype";
	private static final String ID_SUPPORT_EMAIL = "id_support_email";
	private static final String ID_SUPPORT_PHONE_NUMBER = "id_support_phone_number";
	private static final String ID_DEFAULT_INTL_CARRIER_3 = "id_default_intl_carrier_3";
	private static final String ID_DEFAULT_INTL_CARRIER_2 = "id_default_intl_carrier_2";
	private static final String ID_DEFAULT_INTL_CARRIER_1 = "id_default_intl_carrier_1";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Configurations() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}
/**
 *  Modify and update the configuration details by id
 * @param id
 * @param defaultIntlCarrier1
 * @param defaultIntlCarrier2
 * @param defaultIntlCarrier3
 * @param supportPhoneNumber
 * @param supportEmail
 * @param supportSkype
 * @param supportWhatsapp
 * @param apnsMode
 * @param lowChargingThreshold
 * @param highChargingThreshold
 * @param nightTimeStart
 * @param nightTimeEnd
 * @return String result message
 * @
 */
	public String modifyById(String id, 
			String defaultIntlCarrier1, 
			String defaultIntlCarrier2,
			String defaultIntlCarrier3, 
			String supportPhoneNumber,
			String supportEmail, 
			String supportSkype, 
			String supportWhatsapp,
			String apnsMode,
			String lowChargingThreshold	,
			String highChargingThreshold,	
			String nightTimeStart,	
			String nightTimeEnd)
			 {
		// go to configuration page
		select.selectInventoryConfigurations();

		// find the configuration to modify and click on it
		driver.findElement(By.linkText(id)).click();

		// select Default intl carrier 1
		if ( defaultIntlCarrier1 != null) {
			persistUtil.selectByVisibleText(ID_DEFAULT_INTL_CARRIER_1, defaultIntlCarrier1);
		}
		
		// select Default intl carrier 2
		if (defaultIntlCarrier2  != null) {
			persistUtil.selectByVisibleText(ID_DEFAULT_INTL_CARRIER_2, defaultIntlCarrier2);
		}

		// select Default intl carrier 3
		if ( defaultIntlCarrier3 != null) {
			persistUtil.selectByVisibleText(ID_DEFAULT_INTL_CARRIER_3, defaultIntlCarrier3);
		}
		
		// insert Support phone number
		if (supportPhoneNumber  != null) {
			driver.findElement(By.id(ID_SUPPORT_PHONE_NUMBER)).clear();
			driver.findElement(By.id(ID_SUPPORT_PHONE_NUMBER)).sendKeys(supportPhoneNumber);
		}
		
		// insert Support email
		if ( supportEmail != null) {
			driver.findElement(By.id(ID_SUPPORT_EMAIL)).clear();
			driver.findElement(By.id(ID_SUPPORT_EMAIL)).sendKeys(supportEmail);
		}
		
		// insert Support skype
		if ( supportSkype != null) {
			driver.findElement(By.id(ID_SUPPORT_SKYPE)).clear();
			driver.findElement(By.id(ID_SUPPORT_SKYPE)).sendKeys(supportSkype);
		}
		
		// insert Support whatsapp
		if (supportWhatsapp  != null) {
			driver.findElement(By.id(ID_SUPPORT_WHATSAPP)).clear();
			driver.findElement(By.id(ID_SUPPORT_WHATSAPP)).sendKeys(supportWhatsapp);
		}
		
		// select APNS Mode
		if (apnsMode  != null) {
			persistUtil.selectByVisibleText(ID_APNS_MODE, apnsMode);
		}
		
		// Low charging threshold
		if (lowChargingThreshold  != null) {
			driver.findElement(By.id(ID_LOW_CHARGING_THRESHOLD)).clear();
			driver.findElement(By.id(ID_LOW_CHARGING_THRESHOLD)).sendKeys(lowChargingThreshold);
		}
		
		// High charging threshold
		if ( highChargingThreshold != null) {
			driver.findElement(By.id(ID_HIGH_CHARGING_THRESHOLD)).clear();
			driver.findElement(By.id(ID_HIGH_CHARGING_THRESHOLD)).sendKeys(highChargingThreshold);
		}
		
		// Night time start
		if ( nightTimeStart != null) {
			driver.findElement(By.id(ID_NIGHT_TIME_START)).clear();
			driver.findElement(By.id(ID_NIGHT_TIME_START)).sendKeys(nightTimeStart);
		}
		
		// Night time end
		if ( nightTimeEnd != null) {
			driver.findElement(By.id(ID_NIGHT_TIME_END)).clear();
			driver.findElement(By.id(ID_NIGHT_TIME_END)).sendKeys(nightTimeEnd);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}


	public static void main(String[] args) {
		unitTesting();
	}

	public static void unitTesting() {

		Configurations configurations = new Configurations();
		
		String id = "1";
		String defaultIntlCarrier1 = "---------";
		String defaultIntlCarrier2 = "---------";
		String defaultIntlCarrier3 = "---------";
		String supportPhoneNumber = "97231112222";
		String supportEmail = "support@test.com";
		String supportSkype = "supportSkype";
		String supportWhatsapp = "972501112222";
		String apnsMode = "Production";
		String lowChargingThreshold = "90";
		String highChargingThreshold = "65";
		String nightTimeEnd = "12";
		String nightTimeStart = "1";
		
		// MODIFY
		try {
			System.out.println(
					configurations.modifyById(id, defaultIntlCarrier1, defaultIntlCarrier2, defaultIntlCarrier3, supportPhoneNumber, supportEmail, supportSkype, supportWhatsapp, apnsMode, lowChargingThreshold, highChargingThreshold, nightTimeStart, nightTimeEnd));
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		

		configurations.driver.close();
	}
}
