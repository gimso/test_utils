package sim_management.sim_contracts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class Apns {

	private static final String NAME_SAVE = "_save";
	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_NAME = "id_name";
	private static final String ID_APN = "id_apn";
	private static final String ID_USERNAME = "id_username";
	private static final String ID_PASSWORD = "id_password";
	private static final String ID_AUTH_TYPE = "id_auth_type";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;
		
	public Apns() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}
	
	/**
	 * Add apn
	 * 
	 * @param name
	 * @param apn
	 * @param userName
	 * @param password
	 * @param authType
	 * @return message
	 */
	public String add(String name, String apn, String userName, String password, String authType){
		//go to apns page
		select.selectApns();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		//insert apn
		if (apn != null) {
			driver.findElement(By.id(ID_APN)).clear();
			driver.findElement(By.id(ID_APN)).sendKeys(apn);
		}
		//insert username
		if (userName != null) {
			driver.findElement(By.id(ID_USERNAME)).clear();
			driver.findElement(By.id(ID_USERNAME)).sendKeys(userName);
		}
		//insert password
		if (password != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(password);
		}
		//select auth type from list
		if (authType != null) {
			simDriverUtil.selectByVisibleText(ID_AUTH_TYPE, authType);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Modify an apn
	 * 
	 * @param name
	 * @param newName
	 * @param newApn
	 * @param newUserName
	 * @param newPassword
	 * @param newAuthType
	 * @return message
	 */
	public String modifyByName(String name, String newName, String newApn, String newUserName, String newPassword, String newAuthType){
		//go to apns page
		select.selectApns();
		//click on the name to change
		driver.findElement(By.linkText(name)).click();
		//insert new name
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		//insert new apn
		if (newApn != null) {
			driver.findElement(By.id(ID_APN)).clear();
			driver.findElement(By.id(ID_APN)).sendKeys(newApn);
		}
		//insert new username
		if (newUserName != null) {
			driver.findElement(By.id(ID_USERNAME)).clear();
			driver.findElement(By.id(ID_USERNAME)).sendKeys(newUserName);
		}
		//insert new password
		if (newPassword != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(newPassword);
		}
		//select new auth type
		if (newAuthType != null) {
			simDriverUtil.selectByVisibleText(ID_AUTH_TYPE, newAuthType);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete an apn
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteByName(String name){
		select.selectApns();
		simDriverUtil.deleteByLinkTextName(name);
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete all apns
	 * 
	 * @return message
	 */
	public String deleteAll(){
		select.selectApns();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
