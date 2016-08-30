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
public class Crontabs {

	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_MINUTE = "id_minute";
	private static final String ID_HOUR = "id_hour";
	private static final String ID_DAY_OF_WEEK = "id_day_of_week";
	private static final String ID_DAY_OF_MONTH = "id_day_of_month";
	private static final String ID_MONTH_OF_YEAR = "id_month_of_year";
	private static final String NAME_SAVE = "_save";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;

	
	public Crontabs() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}
	
	/**
	 * Add crontab
	 * 
	 * @param minute
	 * @param hour
	 * @param dayOfWeek
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @return message
	 */
	public String add(String minute, String hour, String dayOfWeek, String dayOfMonth, String monthOfYear){
		//go to crontabs page
		select.selectCrontabs();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert minute
		if (minute != null) {
			driver.findElement(By.id(ID_MINUTE)).clear();
			driver.findElement(By.id(ID_MINUTE)).sendKeys(minute);
		}
		//insert hour
		if (hour != null) {
			driver.findElement(By.id(ID_HOUR)).clear();
			driver.findElement(By.id(ID_HOUR)).sendKeys(hour);
		}
		//insert day of the week
		if (dayOfWeek != null) {
			driver.findElement(By.id(ID_DAY_OF_WEEK)).clear();
			driver.findElement(By.id(ID_DAY_OF_WEEK)).sendKeys(dayOfWeek);
		}
		//insert day of the month
		if (dayOfMonth != null) {
			driver.findElement(By.id(ID_DAY_OF_MONTH)).clear();
			driver.findElement(By.id(ID_DAY_OF_MONTH)).sendKeys(dayOfMonth);
		}
		//insert month of the year
		if (monthOfYear != null) {
			driver.findElement(By.id(ID_MONTH_OF_YEAR)).clear();
			driver.findElement(By.id(ID_MONTH_OF_YEAR)).sendKeys(monthOfYear);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Modify crontab 
	 * 
	 * @param crontab
	 * @param minute
	 * @param hour
	 * @param dayOfWeek
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @return message
	 */
	public String modify(String crontab, String minute, String hour, String dayOfWeek, String dayOfMonth, String monthOfYear){
		//go to crontabs page
		select.selectCrontabs();
		driver.findElement(By.linkText(crontab)).click();
		//insert minute
		if (minute != null) {
			driver.findElement(By.id(ID_MINUTE)).clear();
			driver.findElement(By.id(ID_MINUTE)).sendKeys(minute);
		}
		//insert hour
		if (hour != null) {
			driver.findElement(By.id(ID_HOUR)).clear();
			driver.findElement(By.id(ID_HOUR)).sendKeys(hour);
		}
		//insert day of the week
		if (dayOfWeek != null) {
			driver.findElement(By.id(ID_DAY_OF_WEEK)).clear();
			driver.findElement(By.id(ID_DAY_OF_WEEK)).sendKeys(dayOfWeek);
		}
		//insert day of the month
		if (dayOfMonth != null) {
			driver.findElement(By.id(ID_DAY_OF_MONTH)).clear();
			driver.findElement(By.id(ID_DAY_OF_MONTH)).sendKeys(dayOfMonth);
		}
		//insert month of the year
		if (monthOfYear != null) {
			driver.findElement(By.id(ID_MONTH_OF_YEAR)).clear();
			driver.findElement(By.id(ID_MONTH_OF_YEAR)).sendKeys(monthOfYear);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete by crontab name 
	 * 
	 * @param crontab
	 * @return message
	 */
	public String delete(String crontab){
		select.selectCrontabs();
		simDriverUtil.deleteByLinkTextName(crontab);
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete all crontabs
	 * 
	 * @return message
	 */
	public String deleteAll(){
		select.selectCrontabs();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
