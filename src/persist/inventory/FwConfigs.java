package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.PropertiesUtil;

/**
 * This class managed to add modify and delete firmware configuration page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class FwConfigs {
	private static final String ID_FW_UPDATE_CONFIGURATION = "id_fw_update_config";
	private static final String DIRECT_PAGE_FW_CONFIGS = 
			PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_INVENTORY") + "fwconfig/";
	private static final String LINK_TEXT_ADD_FW_CONFIG = "Add fw config";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistUtil persistUtil;

	public FwConfigs() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
	}

	/**
	 * Add firmware configuration with all parameters available
	 * 
	 * @param fwConfig
	 * @return String result message
	 * @
	 */
	public String add(String fwConfig)  {
		// go to fw configuration page
		driver.get(DIRECT_PAGE_FW_CONFIGS);

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_FW_CONFIG)).click();

		// insert fw config
		if (fwConfig !=null) {
			driver.findElement(By.id(ID_FW_UPDATE_CONFIGURATION)).clear();
			driver.findElement(By.id(ID_FW_UPDATE_CONFIGURATION)).sendKeys(fwConfig);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify and update the firmware configuration details by previous
	 * configuration
	 * 
	 * @param fwConfig
	 * @param newfwConfig
	 * @return String result message
	 * @
	 */
	public String modifyByConfig(String fwConfig, String newfwConfig)  {
		
		// go to fw configuration page
		driver.get(DIRECT_PAGE_FW_CONFIGS);

		// find the firmware configuration to modify and click on it
		driver.findElement(By.linkText(fwConfig)).click();

		// insert fw config
		if (newfwConfig !=null) {
			driver.findElement(By.id(ID_FW_UPDATE_CONFIGURATION)).clear();
			driver.findElement(By.id(ID_FW_UPDATE_CONFIGURATION)).sendKeys(newfwConfig);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete firmware configuration by configuration
	 * 
	 * @param fwConfig
	 * @return String result message
	 * @
	 */
	public String deleteByConfig(String fwConfig)  {
		// go to fw configuration page
		driver.get(DIRECT_PAGE_FW_CONFIGS);

		// delete
		persistUtil.deleteByLinkTextName(fwConfig);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		FwConfigs configs = new FwConfigs();
		System.out.println(configs.deleteAll());
		//unitTesting();
	}

	public static void unitTesting() {

		FwConfigs fwConfigs = new FwConfigs();

		String fwConfig = "config", newfwConfig = "configuration";
		// ADD
		System.out.println(fwConfigs.add(fwConfig));

		// MODIFY
		System.out.println(fwConfigs.modifyByConfig(fwConfig, newfwConfig));

		// DELETE
		System.out.println(fwConfigs.deleteByConfig(newfwConfig));

		fwConfigs.driver.close();
	}

	
	/**
	 * Delete firmware config
	 */
	public String deleteAll(){
		driver.get(DIRECT_PAGE_FW_CONFIGS);
		return persistUtil.deleteAllFromTablePage();	
	}

}