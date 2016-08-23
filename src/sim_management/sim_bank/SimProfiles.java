package sim_management.sim_bank;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class SimProfiles {

	private static final String NAME_SAVE = "_save";
	private static final String NAME_APPLY = "apply";
	private static final String ID_SEARCH = "searchbar";
	private static final String ID_CONTRACT = "id_contract";
	private static final String ID_MSISDN = "id_msisdn";
	private static final String ID_PIN1 = "id_pin1";
	private static final String ID_PIN2 = "id_pin2";
	private static final String ID_PUK1 = "id_puk1";
	private static final String ID_PUK2 = "id_puk2";
	private static final String ID_COMMENTS = "id_comments";
	private static final String NAME_ACTION = "action";
	private static final String XPATH_SELECT_ALL = ".//*[@id='action-toggle']";
	private static final String ACTION_RESET_SIMS = "Reset SIMs";
	private static final String ACTION_ADD_SIMS_TO_CONTRACT = "Add SIMs to Contract";
	private static final String ACTION_FREE_ALLOCATIONS = "Free allocations";
	private static final String ACTION_DELETE_SELECTED_PROFILES = "Delete selected sim profiles";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;
	
	
	public SimProfiles() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}
	
	/**
	 * Modify sim's profile by placement
	 * 
	 * @param placement
	 * @param contract
	 * @param msisdn
	 * @param pin1
	 * @param puk1
	 * @param pin2
	 * @param puk2
	 * @param comment
	 * @return message
	 */
	public String modifyByPlacement(
			String placement, 
			String contract, 
			String msisdn, 
			String pin1, 
			String puk1, 
			String pin2, 
			String puk2, 
			String comment
			){
		//select sim profile page
		select.selectSimProfiles();
		//click on the sim's placement
		driver.findElement(By.linkText(placement)).click();
		//select contract
		if (contract != null) {
			simDriverUtil.selectByVisibleText(ID_CONTRACT, contract);
		}
		//insert msisdn
		if (msisdn != null) {
			driver.findElement(By.id(ID_MSISDN)).clear();
			driver.findElement(By.id(ID_MSISDN)).sendKeys(msisdn);
		}
		//insert pin1
		if (pin1 != null) {
			driver.findElement(By.id(ID_PIN1)).clear();
			driver.findElement(By.id(ID_PIN1)).sendKeys(pin1);
		}
		//insert puk1
		if (puk1 != null) {
			driver.findElement(By.id(ID_PUK1)).clear();
			driver.findElement(By.id(ID_PUK1)).sendKeys(puk1);
		}
		//insert pin2
		if (pin2 != null) {
			driver.findElement(By.id(ID_PIN2)).clear();
			driver.findElement(By.id(ID_PIN2)).sendKeys(pin2);
		}
		//insert puk2
		if (puk2 != null) {
			driver.findElement(By.id(ID_PUK2)).clear();
			driver.findElement(By.id(ID_PUK2)).sendKeys(puk2);
		}
		//insert comment
		if (comment != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(comment);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();		
	}	
	
	/**
	 * Search for imsi and add a contract to it
	 * 
	 * @param imsi
	 * @param contract
	 * @param placement
	 * @return message
	 */
	public String addSimToContractByImsi(String imsi, String contract, String placement){
		//select sim profiles page
		select.selectSimProfiles();
		//search for the correct profile by imsi
		driver.findElement(By.id(ID_SEARCH)).sendKeys(imsi);
		driver.findElement(By.id(ID_SEARCH)).submit();
		//select all, then select the action "add sims to contract", and click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_ADD_SIMS_TO_CONTRACT);
		//select a contract from list
		simDriverUtil.selectByVisibleText(ID_CONTRACT, contract);
		//click on "apply"
		driver.findElement(By.name(NAME_APPLY)).click();
		return simDriverUtil.finalCheck();	
	}
	
	/**
	 * Search for imsi and reset the sim
	 * 
	 * @param imsi
	 * @return message
	 */
	public String resetSimByImsi(String imsi){
		//select sim profiles page
		select.selectSimProfiles();
		//search for the correct profile by imsi
		driver.findElement(By.id(ID_SEARCH)).sendKeys(imsi);
		driver.findElement(By.id(ID_SEARCH)).submit();
		//select all, then select the action "reset sims", and click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_RESET_SIMS);
		return simDriverUtil.finalCheck();	
	}
	
	/**
	 * Search for imsi and free its allocation
	 * 
	 * @param imsi
	 * @return message
	 */
	public String freeAllocationByImsi(String imsi){
		//select sim profiles page
		select.selectSimProfiles();
		//search for the correct profile by imsi
		driver.findElement(By.id(ID_SEARCH)).sendKeys(imsi);
		driver.findElement(By.id(ID_SEARCH)).submit();
		//select all, then select the action "free allocations", and click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_FREE_ALLOCATIONS);
		return simDriverUtil.finalCheck();	
	}
	
	
	/**
	 * Search for imsi and delete its profile
	 * 
	 * @param imsi
	 * @return message
	 */
	public String deleteProfileByImsi(String imsi){
		//select sim profiles page
		select.selectSimProfiles();
		//search for the correct profile by imsi
		driver.findElement(By.id(ID_SEARCH)).sendKeys(imsi);
		driver.findElement(By.id(ID_SEARCH)).submit();
		//select all, then select the action "delete selected profiles", and click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_DELETE_SELECTED_PROFILES);
		return simDriverUtil.finalCheck();	
	}
}
