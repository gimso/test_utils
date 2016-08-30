package persist.usage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.PersistPageSelect;
/**
 * This class is Added Modified and Delete User Group under persist.usage.UserGroups page
 * @author Yehuda Ginsburg
 *
 */public class UserGroups {
	private static final String ID_OWNER = "id_owner";
	private static final String ID_CREATOR = "id_creator";
	private static final String ID_PARENT_USER_GROUP = "id_parent_user_group";
	private static final String ID_PLAN = "id_plan";
	private static final String ID_HOME_GEOLOCATION = "id_home_geolocation";
	private static final String ID_ALLOWED = "id_allowed";
	private static final String ID_NAME = "id_name";
	private static final String LINK_TEXT_ADD_USER_GROUP = "Add user group";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public UserGroups() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add user group with all available parameters
	 * 
	 * @param name
	 * @param allowed
	 * @param homeGeolocation
	 * @param plan
	 * @param parentUserGroup
	 * @param creator
	 * @param owner
	 * @return String result message
	 * @
	 */
	public String add(String name, Boolean allowed, String homeGeolocation,
			String plan, String parentUserGroup, String creator, String owner)
			 {

		// go to usage.UserGroup
		select.selectUsageUserGroups();

		// find add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_USER_GROUP)).click();

		// insert name
		if (name!=null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// default allowed is true, if not change it
		if (allowed != null && !allowed) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// select home geolocation
		if (homeGeolocation!=null) {
			persistUtil.selectByVisibleText(ID_HOME_GEOLOCATION, homeGeolocation);
		}
		
		// select plan
		if (plan != null) {
			persistUtil.selectByVisibleText(ID_PLAN, plan);
		}

		// select parent user group
		if (parentUserGroup != null) {
			persistUtil.selectByVisibleText(ID_PARENT_USER_GROUP, parentUserGroup);
		}
		
		// select creator
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}

		// select owner
		if (owner != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();

	}
	/**
	 * Add usage.UserGroups with the requires parameters only, the rest will fill with default values
	 * @param name
	 * @param homeGeolocation
	 * @param owner
	 * @param plan
	 * @return  String result message
	 * @
	 */
	public String add(String name, String homeGeolocation, String owner, String plan)
			 {
		return add(name, true, homeGeolocation, plan, null, null, owner);
	}
	/**
	 *  Modify and update the user group details by previous/old name
	 * @param name
	 * @param newName
	 * @param allowed
	 * @param homeGeolocation
	 * @param plan
	 * @param parentUserGroup
	 * @param creator
	 * @param owner
	 * @return  String result message
	 * @
	 */
	public String modifyByName(String name, String newName, Boolean allowed,
			String homeGeolocation, String plan, String parentUserGroup,
			String creator, String owner)  {
		// go to usage.UserGroup
		select.selectUsageUserGroups();

		// find user group by old name and click on it
		driver.findElement(By.linkText(name)).click();

		// insert name
		if (newName!=null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}

		// default allowed is true, if not change it
		if (allowed != null) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// select home geolocation
		if (homeGeolocation!=null) {
			persistUtil.selectByVisibleText(ID_HOME_GEOLOCATION, homeGeolocation);
		}
		
		// select plan
		if (plan != null) {
			persistUtil.selectByVisibleText(ID_PLAN, plan);
		}

		// select parent user group
		if (parentUserGroup != null) {
			persistUtil.selectByVisibleText(ID_PARENT_USER_GROUP, parentUserGroup);
		}
		
		// select creator
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}
		
		// select owner
		if (owner != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Delete user group by name
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		// go to usage.UserGroup
		select.selectUsageUserGroups();
		
		// find the user name and delete it
		persistUtil.deleteByLinkTextName(name);
		
		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	public static void main(String[] args) {
//		System.out.println(new UserGroups().deleteAll());//
		unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		UserGroups ug = new UserGroups();
		String name = "ug";
		Boolean allowed = false;
		String homeGeolocation = "Australia (505)";
		String plan = "plan-00";
		String parentUserGroup = "group-GB-plan-00-1";
		String creator = "Simgo";
		String owner = "Simgo";
		String oldName = "ug";
		String newName = "UG";
		System.out.println(ug.add(name, allowed, homeGeolocation, plan, parentUserGroup, creator, owner));
		System.out.println(ug.modifyByName(oldName, newName, allowed, homeGeolocation, plan, parentUserGroup, creator, owner));
		System.out.println(ug.deleteByName(newName));
		ug.driver.close();
	}

	/**
	 * Delete All userr groups
	 */
	public String deleteAll() {
		select.selectUsageUserGroups();
		return persistUtil.deleteAllFromTablePage();
	}
}