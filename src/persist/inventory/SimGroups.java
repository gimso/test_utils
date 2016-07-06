package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

/**
 * This class is Added Modified and Delete Sim Groups in
 * persist.inventory.SimGroups page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class SimGroups {
	private static final String ID_PRIORITY = "id_priority";
	private static final String ID_APN = "id_apn";
	private static final String ID_SMSC = "id_smsc";
	private static final String ID_ROAMING_MODE = "id_roaming_mode";
	private static final String ID_DIGEST_AUGMENTATION = "id_digest_augmentation";
	private static final String LINK_TEXT_ADD_SIM_GROUP = "Add sim group";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public SimGroups() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add sim group with all parameters available
	 * 
	 * @param name
	 * @param priority
	 * @param apn
	 * @param smsc
	 * @param roamingMode
	 * @param digestAugmentation
	 * @return String result message
	 * @
	 */
	public String add(String name, String priority, String apn, String smsc,
			String roamingMode, String digestAugmentation){

		// go to sim groups page
		select.selectInventorySimGroups();
		
		// find the sim groups add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_SIM_GROUP)).click();
		
		// insert name
		if (name!=null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		// insert priority
		if (priority != null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}
		
		// select APN
		if (apn != null) {
			persistUtil.selectByVisibleText(ID_APN, apn);
		}
		
		// insert smsc (integer)
		if (smsc != null) {
			driver.findElement(By.id(ID_SMSC)).clear();
			driver.findElement(By.id(ID_SMSC)).sendKeys(smsc);
		}
		
		// select roaming mode
		if (roamingMode != null) {
			persistUtil.selectByVisibleText(ID_ROAMING_MODE, roamingMode);
		}
		
		// insert digest augmentation
		if (digestAugmentation != null) {
			driver.findElement(By.id(ID_DIGEST_AUGMENTATION)).clear();
			driver.findElement(By.id(ID_DIGEST_AUGMENTATION)).sendKeys(digestAugmentation);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add sim group with the requires parameters only, the rest will fill with
	 * default values
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String add(String name)  {
		return add(name, null, null, null, null, null);
	}

	/**
	 * Modify and update the sim group details by previous/old name
	 * 
	 * @param name
	 * @param newName
	 * @param priority
	 * @param apn
	 * @param smsc
	 * @param roamingMode
	 * @param digestAugmentation
	 * @return String result message
	 * @
	 */
	public String modifyByName(String name, String newName, String priority,
			String apn, String smsc, String roamingMode,
			String digestAugmentation)  {
		
		// go to sim groups page
		select.selectInventorySimGroups();

		// find the sim group by name and click on it
		driver.findElement(By.linkText(name)).click();
		
		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// insert priority
		if (priority != null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}

		// select APN
		if (apn != null) {
			persistUtil.selectByVisibleText(ID_APN, apn);
		}

		// insert smsc (integer)
		if (smsc != null) {
			driver.findElement(By.id(ID_SMSC)).clear();
			driver.findElement(By.id(ID_SMSC)).sendKeys(smsc);
		}

		// select roaming mode
		if (roamingMode != null) {
			persistUtil.selectByVisibleText(ID_ROAMING_MODE, roamingMode);
		}

		// insert digest augmentation
		if (digestAugmentation != null) {
			driver.findElement(By.id(ID_DIGEST_AUGMENTATION)).clear();
			driver.findElement(By.id(ID_DIGEST_AUGMENTATION)).sendKeys(digestAugmentation);
		}
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete sim group by name
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		// go to sim groups page
		select.selectInventorySimGroups();

		// find and delete by name
		persistUtil.deleteByLinkTextName(name);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		// unitTesting();
		System.out.println(new SimGroups().deleteAll());
	}

	public static void unitTesting() {
		SimGroups sg = new SimGroups();
		String name = "A";
		String priority = "3";
		String apn = "w";
		String smsc = "1";
		String roamingMode = "Allow Roaming";
		String digestAugmentation = "";
		String newName = "B";

		// ADD MAXIMUM
		System.out.println(sg.add(name, priority, apn, smsc, roamingMode, digestAugmentation));

		// ADD MINIMUM
		System.out.println(sg.add(name));

		// MODIFY
		System.out.println(sg.modifyByName(name, newName, priority, apn, smsc, roamingMode, digestAugmentation));

		// DELETE
		System.out.println(sg.deleteByName(newName));

		sg.driver.close();
	}
	/**
	 * Delete All sim groups
	 */
	public String deleteAll(){
		select.selectInventorySimGroups();
		return persistUtil.deleteAllFromTablePage();	
	}
}