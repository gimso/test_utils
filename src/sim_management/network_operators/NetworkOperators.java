package sim_management.network_operators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class NetworkOperators {

	private static final String CLASS_NAME_ADD = "addlink";
	private static final String NAME_SAVE = "_save";
	private static final String ID_NAME = "id_name";
	private static final String ID_SEARCH = "searchbar";
	private static final String XPATH_SELECT_ALL = ".//*[@id='action-toggle']";
	private static final String NAME_ACTION = "action";
	private static final String ACTION_SYNC_NETWORK_OPERATOR = "Sync network operators";
	private WebDriver driver;
	private SimPageSelect select;
	private SimDriverUtil simDriverUtil;

	public NetworkOperators() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.driver = simDriverUtil.getDriver();
		this.select = simDriverUtil.getSelect();
	}

	/**
	 * Add a network operator
	 * 
	 * @param name
	 * @return message
	 */
	public String add(String name) {
		select.selectNetworkOperators();
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Modify an network operator by name
	 * 
	 * @param name
	 * @param newName
	 * @return message
	 */
	public String modifyByName(String name, String newName) {
		// go to
		select.selectNetworkOperators();
		simDriverUtil.searchAndClickOnElement(ID_SEARCH, name);
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete a network operator by name
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteByName(String name) {
		select.selectMobileNetworks();
		// search for the operator to be deleted
		simDriverUtil.searchForElement(name);
		// delete the first operator in the result list
		simDriverUtil.deleteByLinkTextName(name);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all network operators, using the search option
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteAllByName(String name) {
		select.selectMobileNetworks();
		// search for the operator to be deleted
		simDriverUtil.searchForElement(name);
		// delete the operators in the result list
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Sync network operators.
	 * 
	 * @return message
	 */
	public String syncNetworkOperators() {
		// select the mobile networks page
		select.selectMobileNetworks();
		// select all on the checkbox and select action: sync network operator
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_SYNC_NETWORK_OPERATOR);
		return simDriverUtil.finalCheck();
	}
}
