package sim_management.allocation_policies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
import sim_management.util.Actions;
/**
 * 
 * @author Dana
 *
 */
public class Policies {
	// sim replacement rules params
	private static final String SIM_REPLACEMENT_RULE_ADD_ROW = "Add another Sim Replacement Rule";
	private static final String ID_SIM_REPLACEMENT_RULE = "id_simreplacementrule_set-0-plugin";
	private static final String ID_SIM_REPLACEMENT_RULE1 = "id_simreplacementrule_set-1-plugin";
	private static final String ID_SIM_REPLACEMENT_RULE_DELETE = "id_simreplacementrule_set-0-DELETE";
	// sim selection rules params
	private static final String SIM_SELECTION_RULE_ADD_ROW = "Add another Sim Selection Rule";
	private static final String ID_SIM_SELECTION_RULE = "id_simselectionrule_set-0-plugin";
	private static final String ID_SIM_SELECTION_RULE1 = "id_simselectionrule_set-1-plugin";
	private static final String ID_SIM_SELECTION_RULE_DELETE = "id_simselectionrule_set-0-DELETE";
	
	private static final String ACTION_RESET_POLICY_COUNTERS = "Reset Policy counters";
	private static final String ACTION_MARK_POLICY_AS_MASTER = "Mark policy as master";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";
	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_ACTIVE = "id_active";
	private static final String CLASS_POLICY_NAME = "vTextField";
	private static final String XPATH_SELECT_ALL = ".//*[@id='action-toggle']";
	private static final String NAME_ACTION = "action";
	private static final String NAME_GO_BUTTON = "index";
	private WebDriver driver;
	private SimPageSelect select;
	private SimDriverUtil simDriverUtil;

	public Policies() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.driver = simDriverUtil.getDriver();
		this.select = simDriverUtil.getSelect();
	}

	/**
	 * Add policy
	 * 
	 * @param policyName
	 * @return message
	 */
	public String add(String policyName) {
		select.selectPolicies();
		// click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		if (policyName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(policyName);
		}
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Add policy with all parameters
	 * 
	 * @param policyName
	 * @param active
	 * @param refreshRule
	 * @param filterRule
	 * @return message
	 */
	public String add(String policyName, Boolean active, String refreshRule, String filterRule) {
		select.selectPolicies();
		// click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		// insert policy name
		if (policyName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(policyName);
		}
		// click on active. if marked as false it will be clicked
		if (active != null && active) {
			driver.findElement(By.id(ID_ACTIVE)).click();
		}
		// select allocation refresh rule from list
		if (refreshRule != null) {
			driver.findElement(By.linkText(SIM_REPLACEMENT_RULE_ADD_ROW)).click();
			simDriverUtil.selectByVisibleText(ID_SIM_REPLACEMENT_RULE, refreshRule);
		}
		// select allocation filtering rule from list
		if (filterRule != null) {
			driver.findElement(By.linkText(SIM_SELECTION_RULE_ADD_ROW)).click();
			simDriverUtil.selectByVisibleText(ID_SIM_SELECTION_RULE, filterRule);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Modify by policy name, select if rule should be added/modified/deleted
	 * using Actions Enum.
	 * 
	 * @param action
	 * @param policyName
	 * @param newPolicyName
	 * @param newActive
	 * @param newRefreshRule
	 * @param newFilterRule
	 * @return message
	 */
	public String modifyByPolicyName(Actions action, String policyName, String newPolicyName, Boolean newActive,
			String newRefreshRule, String newFilterRule) {
		select.selectPolicies();
		driver.findElement(By.linkText(policyName)).click();
		// insert new device id
		if (newPolicyName != null) {
			driver.findElement(By.className(CLASS_POLICY_NAME)).clear();
			driver.findElement(By.className(CLASS_POLICY_NAME)).sendKeys(newPolicyName);
		}
		// change active
		// click on the active check box if active = true
		simDriverUtil.clickCheckIfNeeded(ID_ACTIVE, newActive);

		// change refresh rule
		if (newRefreshRule != null) {

			switch (action) {
			// in case of adding another rule click on add and select a rule
			case ADD:
				driver.findElement(By.linkText(SIM_REPLACEMENT_RULE_ADD_ROW)).click();
				simDriverUtil.selectByVisibleText(ID_SIM_REPLACEMENT_RULE1, newRefreshRule);
				break;
			// in case of modifying existing policy select a newFilterRule
			case MODIFY:
				simDriverUtil.selectByVisibleText(ID_SIM_REPLACEMENT_RULE, newRefreshRule);
				break;
			// In case of deleting existing policy click on delete
			case DELETE:
				simDriverUtil.clickCheckIfNeeded(ID_SIM_REPLACEMENT_RULE_DELETE, true);
				break;
			default:
				break;
			}
		}

		// change filtering rule
		if (newFilterRule != null) {

			switch (action) {
			// in case of adding another rule click on add and select a rule
			case ADD:
				driver.findElement(By.linkText(SIM_SELECTION_RULE_ADD_ROW)).click();
				simDriverUtil.selectByVisibleText(ID_SIM_SELECTION_RULE1, newFilterRule);
				break;
			// in case of modifying existing policy select a newFilterRule
			case MODIFY:
				simDriverUtil.selectByVisibleText(ID_SIM_SELECTION_RULE, newFilterRule);
				break;
			// In case of deleting existing policy click on delete
			case DELETE:
				simDriverUtil.clickCheckIfNeeded(ID_SIM_SELECTION_RULE_DELETE, true);
				break;
			default:
				break;
			}
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	public String resetPolicyCounters() {
		// select policies page
		select.selectPolicies();
		// select all on the checkbox and select action: Reset Policy counters
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_RESET_POLICY_COUNTERS);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Mark a specific policy as master
	 * 
	 * @param name
	 * @return message
	 */
	public String markPolicyAsMaster(String name) {
		// select policies page
		select.selectPolicies();
		//find the size of the table
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		//go over all checkbox elements in the table
		for (int i = 1; i < sizeOfElements; i++) {
			try {
				//find in which index the name (text) that's being searched for exists
				String xpathCheck = "//tr[" + i + "]/th/a[text()='"+name+"']";	
				//find the matching checkbox
				String xpathClick = "//tr[" + i + "]/td[1]/input";
				//click on the checkbox with the same index as the name (text).
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					break;
				}
			} catch (Exception e) {
			}
		}
		//go to action list and select Mark policy as master  
		driver.findElement(By.name(NAME_ACTION)).sendKeys(ACTION_MARK_POLICY_AS_MASTER);
		// click on "Go"
		if (driver.findElement(By.name(NAME_GO_BUTTON)) != null) {
			driver.findElement(By.name(NAME_GO_BUTTON)).click();
		}
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete a policy by name
	 * 
	 * @param policyName
	 * @return message
	 */
	public String deleteByPolicyName(String policyName) {
		select.selectPolicies();
		simDriverUtil.deleteByLinkTextName(policyName);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all
	 * 
	 * @return message
	 */
	public String deleteAll() {
		select.selectPolicies();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
