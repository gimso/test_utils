package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import global.PropertiesUtil;
/**
 * @author Dana
 * Page select class, based on the home page of 'Simgo vSIM' GUI
 */
public class SimPageSelect {
	private static final String HOMEPAGE_URL = PropertiesUtil.getInstance().getProperty("SIMGO_VSIM_GUI");
	private static final String ALLOCATIONS_URL = HOMEPAGE_URL + "Allocations/";
	private static final String DJCELERY_URL = HOMEPAGE_URL + "djcelery/";
	private static final String MOBILE_OPERATORS_URL = HOMEPAGE_URL + "MobileOperators/";
	private static final String SIM_BANK_URL = HOMEPAGE_URL + "SimBank/";
	private static final String SIM_CONTRACTS_URL = HOMEPAGE_URL + "SimContracts/";
	private static final String USAGE_COUNTERS_URL = HOMEPAGE_URL + "Counters/";
	private WebDriver webDriver;
	
	public SimPageSelect(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	/*
	 *  Select Contract Priorities
	 */
	public void selectContractPriorities() {
		webDriver.get(ALLOCATIONS_URL);
		webDriver.findElement(By.linkText("Contract Priorities")).click();
	}
	
	/*
	 *  Select Device states
	 */
	public void selectDeviceStates() {
		webDriver.get(ALLOCATIONS_URL);
		webDriver.findElement(By.linkText("Device states")).click();
	}
	
	/*
	 *  Select Forced sims
	 */
	public void selectForcedSims() {
		webDriver.get(ALLOCATIONS_URL);
		webDriver.findElement(By.linkText("Forced sims")).click();
	}
	
	/*
	 *  Select Policies
	 */
	public void selectPolicies() {
		webDriver.get(ALLOCATIONS_URL);
		webDriver.findElement(By.linkText("Policies")).click();
	}
	
	/*
	 *  Select Crontabs
	 */
	public void selectCrontabs() {
		webDriver.get(DJCELERY_URL);
		webDriver.findElement(By.linkText("Crontabs")).click();
	}
	
	/*
	 *  Select Intervals
	 */
	public void selectIntervals() {
		webDriver.get(DJCELERY_URL);
		webDriver.findElement(By.linkText("Intervals")).click();
	}
	
	/*
	 *  Select Periodic tasks
	 */
	public void selectPeriodicTasks() {
		webDriver.get(DJCELERY_URL);
		webDriver.findElement(By.linkText("Periodic tasks")).click();
	}
	
	/*
	 *  Select Mobile networks
	 */
	public void selectMobileNetworks() {
		webDriver.get(MOBILE_OPERATORS_URL);
		webDriver.findElement(By.linkText("Mobile networks")).click();
	}
	
	/*
	 *  Select Network operators
	 */
	public void selectNetworkOperators() {
		webDriver.get(MOBILE_OPERATORS_URL);
		webDriver.findElement(By.linkText("Network operators")).click();
	}
	
	/*
	 *  Select Sim profiles
	 */
	public void selectSimProfiles() {
		webDriver.get(SIM_BANK_URL);
		webDriver.findElement(By.linkText("Sim profiles")).click();
	}
	
	/*
	 *  Select Sim units
	 */
	public void selectSimUnits() {
		webDriver.get(SIM_BANK_URL);
		webDriver.findElement(By.linkText("Sim units")).click();
	}
	
	/*
	 *  Select Apns
	 */
	public void selectApns() {
		webDriver.get(SIM_CONTRACTS_URL);
		webDriver.findElement(By.linkText("Apns")).click();
	}
	
	/*
	 *  Select Contracts
	 */
	public void selectContracts() {
		webDriver.get(SIM_CONTRACTS_URL);
		webDriver.findElement(By.linkText("Contracts")).click();
	}
	
	/*
	 *  Select Limits
	 */
	public void selectLimits() {
		webDriver.get(SIM_CONTRACTS_URL);
		webDriver.findElement(By.linkText("Limits")).click();
	}
	
	/*
	 *  Select Suppliers
	 */
	public void selectSuppliers() {
		webDriver.get(SIM_CONTRACTS_URL);
		webDriver.findElement(By.linkText("Suppliers")).click();
	}
	
	/*
	 *  Select Sim counters
	 */
	public void selectSimCounters() {
		webDriver.get(USAGE_COUNTERS_URL);
		webDriver.findElement(By.linkText("Sim counters")).click();
	}
}
