package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

/**
 * This class is Added Modified and Delete docker in docker page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Dockers {
	private static final String ID_TYPE = "id_type";
	private static final String ID_ADDRESS = "id_address";
	private static final String ID_PORT = "id_port";
	private static final String LINK_TEXT_ADD_DOCKER = "Add docker";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Dockers() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add docker with all parameters available
	 * 
	 * @param type
	 * @param address
	 * @param port
	 * @return String result message
	 * @
	 */
	public String add(String type, String address, String port)
			 {
		// go to docker page
		select.selectInventoryDockers();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_DOCKER)).click();

		// select type
		persistUtil.selectByVisibleText(ID_TYPE, type);

		// insert address
		driver.findElement(By.id(ID_ADDRESS)).clear();
		driver.findElement(By.id(ID_ADDRESS)).sendKeys(address);

		// insert port
		driver.findElement(By.id(ID_PORT)).clear();
		driver.findElement(By.id(ID_PORT)).sendKeys(port);

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify and update the docker details by previous address and port
	 * 
	 * @param address
	 * @param type
	 * @param newAddress
	 * @param port
	 * @return String result message
	 * @
	 */
	public String modifyByAddressAndPort(String address, String port,String type,
			String newAddress, String newPort)  {
		// go to docker page
		select.selectInventoryDockers();

		// find the docker to modify and click on it
		driver.findElement(By.linkText(address+":"+port)).click();

		// select type
		persistUtil.selectByVisibleText(ID_TYPE, type);

		// insert address
		driver.findElement(By.id(ID_ADDRESS)).clear();
		driver.findElement(By.id(ID_ADDRESS)).sendKeys(newAddress);

		// insert port
		driver.findElement(By.id(ID_PORT)).clear();
		driver.findElement(By.id(ID_PORT)).sendKeys(newPort);

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete docker by address and port
	 * 
	 * @param address
	 * @param port 
	 * @return String result message
	 * @
	 */
	public String deleteByAddress(String address, String port)  {
		// go to docker page
		select.selectInventoryDockers();

		// delete
		persistUtil.deleteByLinkTextName(address+":"+port);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		unitTesting();
	}

	public static void unitTesting() {

		Dockers dockers = new Dockers();
		String type = "DRM";
		String address = "1.1.1.1";
		String port = "0";
		String newPort = "1";
		String newAddress = "2.22.2.2";

		// ADD
		try {
			System.out.println(
					dockers.add(type, address, port)
					);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// MODIFY
		try {
			System.out.println(
					dockers.modifyByAddressAndPort(address, port, type, newAddress, newPort)
					);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// DELETE
		try {
			System.out.println(
					dockers.deleteByAddress(newAddress, newPort)
					);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		dockers.driver.close();
	}

}
