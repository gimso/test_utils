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
public class Intervals {

	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_EVERY = "id_every";
	private static final String ID_PERIOD = "id_period";
	private static final String NAME_SAVE = "_save";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;

	public Intervals() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}

	public String add(String every, String period) {
		// go to intervals page
		select.selectIntervals();
		// click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		// insert "every"
		if (every != null) {
			driver.findElement(By.id(ID_EVERY)).clear();
			driver.findElement(By.id(ID_EVERY)).sendKeys(every);
		}
		// select period
		if (period != null) {
			simDriverUtil.selectByVisibleText(ID_PERIOD, period);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	public String modify(String interval, String every, String period) {
		// go to intervals page
		select.selectIntervals();
		// click on add
		driver.findElement(By.linkText(interval)).click();
		// insert "every"
		if (every != null) {
			driver.findElement(By.id(ID_EVERY)).clear();
			driver.findElement(By.id(ID_EVERY)).sendKeys(every);
		}
		// select period
		if (period != null) {
			simDriverUtil.selectByVisibleText(ID_PERIOD, period);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete an interval by its name
	 * 
	 * @param interval
	 * @return message
	 */
	public String delete(String interval) {
		select.selectIntervals();
		simDriverUtil.deleteByLinkTextName(interval);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all intervals
	 * 
	 * @return message
	 */
	public String deleteAll() {
		select.selectIntervals();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
