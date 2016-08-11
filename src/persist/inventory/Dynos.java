package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

/**
 * This class is Modified dyno name in inventory/dyno page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Dynos {
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Dynos() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Modify and update the dyno name details by identifier
	 * 
	 * @param identifier
	 * @param name
	 * @return String result message
	 * @
	 */
	public String modifYByIdentifier(String identifier, String name){
		
		// go to dyno page
		select.selectInventoryDynos();
		
		// find the the identifier and click on it
		driver.findElement(By.linkText(identifier)).click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		Dynos dynos = new Dynos();
		System.out.println(dynos.deleteAll());
		// unitTesting();
	}

	public static void unitTesting() {

		Dynos dynos = new Dynos();

		String identifier = "0";
		String name = "Dyno0";
		System.out.println(dynos.modifYByIdentifier(identifier, name));

		dynos.driver.close();
	}

	/**
	 * Delete dynos
	 */
	public String deleteAll() {
		select.selectInventoryDynos();
		return persistUtil.deleteAllFromTablePage();
	}
}
