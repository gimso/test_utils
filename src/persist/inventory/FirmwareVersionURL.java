package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

/**
 * This class is Added Modified and Delete firmware version url in firmware
 * version url page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class FirmwareVersionURL {
	private static final String ID_COMMENT = "id_comment";
	private static final String ID_FW_URL = "id_fw_url";
	private static final String ID_FW_UPDATE_VERSION = "id_fw_update_version";
	private static final String ID_FW_UPDATE_CONFIG = "id_fw_update_config";
	private static final String LINK_TEXT_ADD_FIRMWARE_VERSION = "Add firmware version";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public FirmwareVersionURL() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add firmware version url with all parameters available
	 * 
	 * @param config
	 * @param version
	 * @param url
	 * @param comment
	 * @return String result message
	 * @
	 */
	public String add(String config, String version, String url, String comment)
			 {
		// go to firmware version url page
		select.selectInventoryFirmwareVersions();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_FIRMWARE_VERSION)).click();

		// select config
		if (config != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_CONFIG, config);
		}
		
		// select version
		if (version  != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_VERSION, version);
		}

		// insert url
		if (url != null) {
			driver.findElement(By.id(ID_FW_URL)).clear();
			driver.findElement(By.id(ID_FW_URL)).sendKeys(url);
		}
		
		// insert comment
		if (comment != null) {
			driver.findElement(By.id(ID_COMMENT)).clear();
			driver.findElement(By.id(ID_COMMENT)).sendKeys(comment);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Add firmware version url with the requires parameters only, the rest will
	 * fill with default values
	 * 
	 * @param config
	 * @param version
	 * @param url
	 * @param comment
	 * @return String result message
	 * @
	 */
	public String add(String config, String version, String url)
			 {
		return add(config, version, url, null);
	}

	/**
	 * Modify and update the firmware version url details by previous URL
	 * 
	 * @param config
	 * @param version
	 * @param url
	 * @param newUrl
	 * @param comment
	 * @return String result message
	 * @
	 */
	public String modifyByUrl(String url, String config, String version,
			String newUrl, String comment)  {
		// go to firmware version url page
		select.selectInventoryFirmwareVersions();

		// find the firmware by url to modify and click on it
		findUrlAndClick(url);

		// select config
		if (config != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_CONFIG, config);
		}

		// select version
		if (version != null) {
			persistUtil.selectByVisibleText(ID_FW_UPDATE_VERSION, version);
		}

		// insert url
		if (url != null) {
			driver.findElement(By.id(ID_FW_URL)).clear();
			driver.findElement(By.id(ID_FW_URL)).sendKeys(url);
		}

		// insert comment
		if (comment != null) {
			driver.findElement(By.id(ID_COMMENT)).clear();
			driver.findElement(By.id(ID_COMMENT)).sendKeys(comment);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete firmware version url by URL
	 * 
	 * @param url
	 * @return String result message
	 * @
	 */
	public String deleteByUrl(String url)  {
		// go to firmware version url page
		select.selectInventoryFirmwareVersions();

		// delete by url
		findUrlAndClick(url);
		try {
			driver.findElement(By.linkText("Delete")).click();
			String xpathImSure = "//input[@value=" + "\"Yes, I'm sure\"" + "]";
			driver.findElement(By.xpath(xpathImSure)).click();
		} catch (Exception e) {
			System.err.println("Couldn't Delete " + url + " Item");
			e.printStackTrace();
		}

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	// url ftp_simgo:SimgoFiles@o.qa.gimso.net/ftp_simgo/test_ftp_02.sgo

	/**
	 * Find by url in firmwareVersion page
	 * 
	 * @param url
	 * @
	 */
	private void findUrlAndClick(String url)  {
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements + 1; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td[3][text()= \"" + url
						+ "\"]";
				String xpathClick = "//tr[" + i + "]/th/a";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					return;

				}
			} catch (Exception e) {
			}
		}
		System.err.println(url + " url didn't found");
	}

	public static void main(String[] args) {
		FirmwareVersionURL firmwareVersionURL = new FirmwareVersionURL();
		System.out.println(firmwareVersionURL.deleteAll());
		//unitTesting();
	}

	public static void unitTesting() {
		FirmwareVersionURL firmwareVersionURL = new FirmwareVersionURL();
		String url = "ftp_simgo:SimgoFiles@o.qa.gimso.net/ftp_simgo/test_ftp_02.sgo", config = "test",
				version = "0.0.0", newUrl = "ftp_simgo:SimgoFiles@o.qa.gimso.net/ftp_simgo/test_ftp_01.sgo",
				comment = "no comment";

		// ADD MAXIMUM
		System.out.println(firmwareVersionURL.add(config, version, url, comment));

		// ADD MINIMUM
		System.out.println(firmwareVersionURL.add(config, version, url));

		// MODIFY
		System.out.println(firmwareVersionURL.modifyByUrl(url, config, version, newUrl, comment));

		// DELETE
		System.out.println(firmwareVersionURL.deleteByUrl(newUrl));

		firmwareVersionURL.driver.close();
	}

	/**
	 * Delete all firmware versions url
	 */
	public String deleteAll(){
		select.selectInventoryFirmwareVersions();
		return persistUtil.deleteAllFromTablePage();	
	}

}