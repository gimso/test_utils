package global;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Select by Clicking pages in the persist cloud
 * 
 * @author Yehuda Ginsburg
 */
public class SelectPage {
	private static final String INVENTORY_URL = PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_HOME") + "inventory/";
	private static final String TRIP_URL = 		PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_HOME") + "trip/";
	private static final String USAGE_URL = 	PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_HOME") + "usage/";

	private WebDriver driver;

	public SelectPage(WebDriver driver) {
		this.driver = driver;
	}

	// Select access number
	public void selectInventoryAccessNumbers() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Access numbers")).click();
	}

	public void selectUsagePlans() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("Plans")).click();
	}

	public void selectUsagePlugs() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("Plugs")).click();
	}

	public void selectTripTrips() {
		driver.get(TRIP_URL);
		driver.findElement(By.linkText("Trips")).click();
	}

	public void selectUsageAllocations() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("Allocations")).click();
	}

	public void selectUsageUserGroups() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("User groups")).click();
	}

	public void selectUsageUsers() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("Users")).click();
	}

	public void selectInventoryGeoLocations() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Geo locations")).click();
	}

	public void selectInventoryAccessNumbersGroups() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Access number groups")).click();
	}

	public void selectInventoryGeoLocationIntlCarriers() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Geo location international carriers")).click();
	}

	public void selectInventoryInternationalCarriers() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("International carriers")).click();
	}

	public void selectInventorySimGroupAccessNumberGroups() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sim group access number groups")).click();
	}

	public void selectTripArchivedTrips() {
		driver.get(TRIP_URL);
		driver.findElement(By.linkText("Archived trips")).click();
	}

	public void selectTripTripPlanHistories() {
		driver.get(TRIP_URL);
		driver.findElement(By.linkText("Trip plan histories")).click();
	}

	public void selectUsageAccessControls() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("Access controls")).click();
	}

	// Select Sim group secondary country's in persist
	public void selectInventorySimGroupSecondaryCountries() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sim group secondary countries")).click();
	}

	// select Apn in persist
	public void selectInventoryApns() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Apns")).click();
	}

	// select configuration setting
	public void selectInventoryConfigurations() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Configurations")).click();
	}

	// Select Dyno in persist
	public void selectInventoryDynos() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Dynos")).click();
	}

	// Select SIMGroup in persist
	public void selecInventorytSimGroups() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sim groups")).click();
	}

	// Select sims at persist
	public void selectInventorySims() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sims")).click();
	}

	// Select simUnit at persist
	public void selectInventorySimUnits() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sim units")).click();
	}

	// Select FirmwareVersion page
	public void selectInventoryFirmwareVersions() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Firmware versions")).click();
	}

	// Select Usage.PlugType page
	public void selectUsagePlugTypes() {
		driver.get(USAGE_URL);
		driver.findElement(By.linkText("Plug types")).click();
	}

	// Select Inventory.SipServer
	public void selectInventorySipServers() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sip servers")).click();
	}
	
	// select Inventory.SimGroup
	public void selectInventorySimGroups() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sim groups")).click();
	}

	// select Inventory.SimBoards
	public void selectInventorySimBoards() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Sim boards")).click();
	}

	public void selectInventoryDockerTypes() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Docker types")).click();
	}

	public void selectInventoryDockers() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Dockers")).click();
	}

	public void selectInventoryCloudUrls() {
		driver.get(INVENTORY_URL);
		driver.findElement(By.linkText("Cloud urls")).click();
	}

}