package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.PersistPageSelect;
/**
 * This class is Modify Sip Server name under
 * persist.inventory.SipServers page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class SipServers {

	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public SipServers () {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();}

	/**
	 * Modify the name by the identifier
	 * @param identifier
	 * @param name
	 * @return String result message
	 * @
	 */
	public String modifyByIdentifier(String identifier, String name)  {
		// go to sip server page
		select.selectInventorySipServers();

		// find the  sip server add button and click on it
		driver.findElement(By.linkText(identifier)).click();
		
		// insert name	
		if (name!=null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	public static void main(String[] args) {
		unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		SipServers ss = new SipServers();
		String identifier = "0";
		String name = "Test";

		
			System.out.println(ss.modifyByIdentifier(identifier, name));
		
		ss.driver.close();
	}
}