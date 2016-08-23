package sim_management.sim_contracts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.SimDriverUtil;
import selenium.SimPageSelect;

/**
 * 
 * @author Dana
 *
 */
public class Contracts {

	private static final String NAME_SAVE = "_save";
	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_NAME = "id_name";
	private static final String ID_FILTER = "id_supported_networks_input";
	private static final String ID_FORM = "id_supported_networks_from";
	private static final String ID_SELECTOR_ARROW = "id_supported_networks_add_link";
	private static final String ID_LIMITS_AND_PRICES = "id_limits";
	private static final String ID_SUPPLIER = "id_supplier";
	private static final String ID_APN = "id_apn";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;

	public Contracts() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}

	/**
	 * Add a contract
	 * 
	 * @param name
	 * @param supportedNetwork
	 * @param limits
	 * @param supplier
	 * @param apn
	 * @return message
	 */
	public String add(String name, String supportedNetwork, String limits, String supplier, String apn) {
		// select contracts page
		select.selectContracts();
		// click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		// select a network from list using filter
		if (supportedNetwork != null) {
			driver.findElement(By.id(ID_FILTER)).sendKeys(supportedNetwork);
			simDriverUtil.selectByVisibleText(ID_FORM, supportedNetwork);
			driver.findElement(By.id(ID_SELECTOR_ARROW)).click();
		}
		// select limit from existing limits list
		if (limits != null) {
			simDriverUtil.selectByVisibleText(ID_LIMITS_AND_PRICES, limits);
		}
		// select supplier from existing suppliers list
		if (supplier != null) {
			simDriverUtil.selectByVisibleText(ID_SUPPLIER, supplier);
		}
		// select apn from existing apns list
		if (apn != null) {
			simDriverUtil.selectByVisibleText(ID_APN, apn);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Modify Contract by name
	 * 
	 * @param name
	 * @param newName
	 * @param newSupportedNetwork
	 * @param newLimits
	 * @param newSupplier
	 * @param newApn
	 * @return message
	 */
	public String modifyByName(String name, String newName, String newSupportedNetwork, String newLimits,
			String newSupplier, String newApn) {
		select.selectContracts();
		driver.findElement(By.linkText(name)).click();
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		// add another network from list using filter
		if (newSupportedNetwork != null) {
			driver.findElement(By.id(ID_FILTER)).sendKeys(newSupportedNetwork);
			simDriverUtil.selectByVisibleText(ID_FORM, newSupportedNetwork);
			driver.findElement(By.id(ID_SELECTOR_ARROW)).click();
		}
		// select limit from existing limits list
		if (newLimits != null) {
			simDriverUtil.selectByVisibleText(ID_LIMITS_AND_PRICES, newLimits);
		}
		// select supplier from existing suppliers list
		if (newSupplier != null) {
			simDriverUtil.selectByVisibleText(ID_SUPPLIER, newSupplier);
		}
		// select apn from existing apns list
		if (newApn != null) {
			simDriverUtil.selectByVisibleText(ID_APN, newApn);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete contract by name
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteByName(String name) {
		select.selectContracts();
		simDriverUtil.deleteByLinkTextName(name);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all contracts
	 * 
	 * @return message
	 */
	public String deleteAll() {
		select.selectContracts();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
