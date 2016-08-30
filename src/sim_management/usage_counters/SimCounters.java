package sim_management.usage_counters;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class SimCounters {
	
	private static final String NAME_SAVE = "_save";
	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_PROFILE = "id_profile";
	private static final String ID_COMMENTS = "id_comments";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;
	
	public SimCounters() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}
	
	/**
	 * Add sim counter with default value
	 * 
	 * @param profile
	 * @return message
	 */
	public String add(String profile){
		//go to sim counters page
		select.selectSimCounters();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//select profile from list
		if (profile != null) {
			simDriverUtil.selectByVisibleText(ID_PROFILE, profile);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Add sim counter with all values
	 * 
	 * @param profile
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @param initialUsage
	 * @param dataUsage
	 * @param comment
	 * @return message
	 */
	public String add(String profile, String comment){
		//go to sim counters page
		select.selectSimCounters();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//select profile from list
		if (profile != null) {
			simDriverUtil.selectByVisibleText(ID_PROFILE, profile);
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
	 * Modify a sim counter
	 * 
	 * @param profile
	 * @param newProfile
	 * @param newComment
	 * @return message
	 */
	public String modifyByProfile(
			String profile, 
			String newProfile,
			String newComment
			){
		//go to sim counters page
		select.selectSimCounters();
		//click on the profile to be changed
		driver.findElement(By.linkText(profile)).click();
		//select profile from list
		if (newProfile != null) {
			simDriverUtil.selectByVisibleText(ID_PROFILE, newProfile);
		}
		//insert comment
		if (newComment != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(newComment);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete a sim counter by its profile
	 * 
	 * @param profile
	 * @return message
	 */
	public String deleteByProfile(String profile){
		select.selectSimCounters();
		simDriverUtil.deleteByLinkTextName(profile);
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete all sim counters
	 * 
	 * @return message
	 */
	public String deleteAll(){
		select.selectSimCounters();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
