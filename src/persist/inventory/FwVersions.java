package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.PropertiesUtil;

/**
 * This class managed to add modify and delete firmware version page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class FwVersions {
	private static final String ID_FW_UPDATE_VERSION = "id_fw_update_version";
	private static final String DIRECT_PAGE_FWVERSION = 
			PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_INVENTORY") + "fwversion/";
	private static final String LINK_TEXT_ADD_FW_VERSION = "Add fw version";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistUtil persistUtil;

	public FwVersions() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
	}

	/**
	 * Add firmware version with all parameters available
	 * 
	 * @param fwVersion
	 * @return String result message
	 * @
	 */
	public String add(String fwVersion)  {
		// go to fw-version page
		driver.get(DIRECT_PAGE_FWVERSION);

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_FW_VERSION)).click();

		// insert fw version
		if (fwVersion !=null) {
			driver.findElement(By.id(ID_FW_UPDATE_VERSION)).clear();
			driver.findElement(By.id(ID_FW_UPDATE_VERSION)).sendKeys(fwVersion);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify and update the firmware version details by previous version
	 * 
	 * @param fwVersion
	 * @param newFwVersion
	 * @return String result message
	 * @
	 */
	public String modifyByVersion(String fwVersion, String newFwVersion)
			 {
		// go to fw-version page
		driver.get(DIRECT_PAGE_FWVERSION);

		// find the firmware version to modify and click on it
		driver.findElement(By.linkText(fwVersion)).click();

		// insert fw version
		if (fwVersion != null) {
			driver.findElement(By.id(ID_FW_UPDATE_VERSION)).clear();
			driver.findElement(By.id(ID_FW_UPDATE_VERSION)).sendKeys(fwVersion);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete firmware version by version
	 * 
	 * @param fwVersion
	 * @return String result message
	 * @
	 */
	public String deleteByVersion(String fwVersion)  {
		// go to fw-version page
		driver.get(DIRECT_PAGE_FWVERSION);

		// delete
		persistUtil.deleteByLinkTextName(fwVersion);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new FwVersions().deleteAll());
		//unitTesting();
	}

	public static void unitTesting() {

		FwVersions fwVersions = new FwVersions();

		String fwVersion = "3.3.3", newFwVersion = "4.4.4";
		// ADD
		System.out.println(fwVersions.add(fwVersion));

		// MODIFY
		System.out.println(fwVersions.modifyByVersion(fwVersion, newFwVersion));

		// DELETE
		System.out.println(fwVersions.deleteByVersion(newFwVersion));

		fwVersions.driver.close();
	}
	
	/**
	 * Delete all firmware versions
	 */
	public String deleteAll(){
		driver.get(DIRECT_PAGE_FWVERSION);
		return persistUtil.deleteAllFromTablePage();	
	}
}