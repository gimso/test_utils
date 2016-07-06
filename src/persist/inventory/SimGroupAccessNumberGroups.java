package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

public class SimGroupAccessNumberGroups {
	private static final String ID_SIM_GROUP = "id_sim_group";
	private static final String ID_ACCESS_NUMBER_GROUP = "id_access_number_group";
	private static final String ID_PRIORITY = "id_priority";
	private static final String LINK_TEXT_ADD_SIM_GROUP_ACCESS_NUMBER_GROUP = "Add sim group access number group";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public SimGroupAccessNumberGroups() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add sim group access number groups with all parameters available
	 * 
	 * @param simGroup
	 * @param accessNumberGroup
	 * @param priority
	 * @return String result message
	 * @
	 */
	public String add(String simGroup, String accessNumberGroup, String priority)
			 {
		// go to sim group access number groups page
		select.selectInventorySimGroupAccessNumberGroups();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_SIM_GROUP_ACCESS_NUMBER_GROUP)).click();
		
		// select sim group
		if (simGroup != null) {
			persistUtil.selectByVisibleText(ID_SIM_GROUP, simGroup);
		}

		// select access number group
		if (accessNumberGroup != null) {
			persistUtil.selectByVisibleText(ID_ACCESS_NUMBER_GROUP,accessNumberGroup);
		}

		// insert priority
		if (priority != null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add sim group access number groups with the requires parameters only, the
	 * rest will fill with default values
	 * 
	 * @param simGroup
	 * @param accessNumberGroup
	 * @return String result message
	 * @
	 */
	public String add(String simGroup, String accessNumberGroup)
			 {
		return add(simGroup, accessNumberGroup, null);
	}

	/**
	 * Modify and update the sim group access number groups details by previous
	 * sim group access number groups
	 * 
	 * @param simGroup
	 * @param newSimGroup
	 * @param accessNumberGroup
	 * @param priority
	 * @return String result message
	 * @
	 */
	public String modifyBySimGroup(String simGroup, String newSimGroup,
			String accessNumberGroup, String priority)  {
		// go to sim group access number groups page
		select.selectInventorySimGroupAccessNumberGroups();

		// find by sim group and click on it
		driver.findElement(By.linkText(simGroup)).click();

		// select sim group
		if (simGroup != null) {
			persistUtil.selectByVisibleText(ID_SIM_GROUP, simGroup);
		}

		// select access number group
		if (accessNumberGroup != null) {
			persistUtil.selectByVisibleText(ID_ACCESS_NUMBER_GROUP, accessNumberGroup);
		}

		// insert priority
		if (priority != null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete sim group access number groups by name
	 * 
	 * @param simGroup
	 * @return String result message
	 * @
	 */
	public String deleteBySimGroup(String simGroup)  {
		// go to sim group access number groups page
		select.selectInventorySimGroupAccessNumberGroups();

		// find sim group and delete the sim group access number group
		persistUtil.deleteByLinkTextName(simGroup);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new SimGroupAccessNumberGroups().deleteAll());
//		unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		String simGroup = "high";
		String newSimGroup = "a";
		String accessNumberGroup = "anGroup";
		String priority = "4";
		SimGroupAccessNumberGroups simGroupAccessNumberGroups = new SimGroupAccessNumberGroups();
		
		// ADD MAXIMUM
		System.out.println(simGroupAccessNumberGroups.add(simGroup, accessNumberGroup, priority));

		// ADD MINIMUM
		System.out.println(simGroupAccessNumberGroups.add(simGroup, accessNumberGroup));

		// MODIFY
		System.out.println(
				simGroupAccessNumberGroups.modifyBySimGroup(simGroup, newSimGroup, accessNumberGroup, priority));

		// DELETE
		System.out.println(simGroupAccessNumberGroups.deleteBySimGroup(newSimGroup));

		simGroupAccessNumberGroups.driver.close();
	}
	
	/**
	 * Delete All sim group access number groups
	 */
	public String deleteAll(){
		select.selectInventorySimGroupAccessNumberGroups();
		return persistUtil.deleteAllFromTablePage();	
	}
}