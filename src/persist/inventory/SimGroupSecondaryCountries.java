package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

/**
 * This class is Added and Modified and Delete sim group secondary country in
 * persist.inventory.SimGroupSecondaryCountry page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class SimGroupSecondaryCountries {
	private static final String ID_GEO_LOCATION = "id_geo_location";
	private static final String ID_PRIORITY = "id_priority";
	private static final String ID_GROUP = "id_group";
	private static final String LINK_TEXT_ADD_SIM_GROUP_SECONDARY_COUNTRY = "Add sim group secondary country";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public SimGroupSecondaryCountries() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add Sim group secondary country with all parameters available
	 * 
	 * @param group
	 * @param priority
	 * @param geoLocation
	 * @return String result message
	 * @
	 */
	public String add(String group, String priority, String geoLocation){
		// go to sim group secondary country page
		select.selectInventorySimGroupSecondaryCountries();

		// find the add button link text and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_SIM_GROUP_SECONDARY_COUNTRY)).click();
		
		// select group
		if (group!=null) {
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// insert priority (Enter a whole number.)
		if (priority!=null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}

		// select geo location
		if (geoLocation!=null) {
			persistUtil.selectByVisibleText(ID_GEO_LOCATION, geoLocation);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify and update the Sim group secondary country details by previous/old
	 * name
	 * 
	 * @param group
	 * @param geoLocation
	 * @param priority
	 * @return String result message
	 * @
	 */
	public String modifyByGroupAndGeoLocation(String group, String geoLocation,
			String newGroup, String priority,String newGeoLocation)  {
		// go to sim group secondary country page
		select.selectInventorySimGroupSecondaryCountries();

		// find the sim group secondary country by group and geo location
		findByGroupAndGeoLocation(group, geoLocation);

		// select group
		if (newGroup!=null) {
			persistUtil.selectByVisibleText(ID_GROUP, newGroup);
		}

		// insert priority (Enter a whole number.)
		if (priority!=null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}

		// select geo location
		if (newGeoLocation!=null) {
			persistUtil.selectByVisibleText(ID_GEO_LOCATION, newGeoLocation);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete Sim group secondary country by group and geolocation
	 * 
	 * @param group
	 * @param geoLocation
	 * @return String result message
	 * @
	 */
	public String deleteByGroupAndGeoLocation(String group, String geoLocation)
			 {
		// go to sim group secondary country page
		select.selectInventorySimGroupSecondaryCountries();

		// find and delete
		findByGroupAndGeoLocation(group, geoLocation);
		try {
			driver.findElement(By.linkText("Delete")).click();
			String xpathImSure = "//input[@value=" + "\"Yes, I'm sure\"" + "]";
			driver.findElement(By.xpath(xpathImSure)).click();
		} catch (Exception e) {
			String errorMessage = "Couldn't Delete " + group + " and " + geoLocation + " Item";
			System.err.println(errorMessage);
			e.printStackTrace();
		}

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * find element by group and geo location and click on it
	 * 
	 * @param group
	 * @param geoLocation
	 */
	public void findByGroupAndGeoLocation(String group, String geoLocation) {
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements; i++) {
			try {
				// check if group exist in the same tr
				String xpathCheckGroup = "//tr[" + i + "]/th/a[text()='"
						+ group + "']";
				// check if geo location exist in this 'tr'
				String xpathCheckGeoLocation = "//tr[" + i
						+ "]/td[2]/select/option[text()='" + geoLocation + "']";
				if ((driver.findElement(By.xpath(xpathCheckGroup)) != null)
						&& (driver.findElement(By.xpath(xpathCheckGeoLocation)) != null)) {
					driver.findElement(By.xpath(xpathCheckGroup)).click();
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		unitTesting();
//System.out.println(new SimGroupSecondaryCountries().deleteAll());
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		SimGroupSecondaryCountries s = new SimGroupSecondaryCountries();
		String group = "Unassigned";
		String priority = "1";
		String geoLocation = "Argentina (722)";

		System.out.println(s.add(group, priority, geoLocation));

		System.out.println(s.modifyByGroupAndGeoLocation(group, geoLocation, group, priority, geoLocation));

		System.out.println(s.deleteByGroupAndGeoLocation(group, geoLocation));

		s.driver.close();
	}
	/**
	 * Delete All sim group secondary countries
	 */
	public String deleteAll(){
		select.selectInventorySimGroupSecondaryCountries();
		return persistUtil.deleteAllFromTablePage();	
	}
}
