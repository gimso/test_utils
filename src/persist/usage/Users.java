package persist.usage;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistException;
import global.TimeAndDateConvertor;
import selenium.PersistUtil;
import selenium.SelectPage;
/**
 * This class is Added Modified and Delete User under persist.usage.User page
 * @author Yehuda Ginsburg
 *
 */
public class Users {
	private static final String ID_REGISTRATION_DATE = "id_registration_date";
	private static final String ID_TOTAL_DATA_USAGE = "id_total_data_usage";
	private static final String ID_OWNER = "id_owner";
	private static final String ID_CREATOR = "id_creator";
	private static final String ID_PLAN = "id_plan";
	private static final String ID_APNS_TOKEN = "id_apns_token";
	private static final String ID_GCM_KEY = "id_gcm_key";
	private static final String ID_PASSWORD = "id_password";
	private static final String ID_HOME_NUMBER = "id_home_number";
	private static final String ID_ALLOWED = "id_allowed";
	private static final String ID_GROUP = "id_group";
	private static final String ID_NAME = "id_name";
	private static final String LINK_TEXT_ADD_USER = "Add user";
	private static final String NAME_SAVE = "_save";
	
	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Users() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}
	
	/**
	 * Add user with all details
	 * @param name
	 * @param group
	 * @param allowed
	 * @param homeNumber
	 * @param password
	 * @param gcmKey
	 * @param apnsToken
	 * @param planOverride
	 * @param creator
	 * @param owner
	 * @param totalDataFromApp
	 * @param registrationDate
	 * @return String result message
	 * @throws PersistException 
	 */
	public String add(String name, String group, Boolean allowed,
			String homeNumber, String password, String gcmKey,
			String apnsToken, String planOverride, String creator,
			String owner, Integer totalDataFromApp, Date registrationDate) {
		
		// go to usage.user page
		select.selectUsageUsers();

		// find the add user button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_USER)).click();

		// name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// group
		if (group != null) {
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// allowed
		if (allowed!=null && !allowed) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// homeNumber,
		if (homeNumber != null) {
			driver.findElement(By.id(ID_HOME_NUMBER)).clear();
			driver.findElement(By.id(ID_HOME_NUMBER)).sendKeys(homeNumber);
		}

		// password,
		if (password != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(password);
		}

		// gcmKey, "id_gcm_key"
		if (gcmKey != null) {
			driver.findElement(By.id(ID_GCM_KEY)).clear();
			driver.findElement(By.id(ID_GCM_KEY)).sendKeys(gcmKey);
		}

		// apnsToken, "id_apns_token"
		if (apnsToken != null) {
			driver.findElement(By.id(ID_APNS_TOKEN)).clear();
			driver.findElement(By.id(ID_APNS_TOKEN)).sendKeys(apnsToken);
		}

		// planOverride,
		if (planOverride != null) {
			persistUtil.selectByVisibleText(ID_PLAN, planOverride);
		}

		// Creator, "id_creator"
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}

		// owner
		if (owner != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// totalDataFromApp,
		if (totalDataFromApp != null) {
			driver.findElement(By.id(ID_TOTAL_DATA_USAGE)).clear();
			driver.findElement(By.id(ID_TOTAL_DATA_USAGE)).sendKeys(totalDataFromApp.toString());
		}

		// registrationDate
		if (registrationDate != null) {
			driver.findElement(By.id(ID_REGISTRATION_DATE)).clear();
			driver.findElement(By.id(ID_REGISTRATION_DATE)).sendKeys(TimeAndDateConvertor.convertDateToString(registrationDate));
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();
		
		//check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Add usage.User with the requires parameters only, the rest will fill with default values
	 *
	 * @param name
	 * @param group
	 * @param homeNumber
	 * @param date
	 * @param owner
	 * @return String result message
	 * @throws PersistException 
	 */
	public String add(String name, String group, String homeNumber, Date registrationDate, String owner){
		return add(name, group, true, homeNumber, null, null, null, null, null, owner, null, registrationDate);
	}
	
	/**
	 * Modify and update the user details by previous/old name
	 * @param name
	 * @param newName
	 * @param group
	 * @param allowed
	 * @param homeNumber
	 * @param password
	 * @param gcmKey
	 * @param apnsToken
	 * @param planOverride
	 * @param creator
	 * @param owner
	 * @param totalDataFromApp
	 * @param registrationDate
	 * @return String result message
	 * @throws PersistException 
	 */
	public String modifyByName(String name, String newName, String group,
			Boolean allowed, String homeNumber, String password, String gcmKey,
			String apnsToken, String planOverride, String creator,
			String owner, Integer totalDataFromApp, Date registrationDate) {
		
		// go to usage.user page
		select.selectUsageUsers();
		
		// find the user name and click on it
		driver.findElement(By.linkText(name)).click();

		// name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}

		// group
		if (group != null) {
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// allowed
		
		if (allowed != null) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// homeNumber,
		if (homeNumber != null) {
			driver.findElement(By.id(ID_HOME_NUMBER)).clear();
			driver.findElement(By.id(ID_HOME_NUMBER)).sendKeys(homeNumber);
		}

		// password,
		if (password != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(password);
		}

		// gcmKey, "id_gcm_key"
		if (gcmKey != null) {
			driver.findElement(By.id(ID_GCM_KEY)).clear();
			driver.findElement(By.id(ID_GCM_KEY)).sendKeys(gcmKey);
		}

		// apnsToken, "id_apns_token"
		if (apnsToken != null) {
			driver.findElement(By.id(ID_APNS_TOKEN)).clear();
			driver.findElement(By.id(ID_APNS_TOKEN)).sendKeys(apnsToken);
		}

		// planOverride,
		if (planOverride != null) {
			persistUtil.selectByVisibleText(ID_PLAN, planOverride);
		}

		// Creator, "id_creator"
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}

		// owner
		if (owner != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// totalDataFromApp,
		if (totalDataFromApp != null) {
			driver.findElement(By.id(ID_TOTAL_DATA_USAGE)).clear();
			driver.findElement(By.id(ID_TOTAL_DATA_USAGE)).sendKeys(totalDataFromApp.toString());
		}

		if (registrationDate != null) {
			// registrationDate
			driver.findElement(By.id(ID_REGISTRATION_DATE)).clear();
			driver.findElement(By.id(ID_REGISTRATION_DATE)).sendKeys(TimeAndDateConvertor.convertDateToString(registrationDate));
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();
		
		//check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Delete user by name
	 * @param name 
	 * @return String result message
	 * @throws PersistException 
	 * 
	 */
	public String deleteByName(String name) {
		
		// go to usage.user page
		select.selectUsageUsers();
		
		// find the user name and delete it
		persistUtil.deleteByLinkTextName(name);
		
		//check and get the message from page
		return persistUtil.finalCheck();
	}
	
	public static void main(String[] args) {
		Users u = new Users();
		u.deleteAll();
		unitTesting();
		PersistUtil.getInstance().getDriver().close();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		Users u = new Users();
		String oldName = "a";
		String group = "group-IL-plan-00-0";
		Boolean allowed = false;
		String homeNumber = "972527894561";
		String password = "1234";
		String gcmKey = "key";
		String apnsToken = "token";
		String planOverride = "plan-00";
		String creator = "Simgo";
		String owner = "Simgo";
		Integer totalDataFromApp = 2;
		Date registrationDate = new Date();
		String newName = "b";
		String add;
		add = u.add(oldName, group, allowed, homeNumber, password, gcmKey,
				apnsToken, planOverride, creator, owner, totalDataFromApp,
				registrationDate);
		System.out.println(add);
		String miniAdd;
		miniAdd = u.add(oldName, group, homeNumber, registrationDate, owner);
		System.out.println(miniAdd);

		String modified;
		modified = u.modifyByName
				(oldName, newName, group, allowed, homeNumber,
				password, gcmKey, apnsToken, planOverride, creator, owner,
				totalDataFromApp, registrationDate);
		System.out.println(modified);

		String delete;
		delete = u.deleteByName(newName);
		System.out.println(delete);

		u.driver.close();
	}
	
	//-------- Special functions ---------//
	
	/**
	 * Delete the user by home number (HSIM)
	 * @param homeNumber
	 * @throws PersistException 
	 */
	public String deleteByHomeNumber(String homeNumber) {
		// go to usage.user page
		select.selectUsageUsers();
		//search for home Number
		persistUtil.searchForElement(homeNumber);
		
		// find the user name and delete it		
		int size = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < size; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td/input[contains(@value,'" + homeNumber + "')]";
				String xpathClick = "//tr[" + i + "]/td[1]/input";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					persistUtil.deleteSelectedItemsFromTable();
					break;
				}
			} catch (Exception e) {}
		}
		//check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Delete all users
	 */
	public String deleteAll(){
		select.selectUsageUsers();
		return persistUtil.deleteAllFromTablePage();	
	}
}
