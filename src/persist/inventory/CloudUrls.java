package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

/**
 * This class is Added Modified and Delete cloud url in cloud url page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class CloudUrls {
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public CloudUrls() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add cloud url with all parameters available
	 * 
	 * @param cloudName
	 * @param cloudUrl
	 * @return String result message
	 * @
	 */
	public String add(String cloudName, String cloudUrl)
			 {
		// go to cloud url page
		select.selectInventoryCloudUrls();

		// find the add button and click on it
		driver.findElement(By.linkText("Add cloud url")).click();

		// add cloud name		
		if (cloudName!=null) {
			driver.findElement(By.id("id_cloud_name")).clear();
			driver.findElement(By.id("id_cloud_name")).sendKeys(cloudName);
		}
		
		// add cloud url		
		if (cloudUrl!=null) {
			driver.findElement(By.id("id_cloud_url")).clear();
			driver.findElement(By.id("id_cloud_url")).sendKeys(cloudUrl);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Modify and update the cloud url details by name
	 * 
	 * @param cloudName
	 * @param cludNewName
	 * @param cloudUrl
	 * @return String result message
	 * @
	 */
	public String modifyByName(String cloudName, String cludNewName,
			String cloudUrl)  {
		// go to cloud url page
		select.selectInventoryCloudUrls();

		// find the cloud url to modify and click on it
		driver.findElement(By.linkText(cloudName)).click();

		// modify cloud name
		if (cludNewName != null) {
			driver.findElement(By.id("id_cloud_name")).clear();
			driver.findElement(By.id("id_cloud_name")).sendKeys(cludNewName);
		}
		
		// modify cloud url
		if (cloudUrl != null) {
			driver.findElement(By.id("id_cloud_url")).clear();
			driver.findElement(By.id("id_cloud_url")).sendKeys(cloudUrl);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete cloud url by name
	 * 
	 * @param cloudName
	 * @return String result message
	 * @
	 */
	public String deleteByName(String cloudName)  {
		// go to cloud url page
		select.selectInventoryCloudUrls();

		// delete
		persistUtil.deleteByLinkTextName(cloudName);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		CloudUrls cloudUrls = new CloudUrls();
		System.out.println(cloudUrls.deleteAll());
		// unitTesting();
	}

	public static void unitTesting() {

		CloudUrls cloudUrls = new CloudUrls();

		// ADD
		String cloudName = "cloudUrlsName";
		String cloudUrl = "http://www.cloudUrlsName.com";
		String cludNewName = "cloudUrlsNewName";
		try {
			System.out.println(cloudUrls.add(cloudName, cloudUrl));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// MODIFY
		try {
			System.out.println(cloudUrls.modifyByName(cloudName, cludNewName,
					cloudUrl));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// DELETE
		try {
			System.out.println(cloudUrls.deleteByName(cludNewName));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		cloudUrls.driver.close();
	}

	/**
	 * looks into html body tag and returns JSON value as string
	 * 
	 * @param url
	 * @return
	 */
	public String getJsonFromUrl(String url) {

		driver.get(url);
		return driver.findElement(By.tagName("body")).getText();
	}

	/**
	 * Delete cloud urls
	 */
	public String deleteAll() {
		select.selectInventoryCloudUrls();
		return persistUtil.deleteAllFromTablePage();
	}

}
