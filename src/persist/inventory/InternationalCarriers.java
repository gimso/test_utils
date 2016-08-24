package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.PersistPageSelect;

/**
 * This class is Added Modified and Delete International Carrier
 * 
 * @author Yehuda Ginsburg
 *
 */
public class InternationalCarriers {
	private static final String ID_NAME = "id_name";
	private static final String ID_URL = "id_url";
	private static final String ID_ALLOWED = "id_allowed";
	private static final String ID_PREFIX = "id_prefix";
	private static final String ID_CARRIER_SPECIFIC = "id_carrier_specific";
	private static final String LINK_TEXT_ADD_INTERNATIONAL_CARRIER = "Add international carrier";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public InternationalCarriers() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add International Carrier with all parameters available
	 * 
	 * @param name
	 * @param url
	 * @param allowed
	 *            true by default
	 * @param prefix
	 * @param carrierSpecfic
	 * @return String result message
	 * @
	 */
	public String add(String name, String url, Boolean allowed, String prefix,
			String carrierSpecfic)  {
		// go to International Carrier page
		select.selectInventoryInternationalCarriers();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_INTERNATIONAL_CARRIER)).click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		if (url !=null) {
			// insert url
			driver.findElement(By.id(ID_URL)).clear();
			driver.findElement(By.id(ID_URL)).sendKeys(url);
		}
		
		// check if not allowed
		if (allowed!=null && !allowed) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// insert prefix
		if (prefix != null) {
			driver.findElement(By.id(ID_PREFIX)).clear();
			driver.findElement(By.id(ID_PREFIX)).sendKeys(prefix);
		}

		// insert carrier specific
		if (carrierSpecfic != null) {
			driver.findElement(By.id(ID_CARRIER_SPECIFIC)).clear();
			driver.findElement(By.id(ID_CARRIER_SPECIFIC)).sendKeys(carrierSpecfic);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add International Carrier with the requires parameters only, the rest
	 * will fill with default values
	 * 
	 * @param name
	 * @param url
	 * @return String result message
	 * @
	 */
	public String add(String name, String url)  {
		return add(name, url, true, null, null);
	}

	/**
	 * Modify and update the International Carrier details by previous
	 * International Carrier
	 * 
	 * @param name
	 * @param newName
	 * @param url
	 * @param allowed
	 * @param prefix
	 * @param carrierSpecfic
	 * @return String result message
	 * @
	 */
	public String modifyByName(String name, String newName, String url,
			Boolean allowed, String prefix, String carrierSpecfic)
			 {
		// go to International Carrier page
		select.selectInventoryInternationalCarriers();

		// find the international carrier to modify and click on it
		driver.findElement(By.linkText(name)).click();

		// insert name
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		
		if (url !=null) {
			// insert url
			driver.findElement(By.id(ID_URL)).clear();
			driver.findElement(By.id(ID_URL)).sendKeys(url);
		}
		
		// check if not allowed
		if (allowed!=null ) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// insert prefix
		if (prefix != null) {
			driver.findElement(By.id(ID_PREFIX)).clear();
			driver.findElement(By.id(ID_PREFIX)).sendKeys(prefix);
		}

		// insert carrier specific
		if (carrierSpecfic != null) {
			driver.findElement(By.id(ID_CARRIER_SPECIFIC)).clear();
			driver.findElement(By.id(ID_CARRIER_SPECIFIC)).sendKeys(carrierSpecfic);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete International Carrier by name
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		// go to International Carrier page
		select.selectInventoryInternationalCarriers();

		// find and delete by name
		persistUtil.deleteByLinkTextName(name);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new InternationalCarriers().deleteAll());
		//unitTesting();
	}

	public static void unitTesting() {
		InternationalCarriers ic = new InternationalCarriers();
		String name = "a", newName = "b", url = "a", prefix = "a", carrierSpecfic = "a";
		Boolean allowed = false;
		// ADD MAXIMUM
		System.out.println(ic.add(name, url, allowed, prefix, carrierSpecfic));

		// ADD MINIMUM
		System.out.println(ic.add(name, url));

		// MODIFY
		System.out.println(ic.modifyByName(name, newName, url, allowed, prefix, carrierSpecfic));

		// DELETE
		System.out.println(ic.deleteByName(newName));

		ic.driver.close();
	}
	/**
	 * Delete All international carriers
	 */
	public String deleteAll(){
		select.selectInventoryInternationalCarriers();
		return persistUtil.deleteAllFromTablePage();	
	}
}