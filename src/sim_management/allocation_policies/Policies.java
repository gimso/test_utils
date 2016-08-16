package sim_management.allocation_policies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
import sim_management.util.Actions;

public class Policies {

	private static final String SIM_REPLACEMENT_RULE_ADD_ROW = "Add another Sim Replacement Rule";
	private final static String ID_SIM_REPLACEMENT_RULE = "id_simreplacementrule_set-0-plugin";
	private final static String ID_SIM_REPLACEMENT_CONFIG = "id_simreplacementrule_set-0-config";
	private static final String SIM_SELECTION_RULE_ADD_ROW = "Add another Sim Selection Rule";
	private final static String ID_SIM_SELECTION_RULE = "id_simselectionrule_set-0-plugin";
	private final static String ID_SIM_SELECTION_CONFIG = "id_simselectionrule_set-0-config";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";
	private final static String CLASS_NAME = "addlink";
	private final static String ID_ACTIVE = "id_active";
	private final static String CLASS_POLICY_NAME = "vTextField";
	
	private WebDriver driver;
	private SimPageSelect select;
	private SimDriverUtil simDriverUtil;
	private Actions action;
	
	public Policies() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.driver = simDriverUtil.getDriver();
		this.select = simDriverUtil.getSelect();
	}
	
	/**
	 * Add policy
	 * @param policyName
	 * @return message
	 */
	public String add(String policyName){
		select.selectPolicies();
		//click on add
		driver.findElement(By.className(CLASS_NAME)).click();
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
	 * @param refreshConfig
	 * @param filterRule
	 * @param filterConfig
	 * @return message
	 */
	public String add(String policyName, Boolean active, String refreshRule, String refreshConfig, String filterRule, String filterConfig){	
		select.selectPolicies();
		//click on add
		driver.findElement(By.className(CLASS_NAME)).click();
		//insert policy name
		if (policyName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(policyName);
		}
		//click on active. if marked as false it will be clicked
		if (active != null && !active) {
			driver.findElement(By.id(ID_ACTIVE)).click();
		}
		//select allocation refresh rule from list	
		if (refreshRule != null) {
			driver.findElement(By.linkText(SIM_REPLACEMENT_RULE_ADD_ROW)).click();
			simDriverUtil.selectByVisibleText(ID_SIM_REPLACEMENT_RULE, refreshRule);
		}
		//insert config for refresh allocation rule
		if (refreshConfig != null) {
			driver.findElement(By.id(ID_SIM_REPLACEMENT_CONFIG)).clear();
			driver.findElement(By.id(ID_SIM_REPLACEMENT_CONFIG)).sendKeys(refreshConfig);
		}
		
		//select allocation filtering rule from list	
		if (filterRule != null) {
			driver.findElement(By.linkText(SIM_SELECTION_RULE_ADD_ROW)).click();
			simDriverUtil.selectByVisibleText(ID_SIM_SELECTION_RULE, filterRule);
		}
		//insert config for filtering allocation rule
		if (filterConfig != null) {
			driver.findElement(By.id(ID_SIM_SELECTION_CONFIG)).clear();
			driver.findElement(By.id(ID_SIM_SELECTION_CONFIG)).sendKeys(filterConfig);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();	
	}
	
	public String modifyByPolicyName(String policyName, String newPolicyName, Boolean active, String refreshRule, String refreshConfig, String filterRule, String filterConfig){
		select.selectPolicies();
		driver.findElement(By.linkText(policyName)).click();
		//insert new device id
		if (newPolicyName != null) {
			driver.findElement(By.className(CLASS_POLICY_NAME)).clear();
			driver.findElement(By.className(CLASS_POLICY_NAME)).sendKeys(newPolicyName);
		}
		//change active
		//click on the exclusive check box if exclusive = true
		simDriverUtil.clickCheckIfNeeded(ID_ACTIVE, active);
		
		switch (action) {
		//in case of adding another rule
		case ADD:
			
			break;
		//in case of modifying existing policy
		case MODIFY:
			
			break;
		//In case of deleting existing policy	
		case DELETE:
			
			break;
		default: 
			break;
		}
		return simDriverUtil.finalCheck();	
	}
	

	public String resetPolicyCounters(){
		return null;
		
	}
	public String markPolicyAsMaster(){
		return null;
		
	}
	
	public static void main(String[] args) {
		Policies p = new Policies();
		p.add("Policy1", null, "<class 'Allocations.SIMReplacementPlugins.MCCChanged.MCCChangedPlugin'>", "{\"a\": 3}", "<class 'Allocations.SIMSelectionPlugins.ForcedSIMs.ForcedSIMPlugin'>", "{\"b\": 5}");
	}
}
