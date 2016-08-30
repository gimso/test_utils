package sim_management.dj_celery;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class PeriodicTasks {

	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_NAME = "id_name";
	private static final String ID_REGISTERED_TASK = "id_regtask";
	private static final String ID_CUSTOM_TASK = "id_task";
	private static final String ID_ENABLED = "id_enabled";
	private static final String ID_INTERVAL = "id_interval";
	private static final String ID_CRONTAB = "id_crontab";
	private static final String NAME_SAVE = "_save";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;
	
	public PeriodicTasks() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}
	
	/**
	 * Add Peridoic task
	 * 
	 * @param name
	 * @param registeredTask
	 * @param customTask
	 * @param enabled
	 * @param interval
	 * @param crontab
	 * @return message
	 */
	public String add(
			String name, 
			String registeredTask, 
			String customTask, 
			Boolean enabled, 
			String interval, 
			String crontab
			){
		//go to period tasks
		select.selectPeriodicTasks();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		//insert registered task
		if (registeredTask != null) {
			simDriverUtil.selectByVisibleText(ID_REGISTERED_TASK, registeredTask);
		}
		//insert custom task
		if (customTask != null) {
			driver.findElement(By.id(ID_CUSTOM_TASK)).clear();
			driver.findElement(By.id(ID_CUSTOM_TASK)).sendKeys(customTask);
		}
		//click on enabled
		if (enabled != null && !enabled) {
			simDriverUtil.clickCheckIfNeeded(ID_ENABLED, enabled);
		}
		//select interval from list
		if (interval != null) {
			simDriverUtil.selectByVisibleText(ID_INTERVAL, interval);
		}
		//select crontab
		if (crontab != null) {
			simDriverUtil.selectByVisibleText(ID_CRONTAB, crontab);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Modify periodic task
	 * 
	 * @param name
	 * @param newName
	 * @param newRegisteredTask
	 * @param newCustomTask
	 * @param enabled
	 * @param newInterval
	 * @param newCrontab
	 * @return message
	 */
	public String modify(
			String name, 
			String newName, 
			String newRegisteredTask, 
			String newCustomTask, 
			Boolean enabled, 
			String newInterval, 
			String newCrontab
			){
		//go to period tasks
		select.selectPeriodicTasks();
		//click on the name to change
		driver.findElement(By.linkText(name)).click();
		//insert new name
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		//insert new registered task
		if (newRegisteredTask != null) {
			simDriverUtil.selectByVisibleText(ID_REGISTERED_TASK, newRegisteredTask);
		}
		//insert new custom task
		if (newCustomTask != null) {
			driver.findElement(By.id(ID_CUSTOM_TASK)).clear();
			driver.findElement(By.id(ID_CUSTOM_TASK)).sendKeys(newCustomTask);
		}
		//click on enabled
		if (enabled != null && !enabled) {
			simDriverUtil.clickCheckIfNeeded(ID_ENABLED, enabled);
		}
		//select new interval from list
		if (newInterval != null) {
			simDriverUtil.selectByVisibleText(ID_INTERVAL, newInterval);
		}
		//select new crontab
		if (newCrontab != null) {
			simDriverUtil.selectByVisibleText(ID_CRONTAB, newCrontab);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete task by name
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteByName(String name){
		select.selectPeriodicTasks();
		simDriverUtil.deleteByLinkTextName(name);
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete all tasks
	 * 
	 * @return message
	 */
	public String deleteAll(){
		select.selectPeriodicTasks();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
