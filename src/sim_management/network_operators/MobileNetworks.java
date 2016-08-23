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
public class MobileNetworks {

	private static final String CLASS_NAME_ADD = "addlink";
	private static final String NAME_SAVE = "_save";
	private static final String ID_OPERATOR = "id_operator";
	private static final String ID_MCC = "id_mcc";
	private static final String ID_MNC = "id_mnc";
	private static final String ID_COUNTRY = "id_country";
	private static final String ID_SEARCH = "searchbar";
	private static final String NAME_ACTION = "action";
	private static final String XPATH_SELECT_ALL = ".//*[@id='action-toggle']";
	private static final String ACTION_SYNC_NETWORK_OPERATOR = "Sync network operators";
	private WebDriver driver;
	private SimPageSelect select;
	private SimDriverUtil simDriverUtil;

	public MobileNetworks() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.driver = simDriverUtil.getDriver();
		this.select = simDriverUtil.getSelect();
	}

	/**
	 * Add a mobile network
	 * 
	 * @param operator
	 * @param mcc
	 * @param mnc
	 * @param country
	 * @return message
	 */
	public String add(String operator, String mcc, String mnc, String country) {
		// select the mobile networks page
		select.selectMobileNetworks();
		// click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		// select operator from list
		if (operator != null) {
			simDriverUtil.selectByVisibleText(ID_OPERATOR, operator);
		}
		// insert mcc
		if (mcc != null) {
			driver.findElement(By.id(ID_MCC)).clear();
			driver.findElement(By.id(ID_MCC)).sendKeys(mcc);
		}
		// insert mnc
		if (mnc != null) {
			driver.findElement(By.id(ID_MNC)).clear();
			driver.findElement(By.id(ID_MNC)).sendKeys(mnc);
		}
		// select country from list
		if (country != null) {
			simDriverUtil.selectByVisibleText(ID_COUNTRY, country);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Modify by name using search option
	 * 
	 * @param operator
	 * @param newOperator
	 * @param newMcc
	 * @param newMnc
	 * @param newCountry
	 * @return message
	 */
	public String modifyByName(String operator, String newOperator, String newMcc, String newMnc, String newCountry) {
		// select the mobile networks page
		select.selectMobileNetworks();
		// search for the operator, and click on it
		simDriverUtil.searchAndClickOnElement(ID_SEARCH, operator);
		// insert new operator
		if (newOperator != null) {
			simDriverUtil.selectByVisibleText(ID_OPERATOR, newOperator);
		}
		// insert new mcc
		if (newMcc != null) {
			driver.findElement(By.id(ID_MCC)).clear();
			driver.findElement(By.id(ID_MCC)).sendKeys(newMcc);
		}
		// insert new mnc
		if (newMnc != null) {
			driver.findElement(By.id(ID_MNC)).clear();
			driver.findElement(By.id(ID_MNC)).sendKeys(newMnc);
		}
		// insert new country
		if (newCountry != null) {
			simDriverUtil.selectByVisibleText(ID_COUNTRY, newCountry);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
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
		//select all on the checkbox and select action: sync network operator
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_SYNC_NETWORK_OPERATOR);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete by operator name using search option
	 * 
	 * @param operatorName
	 * @return message
	 */
	public String deleteByName(String operatorName) {
		select.selectMobileNetworks();
		// search for the operator to be deleted
		simDriverUtil.searchForElement(operatorName);
		// delete the first operator in the result list
		simDriverUtil.deleteByLinkTextName(operatorName);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all operators by name using the search option
	 * 
	 * @return message
	 */
	public String deleteAllByName(String operatorName) {
		select.selectMobileNetworks();
		// search for the operator to be deleted
		simDriverUtil.searchForElement(operatorName);
		// delete the operators in the result list
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
