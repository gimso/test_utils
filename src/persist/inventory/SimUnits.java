package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.PersistPageSelect;

/**
 * This class is Added Modified and Delete Sim Unit under
 * persist.inventory.SimUnit page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class SimUnits {
	private static final String LINK_TEXT_ADD_SIM_UNIT = "Add sim unit";
	private static final String ID_NAME = "id_name";
	private static final String ID_ADDRESS = "id_address";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public SimUnits() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();}

	/**
	 * Add sim unit with all parameters
	 * 
	 * @param name
	 * @param address
	 * @return String result message
	 * @
	 */
	public String add(String name, String address)  {
		
		// go to sim unit page
		select.selectInventorySimUnits();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_SIM_UNIT)).click();

		// insert name
		if (name !=null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		// insert address
		if (address !=null) {
			driver.findElement(By.id(ID_ADDRESS)).clear();
			driver.findElement(By.id(ID_ADDRESS)).sendKeys(address);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify sim unit name and address by name
	 * 
	 * @param name
	 * @param newName
	 * @param address
	 * @return String result message
	 * @
	 */
	public String modifyByName(String name, String newName, String address)
			 {
		// go to sim unit page
		select.selectInventorySimUnits();

		// find sim unit by name and click on it
		driver.findElement(By.linkText(name)).click();
		
		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// insert address
		if (address != null) {
			driver.findElement(By.id(ID_ADDRESS)).clear();
			driver.findElement(By.id(ID_ADDRESS)).sendKeys(address);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete sim unit by name
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		// go to sim unit page
		select.selectInventorySimUnits();
		
		// find and delete the sim unit
		persistUtil.deleteByLinkTextName(name);
		
		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new SimUnits().deleteAll());
		// unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		SimUnits su = new SimUnits();
		String name = "test";
		String newName = "test2";
		String address = "0.0.0.0.0";
		
			System.out.println(su.add(name, address));
		
			System.out.println(su.modifyByName(name, newName, address));
		
			System.out.println(su.deleteByName(newName));
		
		su.driver.close();
	}
	/**
	 * Delete All Sim units
	 */
	public String deleteAll(){
		select.selectInventorySimUnits();
		return persistUtil.deleteAllFromTablePage();	
	}
}