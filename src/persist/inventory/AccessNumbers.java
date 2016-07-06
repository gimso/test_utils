package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

/**
 * This class is Added Modified and Delete access number in access number page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class AccessNumbers {
	private static final String ID_NUMBER = "id_number";
	private static final String ID_GEOLOCATION = "id_geolocation";
	private static final String ID_NUMBER_OF_CHANNELS = "id_number_of_channels";
	private static final String ID_ALLOWED = "id_allowed";
	private static final String ID_GROUP = "id_group";
	private static final String ID_TYPE = "id_type";
	private static final String ID_PHONE_NUMBER_FORMAT = "id_phone_number_format";
	private static final String ID_PHONE_NUMBER_TYPE = "id_phone_number_type";
	private static final String LINK_TEXT_ADD_ACCESS_NUMBER = "Add access number";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public AccessNumbers() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add access number with all parameters available
	 * 
	 * @param number
	 * @param geolocation
	 * @param numberOfChannels
	 * @param allowed
	 * @param group
	 * @param type
	 * @param phoneNumberFormat
	 * @param phoneNumberType
	 * @return String result message
	 * @
	 */
	public String add(String number, String geolocation,
			String numberOfChannels, Boolean allowed, String group,
			String type, String phoneNumberFormat, String phoneNumberType)
			 {
		// go to access number page
		select.selectInventoryAccessNumbers();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_ACCESS_NUMBER)).click();

		// insert Number
		if (number != null) {
			driver.findElement(By.id(ID_NUMBER)).clear();
			driver.findElement(By.id(ID_NUMBER)).sendKeys(number);
		}

		// select Geolocation
		if (geolocation != null) {
			persistUtil.selectByVisibleText(ID_GEOLOCATION, geolocation);
		}

		// insert Number of channels
		if (numberOfChannels != null) {
			driver.findElement(By.id(ID_NUMBER_OF_CHANNELS)).clear();
			driver.findElement(By.id(ID_NUMBER_OF_CHANNELS)).sendKeys(
					numberOfChannels);
		}

		// is Allowed
		if (allowed != null && !allowed) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		if (group != null) {
			// select Group
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// select Type
		if (type != null) {
			persistUtil.selectByVisibleText(ID_TYPE, type);
		}

		// select Phone number format
		if (phoneNumberFormat != null) {
			persistUtil.selectByVisibleText(ID_PHONE_NUMBER_FORMAT,
					phoneNumberFormat);
		}

		// select Phone number type
		if (phoneNumberType != null) {
			persistUtil.selectByVisibleText(ID_PHONE_NUMBER_TYPE,
					phoneNumberType);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add access number with the requires parameters only, the rest will fill
	 * with default values
	 * 
	 * @param number
	 * @param geolocation
	 * @param group
	 * @return String result message
	 * @
	 */
	public String add(String number, String geolocation, String group)
			 {
		return add(number, geolocation, null, true, group, null, null, null);
	}

	/**
	 * Modify and update the access number details by access number id
	 * 
	 * @param id
	 * @param number
	 * @param geolocation
	 * @param numberOfChannels
	 * @param allowed
	 * @param group
	 * @param type
	 * @param phoneNumberFormat
	 * @param phoneNumberType
	 * @return String result message
	 * @
	 */
	public String modifyById(String id, String number, String geolocation,
			String numberOfChannels, Boolean allowed, String group,
			String type, String phoneNumberFormat, String phoneNumberType)
			 {
		// go to access number page
		select.selectInventoryAccessNumbers();

		// find the access number to modify and click on it
		driver.findElement(By.linkText(id)).click();

		// insert Number
		if (number != null) {
			driver.findElement(By.id(ID_NUMBER)).clear();
			driver.findElement(By.id(ID_NUMBER)).sendKeys(number);
		}

		// select Geolocation
		if (geolocation != null) {
			persistUtil.selectByVisibleText(ID_GEOLOCATION, geolocation);
		}

		// insert Number of channels
		if (numberOfChannels != null) {
			driver.findElement(By.id(ID_NUMBER_OF_CHANNELS)).clear();
			driver.findElement(By.id(ID_NUMBER_OF_CHANNELS)).sendKeys(
					numberOfChannels);
		}

		// is Allowed
		if (allowed != null ) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// select Group
		if (group != null) {
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// select Type
		if (type != null) {
			persistUtil.selectByVisibleText(ID_TYPE, type);
		}

		// select Phone number format
		if (phoneNumberFormat != null) {
			persistUtil.selectByVisibleText(ID_PHONE_NUMBER_FORMAT,
					phoneNumberFormat);
		}

		// select Phone number type
		if (phoneNumberType != null) {
			persistUtil.selectByVisibleText(ID_PHONE_NUMBER_TYPE,
					phoneNumberType);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	
	public String modifyByNumber(String number, String newNumber, String geolocation,
			String numberOfChannels, Boolean allowed, String group,
			String type, String phoneNumberFormat, String phoneNumberType)
			 {
		// go to access number page
		select.selectInventoryAccessNumbers();

		// find the access number to modify and click on it
		getByNumberAndClick(number);

		// insert Number
		if (newNumber != null) {
			driver.findElement(By.id(ID_NUMBER)).clear();
			driver.findElement(By.id(ID_NUMBER)).sendKeys(newNumber);
		}

		// select Geolocation
		if (geolocation != null) {
			persistUtil.selectByVisibleText(ID_GEOLOCATION, geolocation);
		}

		// insert Number of channels
		if (numberOfChannels != null) {
			driver.findElement(By.id(ID_NUMBER_OF_CHANNELS)).clear();
			driver.findElement(By.id(ID_NUMBER_OF_CHANNELS)).sendKeys(
					numberOfChannels);
		}

		// is Allowed
		if (allowed != null ) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// select Group
		if (group != null) {
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// select Type
		if (type != null) {
			persistUtil.selectByVisibleText(ID_TYPE, type);
		}

		// select Phone number format
		if (phoneNumberFormat != null) {
			persistUtil.selectByVisibleText(ID_PHONE_NUMBER_FORMAT,
					phoneNumberFormat);
		}

		// select Phone number type
		if (phoneNumberType != null) {
			persistUtil.selectByVisibleText(ID_PHONE_NUMBER_TYPE,
					phoneNumberType);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	/**
	 * Delete access number by access number id
	 * 
	 * @param id
	 * @return String result message
	 * @
	 */
	public String deleteById(String id)  {
		// go to access number page
		select.selectInventoryAccessNumbers();

		// delete
		persistUtil.deleteByLinkTextName(id);

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	public String deleteByNumber(String accessNumber)  {
		select.selectInventoryAccessNumbers();

		// delete
		getByNumberAndClick(accessNumber);
		persistUtil.deleteSelectedItemsFromElementPage();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args)  {
		AccessNumbers accessNumbers = new AccessNumbers();
		String number = "97239606666";
		String modifyByNumber = accessNumbers.modifyByNumber(number, null, null, null, null, null, null, null, null);
		String deleteByNumber = accessNumbers.deleteByNumber(number);
		System.out.println(modifyByNumber+"\n"+deleteByNumber);
		// unitTesting();
	}

	public static void unitTesting() {

		AccessNumbers accessNumbers = new AccessNumbers();

		String number = "97230000000";
		String geolocation = "Israel (425)";
		String numberOfChannels = "1";
		Boolean allowed = false;
		String group = "Unassigned";
		String type = "Incoming";
		String phoneNumberFormat = "Local";
		String phoneNumberType = "Fixed Line";
		String id = "42";

		System.out.println(accessNumbers.add(number, geolocation, numberOfChannels, allowed, group, type,phoneNumberFormat, phoneNumberType));
		System.out.println(accessNumbers.add(number, geolocation, group));
		System.out.println(accessNumbers.modifyById(id, number, geolocation, numberOfChannels, allowed, group, type,phoneNumberFormat, phoneNumberType));
		System.out.println(accessNumbers.deleteById(id));

		accessNumbers.driver.close();
	}

	// -------- Special functions ---------//

	public void getByNumberAndClick(String accessNumber)  {
		
		persistUtil.searchForElement(accessNumber);

		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td/input[@value='"
						+ accessNumber + "']";
				String xpathClick = "//tr[" + i + "]/th/a";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					break;
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * Delete access number
	 */
	public String deleteAll() {
		select.selectInventoryAccessNumbers();
		return persistUtil.deleteAllFromTablePage();
	}
}
