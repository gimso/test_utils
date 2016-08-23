package sim_management.allocation_policies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class ForcedSims {
	private WebDriver driver;
	private SimPageSelect select;
	private SimDriverUtil simDriverUtil;
	private static final String CLASS_NAME = "addlink";
	private static final String CLASS_DEVICE_ID = "vTextField";
	private static final String NAME_SAVE = "_save";
	private static final String ID_PROFILE = "id_profile";
	private static final String ID_EXCLUSIVE = "id_exclusive";
	
	public ForcedSims() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.driver = simDriverUtil.getDriver();
		this.select = simDriverUtil.getSelect();
	}

	/**
	 * Add forced sim with minimum required values
	 * 
	 * @param deviceId
	 * @param profile
	 * @return message
	 */
	public String add(String deviceId, String profile) {
		select.selectForcedSims();
		driver.findElement(By.className(CLASS_NAME)).click();
		if (deviceId != null) {
			driver.findElement(By.className(CLASS_DEVICE_ID)).clear();
			driver.findElement(By.className(CLASS_DEVICE_ID)).sendKeys(deviceId);
		}
		// select sim from the list
		if (profile != null) {
			simDriverUtil.selectByVisibleText(ID_PROFILE, profile);
		}
		// click on save and get the message back
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Add forced sim with all values.
	 * 
	 * @param deviceId
	 * @param profile
	 * @param exclusive
	 * @return message
	 */
	public String add(String deviceId, String profile, Boolean exclusive) {
		select.selectForcedSims();
		//click on add
		driver.findElement(By.className(CLASS_NAME)).click();
		//insert device id
		if (deviceId != null) {
			driver.findElement(By.className(CLASS_DEVICE_ID)).clear();
			driver.findElement(By.className(CLASS_DEVICE_ID)).sendKeys(deviceId);
		}
		// select sim from the list
		if (profile != null) {
			simDriverUtil.selectByVisibleText(ID_PROFILE, profile);
		}
		// When marked as true it will be added, marked as false- it will not be
		// added (checkbox will be empty)
		if (exclusive != null && !exclusive) {
			driver.findElement(By.id(ID_EXCLUSIVE)).click();
		}
		// click on save and get the message back
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Modify forced sim by device id. profile is required.
	 * 
	 * @param deviceId
	 * @param newDeviceId
	 * @param newProfile
	 * @return message
	 */
	public String modifyByDeviceId(String deviceId, String newDeviceId, String newProfile, Boolean newExclusive) {
		select.selectForcedSims();
		//click on the device id
		driver.findElement(By.linkText(deviceId)).click();
		//insert new device id
		if (newDeviceId != null) {
			driver.findElement(By.className(CLASS_DEVICE_ID)).clear();
			driver.findElement(By.className(CLASS_DEVICE_ID)).sendKeys(newDeviceId);
		}
		// select sim from the list
		if (newProfile != null) {
			simDriverUtil.selectByVisibleText(ID_PROFILE, newProfile);
		}
		//click on the exclusive check box if exclusive = true
		simDriverUtil.clickCheckIfNeeded(ID_EXCLUSIVE, newExclusive);
		//save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete a forced sim by device id.
	 * 
	 * @param deviceId
	 * @return message
	 */
	public String deleteByDeviceId(String deviceId) {
		select.selectForcedSims();
		simDriverUtil.deleteByLinkTextName(deviceId);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all forced sims
	 * 
	 * @return message
	 */
	public String deleteAll() {
		select.selectForcedSims();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
