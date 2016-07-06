package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;
/**
* This class is Added Modified and Delete Geo Location in
* Geo Location page
* 
* @author Yehuda Ginsburg
*
*/
public class GeoLocations {
	private static final String ID_BILLING_ZONE = "id_billing_zone";
	private static final String ID_JUNK_NUMBER = "id_junk_number";
	private static final String ID_COUNTRY_CODE = "id_country_code";
	private static final String ID_MCC = "id_mcc";
	private static final String LINK_TEXT_ADD_GEO_LOCATION = "Add geo location";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public GeoLocations() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add Geo Location with all parameters available
	 * @param mcc
	 * @param name
	 * @param countryCode
	 * @param junkNumber
	 * @param billingZone
	 * @return String result message
	 * @
	 */
	public String add(String mcc, String name, String countryCode,
			String junkNumber, String billingZone)  {

		// go to geo location page
		select.selectInventoryGeoLocations();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_GEO_LOCATION)).click();

		// insert mcc
		if (mcc != null) {
			driver.findElement(By.id(ID_MCC)).clear();
			driver.findElement(By.id(ID_MCC)).sendKeys(mcc);
		}
		
		// insert Name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		// insert Country code
		if (countryCode != null) {
			driver.findElement(By.id(ID_COUNTRY_CODE)).clear();
			driver.findElement(By.id(ID_COUNTRY_CODE)).sendKeys(countryCode);
		}
		
		// insert Junk number
		if (junkNumber != null) {
			driver.findElement(By.id(ID_JUNK_NUMBER)).clear();
			driver.findElement(By.id(ID_JUNK_NUMBER)).sendKeys(junkNumber);
		}
		
		// select billing zone
		if (billingZone != null) {
			persistUtil.selectByVisibleText(ID_BILLING_ZONE, billingZone);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add Geo Location with the requires parameters only, the rest will fill with default values
	 * @param mcc
	 * @param name
	 * @param countryCode
	 * @param junkNumber
	 * @return String result message
	 * @
	 */
	public String add(String mcc, String name, String countryCode,
			String junkNumber)  {
		return add(mcc, name, countryCode, junkNumber, null);
	}

	/**
	 * Modify and update the Geo Location details by previous Mcc
	 * @param mcc to find by - mcc is not changeable
	 * @param name
	 * @param countryCode
	 * @param junkNumber
	 * @param billingZone
	 * @return String result message
	 * @
	 */
	public String modifyByMcc(String mcc, String name, String countryCode,
			String junkNumber, String billingZone)  {
		// go to geo location page
		select.selectInventoryGeoLocations();

		// find the geo location by mcc and click on it
		driver.findElement(By.linkText(mcc)).click();

		// cannot change the mcc itself
		// insert Name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}

		// insert Country code
		if (countryCode != null) {
			driver.findElement(By.id(ID_COUNTRY_CODE)).clear();
			driver.findElement(By.id(ID_COUNTRY_CODE)).sendKeys(countryCode);
		}

		// insert Junk number
		if (junkNumber != null) {
			driver.findElement(By.id(ID_JUNK_NUMBER)).clear();
			driver.findElement(By.id(ID_JUNK_NUMBER)).sendKeys(junkNumber);
		}

		// select billing zone
		if (billingZone != null) {
			persistUtil.selectByVisibleText(ID_BILLING_ZONE, billingZone);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete Geo Location by Mcc
	 * @param mcc
	 * @return String result message
	 * @
	 */
	public String deleteByMcc(String mcc)  {
		// go to geo location page
		select.selectInventoryGeoLocations();

		// find and delete
		persistUtil.deleteByLinkTextName(mcc);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new GeoLocations().deleteAll());
//		unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		GeoLocations geoLocations = new GeoLocations();
		String mcc = "111", name = "test", countryCode = "111", junkNumber = "111", billingZone = "3";
		// ADD MAXIMUM
		System.out.println(geoLocations.add(mcc, name, countryCode, junkNumber, billingZone));

		// ADD MINIMUM
		System.out.println(geoLocations.add(mcc, name, countryCode, junkNumber));

		// MODIFY
		System.out.println(geoLocations.modifyByMcc(mcc, name, countryCode, junkNumber, billingZone));

		// DELETE
		System.out.println(geoLocations.deleteByMcc(mcc));

		geoLocations.driver.close();
	}
	
	/**
	 * Delete geo locations
	 */
	public String deleteAll(){
		select.selectInventoryGeoLocations();
		return persistUtil.deleteAllFromTablePage();	
	}
}