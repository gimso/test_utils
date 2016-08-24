package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.PersistPageSelect;
/**
 * This class is Added Modified and Delete Geolocation International Carrier in
 * Geolocation International Carrier page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class GeoLocationInternaionalCarriers {
	private static final String ID_GEOLOCATION = "id_geolocation";
	private static final String ID_INTL_CARRIER = "id_intl_carrier";
	private static final String ID_PRIORITY = "id_priority";
	private static final String LINK_TEXT_ADD_GEO_LOCATION_INTERNATIONAL_CARRIER = "Add geo location international carrier";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public GeoLocationInternaionalCarriers() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add Geolocation International Carrier with all parameters available
	 * 
	 * @param geolocation
	 * @param internationalCarrier
	 * @param priority
	 * @return String result message
	 * @
	 */
	public String add(String geolocation, String internationalCarrier, String priority)  {
		
		// go to Geolocation International Carrier page
		select.selectInventoryGeoLocationIntlCarriers();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_GEO_LOCATION_INTERNATIONAL_CARRIER)).click();

		// select geolocation
		if (geolocation != null) {
			persistUtil.selectByVisibleText(ID_GEOLOCATION, geolocation);
		}

		// select international carrier
		if (internationalCarrier != null) {
			persistUtil.selectByVisibleText(ID_INTL_CARRIER, internationalCarrier);
		}
		
		// insert priority
		if (priority!=null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add Geolocation International Carrier with the requires parameters only,
	 * the rest will fill with default values
	 * 
	 * @param geolocation
	 * @param internationalCarrier
	 * @return String result message
	 * @
	 */
	public String add(String geolocation, String internationalCarrier)
			 {
		return add(geolocation, internationalCarrier, null);
	}

	/**
	 * Modify and update the Geolocation International Carrier details by
	 * previous geolocation
	 * 
	 * @param geolocation
	 * @param newGeolocation
	 * @param internationalCarrier
	 * @param priority
	 * @return String result message
	 * @
	 */
	public String modifyByGeolocation(String geolocation,
			String newGeolocation, String internationalCarrier, String priority)
			 {
		// go to Geolocation International Carrier page
		select.selectInventoryGeoLocationIntlCarriers();

		// find the Geolocation International Carrier to modify and click on it
		driver.findElement(By.linkText(geolocation)).click();

		// select geolocation
		if (geolocation != null) {
			persistUtil.selectByVisibleText(ID_GEOLOCATION, geolocation);
		}

		// select international carrier
		if (internationalCarrier != null) {
			persistUtil.selectByVisibleText(ID_INTL_CARRIER, internationalCarrier);
		}
		
		// insert priority
		if (priority!=null) {
			driver.findElement(By.id(ID_PRIORITY)).clear();
			driver.findElement(By.id(ID_PRIORITY)).sendKeys(priority);
		}

				// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete Geolocation International Carrier by geolocation
	 * 
	 * @param geolocation
	 * @return String result message
	 * @
	 */
	public String deleteByGeolocation(String geolocation){
		// go to Geolocation International Carrier page
		select.selectInventoryGeoLocationIntlCarriers();

		// delete
		persistUtil.deleteByLinkTextName(geolocation);

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	public static void main(String[] args) {
		System.out.println(new GeoLocationInternaionalCarriers().deleteAll());
		//unitTesting();
	}
	
	public static void unitTesting() {
		String geolocation = "Switzerland (228)", newGeolocation = "Sweden (240)", internationalCarrier = "0",
				priority = "2";
		GeoLocationInternaionalCarriers glil = new GeoLocationInternaionalCarriers();

		// ADD MAXIMUM
		System.out.println(glil.add(geolocation, internationalCarrier, priority));

		// ADD MINIMUM
		System.out.println(glil.add(geolocation, internationalCarrier));

		// MODIFY
		System.out.println(glil.modifyByGeolocation(geolocation, newGeolocation, internationalCarrier, priority));

		// DELETE
		System.out.println(glil.deleteByGeolocation(newGeolocation));

		glil.driver.close();
	}
	
	/**
	 * Delete geo location international carriers
	 */
	public String deleteAll(){
		select.selectInventoryGeoLocationIntlCarriers();
		return persistUtil.deleteAllFromTablePage();	
	}	
}