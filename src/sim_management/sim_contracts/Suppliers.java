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
public class Suppliers {

	private static final String NAME_SAVE = "_save";
	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_NAME = "id_name";
	private static final String ID_COUNTRY = "id_country";
	private static final String ID_WEBSITE = "id_website";
	private static final String ID_USERNAME = "id_username";
	private static final String ID_PASSWORD = "id_password";
	private static final String ID_DEPARTMENT = "id_contact_set-0-department";
	private static final String ID_FIRST_NAME = "id_contact_set-0-first_name";
	private static final String ID_LAST_NAME = "id_contact_set-0-last_name";
	private static final String ID_EMAIL = "id_contact_set-0-email";
	private static final String ID_PHONE_NUMBER = "id_contact_set-0-phone_number";
	private static final String ID_FAX = "id_contact_set-0-fax";
	private static final String ID_COMMENTS = "id_contact_set-0-comments";
	
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;
	
	public Suppliers() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}
	
	/**
	 * Add supplier with default value
	 * 
	 * @param name
	 * @param country
	 * @return message
	 */
	public String add(String name, String country){
		//go to suppliers page
		select.selectSuppliers();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		//select country from list
		if (country != null) {
			simDriverUtil.selectByVisibleText(ID_COUNTRY, country);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Add supplier with all values
	 * 
	 * @param name
	 * @param country
	 * @param website
	 * @param username
	 * @param password
	 * @param department
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param fax
	 * @param comments
	 * @return message
	 */
	public String add(
			String name, 
			String country, 
			String website, 
			String username, 
			String password, 
			String department, 
			String firstName, 
			String lastName, 
			String email, 
			String phoneNumber, 
			String fax, 
			String comments
			){
		//go to suppliers page
		select.selectSuppliers();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		//select country from list
		if (country != null) {
			simDriverUtil.selectByVisibleText(ID_COUNTRY, country);
		}
		//insert website
		if (website != null) {
			driver.findElement(By.id(ID_WEBSITE)).clear();
			driver.findElement(By.id(ID_WEBSITE)).sendKeys(website);
		}
		//insert username
		if (username != null) {
			driver.findElement(By.id(ID_USERNAME)).clear();
			driver.findElement(By.id(ID_USERNAME)).sendKeys(username);
		}
		//insert password
		if (password != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(password);
		}
		//select a department
		if (department != null) {
			simDriverUtil.selectByVisibleText(ID_DEPARTMENT, department);
		}
		//insert first name
		if (firstName != null) {
			driver.findElement(By.id(ID_FIRST_NAME)).clear();
			driver.findElement(By.id(ID_FIRST_NAME)).sendKeys(firstName);
		}
		//insert last name
		if (lastName != null) {
			driver.findElement(By.id(ID_LAST_NAME)).clear();
			driver.findElement(By.id(ID_LAST_NAME)).sendKeys(lastName);
		}
		//insert email
		if (email != null) {
			driver.findElement(By.id(ID_EMAIL)).clear();
			driver.findElement(By.id(ID_EMAIL)).sendKeys(email);
		}
		//insert phone number
		if (phoneNumber != null) {
			driver.findElement(By.id(ID_PHONE_NUMBER)).clear();
			driver.findElement(By.id(ID_PHONE_NUMBER)).sendKeys(phoneNumber);
		}
		//insert fax
		if (fax != null) {
			driver.findElement(By.id(ID_FAX)).clear();
			driver.findElement(By.id(ID_FAX)).sendKeys(fax);
		}
		//insert comments
		if (comments != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(comments);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Modify supplier
	 * 
	 * @param name
	 * @param newName
	 * @param newCountry
	 * @param newWebsite
	 * @param newUsername
	 * @param newPassword
	 * @param newDepartment
	 * @param newFirstName
	 * @param newLastName
	 * @param newEmail
	 * @param newPhoneNumber
	 * @param newFax
	 * @param newComments
	 * @return message
	 */
	public String modifyByNmae(
			String name, 
			String newName,
			String newCountry, 
			String newWebsite, 
			String newUsername, 
			String newPassword, 
			String newDepartment, 
			String newFirstName, 
			String newLastName, 
			String newEmail, 
			String newPhoneNumber, 
			String newFax, 
			String newComments
			){
		//go to suppliers page
		select.selectSuppliers();
		//click on the name of supplier to change
		driver.findElement(By.linkText(name)).click();
		//insert new name
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		//select country from list
		if (newCountry != null) {
			simDriverUtil.selectByVisibleText(ID_COUNTRY, newCountry);
		}
		//insert website
		if (newWebsite != null) {
			driver.findElement(By.id(ID_WEBSITE)).clear();
			driver.findElement(By.id(ID_WEBSITE)).sendKeys(newWebsite);
		}
		//insert username
		if (newUsername != null) {
			driver.findElement(By.id(ID_USERNAME)).clear();
			driver.findElement(By.id(ID_USERNAME)).sendKeys(newUsername);
		}
		//insert password
		if (newPassword != null) {
			driver.findElement(By.id(ID_PASSWORD)).clear();
			driver.findElement(By.id(ID_PASSWORD)).sendKeys(newPassword);
		}
		//select a department
		if (newDepartment != null) {
			simDriverUtil.selectByVisibleText(ID_DEPARTMENT, newDepartment);
		}
		//insert first name
		if (newFirstName != null) {
			driver.findElement(By.id(ID_FIRST_NAME)).clear();
			driver.findElement(By.id(ID_FIRST_NAME)).sendKeys(newFirstName);
		}
		//insert last name
		if (newLastName != null) {
			driver.findElement(By.id(ID_LAST_NAME)).clear();
			driver.findElement(By.id(ID_LAST_NAME)).sendKeys(newLastName);
		}
		//insert email
		if (newEmail != null) {
			driver.findElement(By.id(ID_EMAIL)).clear();
			driver.findElement(By.id(ID_EMAIL)).sendKeys(newEmail);
		}
		//insert phone number
		if (newPhoneNumber != null) {
			driver.findElement(By.id(ID_PHONE_NUMBER)).clear();
			driver.findElement(By.id(ID_PHONE_NUMBER)).sendKeys(newPhoneNumber);
		}
		//insert fax
		if (newFax != null) {
			driver.findElement(By.id(ID_FAX)).clear();
			driver.findElement(By.id(ID_FAX)).sendKeys(newFax);
		}
		//insert comments
		if (newComments != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(newComments);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete supplier by name
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteByName(String name){
		select.selectSuppliers();
		simDriverUtil.deleteByLinkTextName(name);
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete all suppliers on the list
	 * 
	 * @return message
	 */
	public String deleteAll(){
		select.selectSuppliers();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
