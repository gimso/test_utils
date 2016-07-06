package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

/**
 * This class is Added Modified and Delete docker type in docker type page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class DockerTypes {
	private static final String LINK_TEXT_ADD_DOCKER_TYPE = "Add docker type";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public DockerTypes() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add docker type with all parameters available
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String add(String name)  {
		// go to docker type page
		select.selectInventoryDockerTypes();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_DOCKER_TYPE)).click();

		// insert name
		driver.findElement(By.id(ID_NAME)).clear();
		driver.findElement(By.id(ID_NAME)).sendKeys(name);

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify and update the docker type details by previous docker type name
	 * 
	 * @param name
	 * @param newName
	 * @return String result message
	 * @
	 */
	public String modifyByName(String name, String newName)
			 {
		// go to docker type page
		select.selectInventoryDockerTypes();

		// find the docker type name to modify and click on it
		driver.findElement(By.linkText(name)).click();

		// insert new name
		driver.findElement(By.id(ID_NAME)).clear();
		driver.findElement(By.id(ID_NAME)).sendKeys(newName);

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete docker type by docker type name
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByName(String name)  {
		// go to docker type page
		select.selectInventoryDockerTypes();

		// delete by name
		persistUtil.deleteByLinkTextName(name);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		unitTesting();
	}

	public static void unitTesting() {

		DockerTypes dockerTypes = new DockerTypes();

		String name = "TestType";
		String newName = "Test Type";

		// ADD
		try {
			System.out.println(dockerTypes.add(name));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// MODIFY
		try {
			System.out.println(dockerTypes.modifyByName(name, newName));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// DELETE
		try {
			System.out.println(dockerTypes.deleteByName(newName));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		dockerTypes.driver.close();
	}
}
