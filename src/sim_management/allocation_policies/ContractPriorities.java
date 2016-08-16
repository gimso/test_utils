package sim_management.allocation_policies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;

/**
 * Util for add/modify/delete contract priorities
 * 
 * @author Dana
 */
public class ContractPriorities {

	private WebDriver driver;
	private SimPageSelect select;
	private SimDriverUtil simDriverUtil;
	private static final String ID_MCC = "id_mcc";
	private static final String CLASS_NAME = "addlink";
	private static final String NAME_SAVE = "_save";

	public ContractPriorities() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.driver = simDriverUtil.getDriver();
		this.select = simDriverUtil.getSelect();
	}

	/**
	 * Add MCC priority
	 * 
	 * @param mcc
	 * @return message from page
	 */
	public String add(String mcc) {
		// select contract priorities page, click on add mcc priority
		select.selectContractPriorities();
		driver.findElement(By.className(CLASS_NAME)).click();
		// add mcc
		if (mcc != null) {
			driver.findElement(By.id(ID_MCC)).clear();
			driver.findElement(By.id(ID_MCC)).sendKeys(mcc);
		}
		// click on save and get the message back
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Modify Priority by MCC ID
	 * 
	 * @param mccId
	 * @param newMcc
	 * @return message
	 */
	public String modifyByMccId(String mccId, String newMcc) {
		select.selectContractPriorities();
		driver.findElement(By.linkText(mccId)).click();
		if (newMcc != null) {
			driver.findElement(By.id(ID_MCC)).clear();
			driver.findElement(By.id(ID_MCC)).sendKeys(newMcc);
		}
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete a priority by mcc id
	 * 
	 * @param mccId
	 * @return message
	 */
	public String deleteById(String mccId) {
		select.selectContractPriorities();
		simDriverUtil.deleteByLinkTextName(mccId);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all priorities
	 * 
	 * @return message
	 */
	public String deleteAll() {
		select.selectContractPriorities();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
