package persist.usage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

/**
 * This class is Added Modified and Delete Access Controls under
 * persist.usage.AccessControls page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class AccessControls {
	private static final String LINK_TEXT_ADD_ACCESS_CONTROL = "Add access control";
	private static final String ID_CIDR = "id_cidr";
	private static final String ID_TYPE = "id_type";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public AccessControls() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add Access Control with all parameters available
	 * 
	 * @param cidr
	 * @param type
	 * @return String result message
	 * @
	 */
	public String add(String cidr, String type)  {
		// go to access control type page
		select.selectUsageAccessControls();

		// find add access control link text and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_ACCESS_CONTROL)).click();

		// insert cidr
		if (cidr != null) {
			driver.findElement(By.id(ID_CIDR)).clear();
			driver.findElement(By.id(ID_CIDR)).sendKeys(cidr);
		}

		// select type
		if (type != null) {
			persistUtil.selectByVisibleText(ID_TYPE, type);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add Access Control with the requires parameters only, the rest will fill
	 * with default values
	 * 
	 * @param cidr
	 * @return String result message
	 * @
	 */
	public String add(String cidr)  {
		return add(cidr, null);
	}

	/**
	 * Modify and update the Access Control details by previous/old name
	 * 
	 * @param cidr
	 * @param newCidr
	 * @param type
	 * @return String result message
	 * @
	 */
	public String modifyByCidr(String cidr, String newCidr, String type)
			 {
		// go to access control type page
		select.selectUsageAccessControls();

		// find the access control by cidr and click on it
		driver.findElement(By.linkText(cidr)).click();

		// insert cidr
		if (cidr!=null) {
			driver.findElement(By.id(ID_CIDR)).clear();
			driver.findElement(By.id(ID_CIDR)).sendKeys(newCidr);
		}

		// select type
		if (type!=null) {
			persistUtil.selectByVisibleText(ID_TYPE, type);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete Access Control by Cidr
	 * 
	 * @param cidr
	 * @return String result message
	 * @
	 */
	public String deleteByCidr(String cidr)  {
		// go to access control type page
		select.selectUsageAccessControls();

		// find the cidr by name and delete it
		persistUtil.deleteByLinkTextName(cidr);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new AccessControls().deleteAll());//unitTesting();

	}

	/**
	 * 
	 */
	public static void unitTesting() {
		AccessControls accessControls = new AccessControls();
		String cidr = "test";
		String newCidr = "test2";
		String type = "Dyno Gatekeeper";
		String cidr2 = "cidr2";
		
			System.out.println(accessControls.add(cidr, type));
		
			System.out.println(accessControls.add(cidr2));
		
			System.out
					.println(accessControls.modifyByCidr(cidr, newCidr, type));
		
			System.out.println(accessControls.deleteByCidr(newCidr) + "\n"
					+ accessControls.deleteByCidr(cidr2));
		
		accessControls.driver.close();
	}
	
	/**
	 * Delete All access controls
	 */
	public String deleteAll(){
		select.selectUsageAccessControls();
		return persistUtil.deleteAllFromTablePage();	
	}
}