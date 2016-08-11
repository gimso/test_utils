package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

/**
 * This class is Added Modified and Delete apn in apn page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Apns {
	private static final String ID_APN_SMS_UPDATE = "id_apn_sms_update";
	private static final String ID_AUTHENTICATION_TYPE = "id_authentication_type";
	private static final String ID_PASSWORD = "id_password";
	private static final String ID_USERNAME = "id_username";
	private static final String ID_APN = "id_apn";
	private static final String LINK_TEXT_ADD_APN = "Add apn";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Apns() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add apn with all parameters available
	 * 
	 * @param name
	 * @param apn
	 * @param username
	 * @param password
	 * @param authenticationType
	 * @param apnSmsUpdate
	 * @return String result message
	 * @
	 */
	public String add(String name, String apn, String username,
			String password, String authenticationType, String apnSmsUpdate)
			 {
		// go to apn page
		select.selectInventoryApns();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_APN)).click();

		if (name != null) {
			// insert Name
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		if (apn != null) {
			// insert Apn
			driver.findElement(By.id(ID_APN)).clear();
			driver.findElement(By.id(ID_APN)).sendKeys(apn);
		}
		// insert User Name
		if (username != null) {
			driver.findElement(By.id(ID_USERNAME)).clear();
			driver.findElement(By.id(ID_USERNAME)).sendKeys(username);
		}

		// insert Password
		if (password != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(password);
		}

		// select Authentication type
		if (authenticationType != null) {
			persistUtil.selectByVisibleText(ID_AUTHENTICATION_TYPE,
					authenticationType);
		}

		// select Apn sms update
		if (apnSmsUpdate != null) {
			persistUtil.selectByVisibleText(ID_APN_SMS_UPDATE, apnSmsUpdate);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add apn with the requires parameters only, the rest will fill with
	 * default values
	 * 
	 * @param name
	 * @param apn
	 * @return String result message
	 * @
	 */
	public String add(String name, String apn)  {
		return add(name, apn, null, null, null, null);
	}

	/**
	 * Modify and update the apn details by id
	 * 
	 * @param id
	 * @param name
	 * @param apn
	 * @param username
	 * @param password
	 * @param authenticationType
	 * @param apnSmsUpdate
	 * @return String result message
	 * @
	 */
	public String modifyById(String id, String name, String apn,
			String username, String password, String authenticationType,
			String apnSmsUpdate)  {
		// go to apn page
		select.selectInventoryApns();

		// find the apn to modify and click on it
		driver.findElement(By.linkText(id)).click();

		if (name != null) {
			// insert Name
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		if (apn != null) {
			// insert Apn
			driver.findElement(By.id(ID_APN)).clear();
			driver.findElement(By.id(ID_APN)).sendKeys(apn);
		}

		// insert User Name
		if (username != null) {
			driver.findElement(By.id(ID_USERNAME)).clear();
			driver.findElement(By.id(ID_USERNAME)).sendKeys(username);
		}

		// insert Password
		if (password != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(password);
		}

		// select Authentication type
		if (authenticationType != null) {
			persistUtil.selectByVisibleText(ID_AUTHENTICATION_TYPE,
					authenticationType);
		}

		// select Apn sms update
		if (apnSmsUpdate != null) {
			persistUtil.selectByVisibleText(ID_APN_SMS_UPDATE, apnSmsUpdate);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete apn by apn-id
	 * 
	 * @param id
	 * @return String result message
	 * @
	 */
	public String deleteById(String id)  {
		// go to apn page
		select.selectInventoryApns();

		// delete
		persistUtil.deleteByLinkTextName(id);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new Apns().deleteAll());
		// unitTesting();
	}

	public static void unitTesting() {
		Apns apns = new Apns();

		// String id = "6";
		String name = "apnName";
		String apn = "apn";
		String username = "userName";
		String password = "password";
		String authenticationType = "None";
		String apnSmsUpdate = "Always Send";

		// ADD MAXIMUM
		try {
			System.out.println(apns.add(name, apn, username, password,
					authenticationType, apnSmsUpdate));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// ADD MINIMUM
		try {
			System.out.println(apns.add(name, apn));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// MODIFY
		try {
			System.out.println(
			// apns.modifyById(id, "apn name changed", apn, username, password,
			// authenticationType, apnSmsUpdate)
					apns.modifyByName(name, "apn name changed", apn, username,
							password, authenticationType, apnSmsUpdate));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// DELETE
		try {
			System.out.println(
			// apns.deleteById(id)
					apns.deleteByName("apn name changed"));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		apns.driver.close();
	}

	/**
	 * modify the apn by name
	 * 
	 * @param name
	 * @param newName
	 * @param apn
	 * @param username
	 * @param password
	 * @param authenticationType
	 * @param apnSmsUpdate
	 * @return String result message
	 * @
	 */
	public String modifyByName(String name, String newName, String apn,
			String username, String password, String authenticationType,
			String apnSmsUpdate)  {
		String id = getIdFromName(name);
		return modifyById(id, newName, apn, username, password,
				authenticationType, apnSmsUpdate);
	}

	/**
	 * delete the apn by name
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		String id = getIdFromName(name);
		return deleteById(id);
	}

	/**
	 * find the apn id by name and return it as a string
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	private String getIdFromName(String name)  {
		select.selectInventoryApns();
		persistUtil.searchForElement(name);
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements + 1; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td[2]/input[@value='"
						+ name + "']";
				String xpathClick = "//tr[" + i + "]/th/a";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					return driver.findElement(By.xpath(xpathClick)).getText();
				}
			} catch (Exception e) {
			}
		}
		System.err.println("The id for " + name + " was not found");
		return null;
	}

	/**
	 * Delete apns
	 */
	public String deleteAll() {
		select.selectInventoryApns();
		return persistUtil.deleteAllFromTablePage();
	}

}
