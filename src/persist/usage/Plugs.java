package persist.usage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import selenium.PersistUtil;
import selenium.PersistPageSelect;

/**
 * This class is Added Modified and Delete Plug under persist.usage.Plug page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Plugs {
	private static final String ID_ALLOWED = "id_allowed";
	private static final String ID_KEY = "id_key";
	private static final String ID_ID = "id_id";
	private static final String ID_USE_HSIM_IN_HOME_COUNTRY = "id_use_HSIM_in_home_country";
	private static final String ID_FW_FORCE = "id_fw_force";
	private static final String ID_FW_UPDATE_CONFIG = "id_fw_update_config";
	private static final String ID_FW_UPDATE_VERSION = "id_fw_update_version";
	private static final String ID_SIM_FORCE = "id_sim_force";
	private static final String ID_CREATOR = "id_creator";
	private static final String ID_OWNER = "id_owner";
	private static final String ID_USER_GROUP = "id_user_group";
	private static final String ID_PLUG_TYPE = "id_plug_type";
	private static final String ID_PLUG_ORDER = "id_plug_order";
	private static final String ID_ALLOW_TECHNICIAN_CODES = "id_allow_technician_codes";
	private static final String LINK_TEXT_ADD_PLUG = "Add plug";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public Plugs() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add plug with all parameters available
	 * 
	 * @param id
	 * @param key
	 * @param userGroup
	 * @param allowed
	 * @param useHsimInHomeCountry
	 * @param allowFwUpdate
	 * @param fwUpdateConfiguration
	 * @param fwUpdateVersion
	 * @param forcedSim
	 * @param allowTechnicianCodes
	 * @param creator
	 * @param owner
	 * @param plugType
	 * @param plugOrder
	 * @return String result message @
	 */
	public String add(String id, String key, String userGroup, Boolean allowed, Boolean useHsimInHomeCountry,
			Boolean allowFwUpdate, String fwUpdateConfiguration, String fwUpdateVersion, String forcedSim,
			Boolean allowTechnicianCodes, String creator, String owner, String plugType, String plugOrder) {
		// go to plug page
		select.selectUsagePlugs();
		// find and click the add button
		driver.findElement(By.linkText(LINK_TEXT_ADD_PLUG)).click();

		// insert id
		if (id != null) {
			driver.findElement(By.id(ID_ID)).clear();
			driver.findElement(By.id(ID_ID)).sendKeys(id);
		}

		// insert key
		if (key != null) {
			driver.findElement(By.id(ID_KEY)).clear();
			driver.findElement(By.id(ID_KEY)).sendKeys(key);
		}

		// select user group
		if (userGroup != null) {
			persistUtil.selectByVisibleText(ID_USER_GROUP, userGroup);
		}

		// insert if allowed default true
		if (allowed != null && !allowed) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// insert if to use hsim in home country default true
		if (useHsimInHomeCountry != null && !useHsimInHomeCountry) {
			driver.findElement(By.id(ID_USE_HSIM_IN_HOME_COUNTRY)).click();
		}

		// insert if allowed firmware update default false
		if (allowFwUpdate != null && allowFwUpdate) {
			driver.findElement(By.id(ID_FW_FORCE)).click();
		}

		// select firmware update configuration
		if (fwUpdateConfiguration != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_CONFIG, fwUpdateConfiguration);
		}

		// select firmware update version
		if (fwUpdateVersion != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_VERSION, fwUpdateVersion);
		}

		// select rsim to force to the plug
		if (forcedSim != null) {
			persistUtil.selectByVisibleText(ID_SIM_FORCE, forcedSim);
		}

		// insert if allowed technician code default false
		if (allowTechnicianCodes != null && allowTechnicianCodes) {
			driver.findElement(By.id(ID_ALLOW_TECHNICIAN_CODES)).click();
		}

		// select creator
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}

		// select owner
		if (owner != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// select plug type
		if (plugType != null) {
			persistUtil.selectByVisibleText(ID_PLUG_TYPE, plugType);
		}

		// select plug order
		if (plugOrder != null) {
			persistUtil.selectByVisibleText(ID_PLUG_ORDER, plugOrder);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * * Add plug with the requires parameters only, the rest will fill with
	 * default values
	 * 
	 * @param id
	 * @param key
	 * @param userGroup
	 * @return @
	 */

	public String add(String id, String key, String userGroup, String owner) {
		return add(id, key, userGroup, true, false, false, null, null, null, false, null, owner, null, null);

	}

	/**
	 * Modify and update the plug details by previous/old id
	 * 
	 * @param id
	 * @param newId
	 * @param key
	 * @param userGroup
	 * @param allowed
	 * @param useHsimInHomeCountry
	 * @param allowFwUpdate
	 * @param fwUpdateConfiguration
	 * @param fwUpdateVersion
	 * @param forcedSim
	 * @param allowTechnicianCodes
	 * @param creator
	 * @param owner
	 * @param plugType
	 * @param plugOrder
	 * @return String result message @
	 */
	public String modifyById(String id, String newId, String key, String userGroup, Boolean allowed,
			Boolean useHsimInHomeCountry, Boolean allowFwUpdate, String fwUpdateConfiguration, String fwUpdateVersion,
			String forcedSim, Boolean allowTechnicianCodes, String creator, String owner,

	String plugType, String plugOrder) {
		// go to plug type page
		select.selectUsagePlugs();

		// select plug id and click on it
		driver.findElement(By.linkText(id)).click();

		// insert id
		if (newId != null) {
			driver.findElement(By.id(ID_ID)).clear();
			driver.findElement(By.id(ID_ID)).sendKeys(newId);
		}

		// insert key
		if (key != null) {
			driver.findElement(By.id(ID_KEY)).clear();
			driver.findElement(By.id(ID_KEY)).sendKeys(key);
		}

		// select user group
		if (userGroup != null) {
			persistUtil.selectByVisibleText(ID_USER_GROUP, userGroup);
		}

		// insert if allowed default true
		persistUtil.clickCheckIfNeeded(ID_ALLOWED, allowed);

		// insert if to use hsim in home country default true
		persistUtil.clickCheckIfNeeded(ID_USE_HSIM_IN_HOME_COUNTRY, useHsimInHomeCountry);

		// insert if allowed firmware update default false
		persistUtil.clickCheckIfNeeded(ID_FW_FORCE, allowFwUpdate);

		// select firmware update configuration
		if (fwUpdateConfiguration != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_CONFIG, fwUpdateConfiguration);
		}

		// select firmware update version
		if (fwUpdateVersion != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_VERSION, fwUpdateVersion);
		}

		// select rsim to force to the plug
		if (forcedSim != null) {
			persistUtil.selectByVisibleText(ID_SIM_FORCE, forcedSim);
		}

		// insert if allowed technician code default false
		persistUtil.clickCheckIfNeeded(ID_ALLOW_TECHNICIAN_CODES, allowTechnicianCodes);

		// select creator
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}

		// select owner
		if (owner != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// select plug type
		if (plugType != null) {
			persistUtil.selectByVisibleText(ID_PLUG_TYPE, plugType);
		}

		// select plug order
		if (plugOrder != null) {
			persistUtil.selectByVisibleText(ID_PLUG_ORDER, plugOrder);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete Plug by id
	 * 
	 * @param id
	 * @return String result message @
	 */
	public String deleteById(String id) {
		// go to plug type page
		select.selectUsagePlugs();
		// delete
		persistUtil.deleteByLinkTextName(id);
		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new Plugs().deleteAll());// unitTesting();
	}

	public static void unitTesting() {
		Plugs plugs = new Plugs();
		String oldId = "000010001000";
		String newId = "000010001008";
		String id = oldId;
		String key = "1111222233334444";
		String userGroup = "Undefined";
		Boolean allowed = false;
		Boolean useHsimInHomeCountry = false;
		Boolean allowFwUpdate = true;
		String fwUpdateConfiguration = "global_qa_sgs4";
		String fwUpdateVersion = "0.6.6";
		String forcedSim = "SIMUNIT:0:0";
		Boolean allowTechnicianCodes = true;
		String creator = "Simgo";
		String owner = "Simgo";
		String plugType = "S";
		String plugOrder = "1";

		System.out.println(
				plugs.add(id, key, userGroup, allowed, useHsimInHomeCountry, allowFwUpdate, fwUpdateConfiguration,
						fwUpdateVersion, forcedSim, allowTechnicianCodes, creator, owner, plugType, plugOrder));

		System.out.println(

		plugs.add(id, key, userGroup, owner));

		System.out.println(

		plugs.modifyById(oldId, null, null, null, allowed, useHsimInHomeCountry, false, null, null, "---------",
				allowTechnicianCodes, null, null, null, null));

		System.out.println(plugs.deleteById(newId));

		plugs.driver.close();
	}

	/**
	 * Delete All plugs
	 */
	public String deleteAll() {
		select.selectUsagePlugs();
		return persistUtil.deleteAllFromTablePage();
	}
}