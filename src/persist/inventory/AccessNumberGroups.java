package persist.inventory;

import selenium.PersistUtil;
import selenium.PersistPageSelect;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This class is Added Modified and Delete access number group in access number
 * group page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class AccessNumberGroups {
	private static final String LINK_TEXT_ADD_ACCESS_NUMBER_GROUP = "Add access number group";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public AccessNumberGroups() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add access number group with all parameters available
	 * 
	 * @param name
	 * @param allowed
	 * @return String result message
	 * @
	 */
	public String add(String name, Boolean allowed)  {
		// go to access number group page
		select.selectInventoryAccessNumbersGroups();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_ACCESS_NUMBER_GROUP))
				.click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// check if not allowed
		if (allowed!=null&&!allowed) {
			driver.findElement(By.id("id_allowed")).click();
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add access number group with the requires parameters only, the rest will
	 * fill with default values
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String add(String name)  {
		return add(name, true);
	}

	/**
	 * Modify and update the access number group details by access number group
	 * name
	 * 
	 * @param name
	 * @param newName
	 * @param allowed
	 * @return String result message
	 * @
	 */
	public String modifyByName(String name, String newName, Boolean allowed)
			 {
		// go to access number group page
		select.selectInventoryAccessNumbersGroups();

		// find the access number group to modify and click on it
		driver.findElement(By.linkText(name)).click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}

		// check if not allowed
		if (allowed!=null) {
			driver.findElement(By.id("id_allowed")).click();
		}
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete access number group by access number group name
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		// go to access number group page
		select.selectInventoryAccessNumbersGroups();

		// delete
		persistUtil.deleteByLinkTextName(name);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		AccessNumberGroups accessNumberGroups = new AccessNumberGroups();
		System.out.println(accessNumberGroups.deleteAll());
		// unitTesting();
	}

	public static void unitTesting() {
		AccessNumberGroups accessNumberGroups = new AccessNumberGroups();
		String name = "name", newName = "new name";
		Boolean allowed = false;

		// ADD MAXIMUM
		System.out.println(accessNumberGroups.add(name, allowed));

		// ADD MINIMUM
		System.out.println(accessNumberGroups.add(name));

		// MODIFY
		System.out.println(accessNumberGroups.modifyByName(name, newName, allowed));

		// DELETE
		System.out.println(accessNumberGroups.deleteByName(newName));

		accessNumberGroups.driver.close();
	}

	/**
	 * Delete access number groups
	 */
	public String deleteAll() {
		select.selectInventoryAccessNumbersGroups();
		return persistUtil.deleteAllFromTablePage();
	}
}
