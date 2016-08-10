package sim_management.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import global.PropertiesUtil;
/**
 * @author Dana
 * Page select class, based on the home page of 'Simgo vSIM' GUI
 */
public class SimPageSelect {
	private static final String HOMEPAGE_URL = PropertiesUtil.getInstance().getProperty("SIMGO_VSIM_GUI");
	private WebDriver webDriver;
	
	
	public SimPageSelect(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	/*
	 *  Select Contract Priorities
	 */
	public void selectContractPriorities() {
		webDriver.get(HOMEPAGE_URL + "Allocations/mccpriority/");
		webDriver.findElement(By.linkText("Contract Priorities")).click();
	}
	
	/*
	 *  Select Device states
	 */
	public void selectDeviceStates() {
		webDriver.get(HOMEPAGE_URL + "Allocations/devicestate/");
		webDriver.findElement(By.linkText("Device states")).click();
	}
	
	/*
	 *  Select Forced sims
	 */
	public void selectForcedSims() {
		webDriver.get(HOMEPAGE_URL + "Allocations/forcedsim/");
		webDriver.findElement(By.linkText("Forced sims")).click();
	}
	
	/*
	 *  Select Policies
	 */
	public void selectPolicies() {
		webDriver.get(HOMEPAGE_URL + "Allocations/policy/");
		webDriver.findElement(By.linkText("Policies")).click();
	}
	
	/*
	 *  Select Crontabs
	 */
	public void selectCrontabs() {
		webDriver.get(HOMEPAGE_URL + "djcelery/crontabschedule/");
		webDriver.findElement(By.linkText("Crontabs")).click();
	}
	
	/*
	 *  Select Intervals
	 */
	public void selectIntervals() {
		webDriver.get(HOMEPAGE_URL + "djcelery/intervalschedule/");
		webDriver.findElement(By.linkText("Intervals")).click();
	}
	
	/*
	 *  Select Periodic tasks
	 */
	public void selectPeriodicTasks() {
		webDriver.get(HOMEPAGE_URL + "djcelery/periodictask/");
		webDriver.findElement(By.linkText("Periodic tasks")).click();
	}
	
	/*
	 *  Select Mobile networks
	 */
	public void selectMobileNetworks() {
		webDriver.get(HOMEPAGE_URL + "MobileOperators/mobilenetwork/");
		webDriver.findElement(By.linkText("Mobile networks")).click();
	}
	
	/*
	 *  Select Network operators
	 */
	public void selectNetworkOperators() {
		webDriver.get(HOMEPAGE_URL + "MobileOperators/networkoperator/");
		webDriver.findElement(By.linkText("Network operators")).click();
	}
	
	/*
	 *  Select Sim profiles
	 */
	public void selectSimProfiles() {
		webDriver.get(HOMEPAGE_URL + "SimBank/simprofile/");
		webDriver.findElement(By.linkText("Sim profiles")).click();
	}
	
	/*
	 *  Select Sim units
	 */
	public void selectSimUnits() {
		webDriver.get(HOMEPAGE_URL + "SimBank/simunit/");
		webDriver.findElement(By.linkText("Sim units")).click();
	}
	
	/*
	 *  Select Apns
	 */
	public void selectApns() {
		webDriver.get(HOMEPAGE_URL + "SimContracts/apn/");
		webDriver.findElement(By.linkText("Apns")).click();
	}
	
	/*
	 *  Select Contracts
	 */
	public void selectContracts() {
		webDriver.get(HOMEPAGE_URL + "SimContracts/contract/");
		webDriver.findElement(By.linkText("Contracts")).click();
	}
	
	/*
	 *  Select Limits
	 */
	public void selectLimits() {
		webDriver.get(HOMEPAGE_URL + "SimContracts/limit/");
		webDriver.findElement(By.linkText("Limits")).click();
	}
	
	/*
	 *  Select Suppliers
	 */
	public void selectSuppliers() {
		webDriver.get(HOMEPAGE_URL + "SimContracts/supplier/");
		webDriver.findElement(By.linkText("Suppliers")).click();
	}
	
	/*
	 *  Select Sim counters
	 */
	public void selectSimCounters() {
		webDriver.get(HOMEPAGE_URL + "Counters/simcounter/");
		webDriver.findElement(By.linkText("Sim counters")).click();
	}
}
