package persist.usage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

/**
 * This class is Added Modified and Delete Plug Type under
 * persist.usage.PlugTypes page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class PlugTypes {
	private static final String LINK_TEXT_ADD_PLUG_TYPE = "Add plug type";
	private static final String ID_NAME = "id_name";
	private static final String ID_ERP_INFO = "id_erp_info";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public PlugTypes() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add plug type with all parameters available
	 * 
	 * @param name
	 * @param erpInfo
	 * @return String result message @
	 */
	public String add(String name, String erpInfo) {
		// go to plug type page
		select.selectUsagePlugTypes();

		// click on Add plug type
		driver.findElement(By.linkText(LINK_TEXT_ADD_PLUG_TYPE)).click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// insert erp Info
		if (erpInfo != null) {
			driver.findElement(By.id(ID_ERP_INFO)).clear();
			driver.findElement(By.id(ID_ERP_INFO)).sendKeys(erpInfo);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add plug type with the requires parameters only, the rest will fill with
	 * default values
	 * 
	 * @param name
	 *            -
	 *            {@code Be Aware if name is exist even in another case Upper/Lower it won't add}
	 * @return String result message @
	 */
	public String add(String name) {
		return add(name, null);
	}

	/**
	 * Modify and update the plug type details by previous/old name
	 * 
	 * @param name
	 * @param newName
	 * @param erpInfo
	 * @return String result message @
	 */
	public String modifyByName(String name, String newName, String erpInfo) {
		// go to plug type page
		select.selectUsagePlugTypes();

		// click on Add plug type
		driver.findElement(By.linkText(name)).click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// insert erp Info
		if (erpInfo != null) {
			driver.findElement(By.id(ID_ERP_INFO)).clear();
			driver.findElement(By.id(ID_ERP_INFO)).sendKeys(erpInfo);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete PlugType by name
	 * 
	 * @param name
	 * @return String result message @
	 */
	public String delete(String name) {
		// go to plug type page
		select.selectUsagePlugTypes();

		// find and delete
		persistUtil.deleteByLinkTextName(name);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new PlugTypes().deleteAll());// unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		PlugTypes pt = new PlugTypes();
		String name = "pt";
		String oldName = name;
		String newName = "PT2";
		String erpInfo = "ERP";
		System.out.println(pt.add(name, erpInfo));

		System.out.println(pt.add(name));

		System.out.println(pt.modifyByName(oldName, newName, erpInfo));

		System.out.println(pt.delete(newName));

		pt.driver.close();
	}

	/**
	 * Delete All plug types
	 */
	public String deleteAll() {
		select.selectUsagePlugTypes();
		return persistUtil.deleteAllFromTablePage();
	}
}