package global;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testing_utils.TestOutput;

/**
 * this class performs login to cloud (user&password from Properties.json)
 * return initialized Properties bean, WebDriver instance, SelectPage instance.
 * 
 * @author Yehuda
 *
 */
public class PersistUtil {

	public static final String USIM = "USIM";
	public static final String SIM = "SIM";

	private WebDriver driver;
	private SelectPage select;

	private static volatile PersistUtil instance = null;

	/**
	 * Use Singleton pattern to avoid multiple windows and WebDriver object -
	 * using one for all class
	 * 
	 * @return
	 */
	public static synchronized PersistUtil getInstance() {
		if (instance == null) {
			instance = new PersistUtil();
		}
		return instance;
	}

	/**
	 * initializing properties,driver,select and db connection
	 */
	private PersistUtil() {

	
		// Fire up FireFox browser
		this.driver = new FirefoxDriver();
		this.select = new SelectPage(driver);

		// Login to persist, take the version of the cloud and update the XML
		// for the TestRail application
		driver.get(PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_INVENTORY"));

		// if couldn't find the element (because of Internet speed etc), it will
		// wait another 10 seconds without throwing exception
		WebDriverWait wait = new WebDriverWait(driver, 10);// 1 minute
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_username")));

		driver.findElement(By.id("id_username")).clear();
		driver.findElement(By.id("id_username")).sendKeys(PropertiesUtil.getInstance().getProperty("USER"));
		driver.findElement(By.id("id_password")).clear();
		driver.findElement(By.id("id_password")).sendKeys(PropertiesUtil.getInstance().getProperty("PASSWORD"));
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		// Wait 5 seconds for timeout
		this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}

	/**
	 * Select item found elementId and click by visible text, Surrounded with
	 * try and catch if element is not there.
	 * 
	 * @param visibleText
	 * @param elementId
	 * @throws PersistException
	 */
	public void selectByVisibleText(String elementId, String visibleText) {
		try {
			new Select(driver.findElement(By.id(elementId))).selectByVisibleText(visibleText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete in the global page (where all elements in table) A global method
	 * that after checking (selecting) an table columns id delete them
	 */
	public void deleteSelectedItemsFromTable() {
		new Select(driver.findElement(By.name("action"))).selectByVisibleText("Delete selected");
		driver.findElement(By.name("index")).click();
	}

	/**
	 * delete an item when its on specific element page
	 * 
	 * @throws PersistException
	 */
	public void deleteSelectedItemsFromElementPage() {
		try {
			driver.findElement(By.linkText("Delete")).click();
			String xpathImSure = "//input[@value=" + "\"Yes, I'm sure\"" + "]";
			driver.findElement(By.xpath(xpathImSure)).click();
		} catch (Exception e) {
			System.err.println("Couldn't Delete selected Item");
			e.printStackTrace();
		}
	}

	/**
	 * Delete inside component page (where all the details of items been)
	 * 
	 * @param name
	 * @throws PersistException
	 */
	public void deleteByLinkTextName(String name) {
		try {
			driver.findElement(By.linkText(name)).click();
			deleteSelectedItemsFromElementPage();
		} catch (Exception e) {
			String errorMessage = "Couldn't Delete " + name + " Item";
			System.err.println(errorMessage);
			e.printStackTrace();
		}
	}

	// *******************************
	// Error Checking
	// ********************************

	/**
	 * A global method that check for all Error messages in the page if exist
	 * 
	 * @param message
	 * @return TestOutput
	 */
	public TestOutput checkErrors(String message) {
		try {
			if ((driver.findElement(By.className("errornote"))) != null) {
				List<WebElement> elements = driver.findElements(By.xpath("//ul[@class='errorlist']/li"));
				String errorMessage = " - ";
				for (WebElement w : elements) {
					errorMessage = w.getText() + "\n";
				}

				System.err.println(message + " ," + errorMessage);
				return new TestOutput(false, message + " ," + errorMessage);
			}
		} catch (Exception e) {}

		return new TestOutput(true, "PASS");
	}

	/**
	 * Updating the TestOutput according to error's on the page if they exist
	 * 
	 * @param output
	 * @param pass
	 * @return TestOutput
	 */
	public TestOutput finalCheck(String output, boolean pass) {
		if (!checkErrors(output).getOutput().equalsIgnoreCase("Pass")) {
			output = checkErrors(output).getOutput();
			pass = false;
		} else {
			output = driver.findElement(By.className("grp-info")).getText();
			pass = true;
		}
		return new TestOutput(pass, output);
	}

	public String finalCheck() {
		String errorMessage = null;
		String outputMessage = null;
		try {
			if ((driver.findElement(By.className("errornote"))) != null) {
				List<WebElement> elements = driver.findElements(By.xpath("//ul[@class='errorlist']/li"));
				errorMessage = " - ";
				for (WebElement w : elements) {
					errorMessage += "\n" + w.getText();
				}
				return errorMessage;
			}
		} catch (Exception e) {
			/* if didn't find errorClass do Nothing */}
		try {
			if (driver.findElement(By.className("grp-info")) != null) {
				outputMessage = driver.findElement(By.className("grp-info")).getText();
			}
		} catch (Exception e) {
			/* if didn't find grpInfo do Nothing */}
		return outputMessage != null ? outputMessage : "";

	}

	/**
	 * 
	 * @return WebDriver instance
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * 
	 * @return SelectPage instance
	 */
	public SelectPage getSelect() {
		return select;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setSelect(SelectPage select) {
		this.select = select;
	}

	/**
	 * must go to the page of the table </br>
	 * Delete all page in generic way</br>
	 * for every combination of delete like "delete all selected items" and
	 * "delete fw versions" etc.</br>
	 * and even if ask "are you sure"
	 */
	public String deleteAllFromTablePage() {
		String selectAll = "//tr/th[1]/div/span/input";
		boolean exist = false;
		try {
			if (driver.findElement(By.xpath(selectAll)) != null) {
				exist = true;
				driver.findElement(By.xpath(selectAll)).click();
				Select selectOption = new Select(driver.findElement(By.name("action")));
				for (WebElement element : selectOption.getOptions()) {
					String textContent = element.getText().toLowerCase();
					if (textContent.contains("delete".toLowerCase())) {
						selectOption.selectByVisibleText(element.getText());
						break;
					}
				}
				String xpathImSure = "//input[@value=\"Yes, I'm sure\"]";
				if (driver.findElement(By.xpath(xpathImSure)) != null) {
					driver.findElement(By.xpath(xpathImSure)).click();
				}
			}
		} catch (Exception e) {/*if cannot find elements skip*/}
		
		if (exist) {
			return finalCheck();
		}
		return "The table is already empty";
	}

	/**
	 * Insert URL query
	 * @param element
	 */
	public void searchForElement(String element) {
		driver.get(driver.getCurrentUrl() + "?q=" + element);
	}
	
	/**
	 * Search all elements of headers by name and get the column index
	 * @param coulmnName
	 * @return int index
	 */
	public int getColumnIndexByHeaderName(String coulmnName) {

		int coulmnIndex = -1;
		List<WebElement> findElements = driver.findElements(By.xpath("//table/thead/tr/th"));
		int i = 0;
		for (WebElement element : findElements) {
			String text = element.getText();
			if (text.equals(coulmnName)) {
				coulmnIndex = i;
				break;
			}
			i++;
		}
		return coulmnIndex;

	}
	
	/**
	 * find the element by id, find if its not already checked, click if it is
	 * checked and need to uncheck, click also if it is unchecked and need to
	 * check.
	 * 
	 * @param id
	 * 
	 * @param check
	 */
	public void clickCheckIfNeeded(String id, Boolean check) {
		WebElement allowedElement = driver.findElement(By.id(id));
		boolean isChecked = allowedElement.isSelected();
		if ((!isChecked && check) || (isChecked && !check)) {
			allowedElement.click();
		}
	}

}