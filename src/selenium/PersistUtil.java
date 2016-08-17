package selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import global.PropertiesUtil;

/**
 * this class performs login to cloud (user&password from Properties.json)
 * return initialized Properties bean, WebDriver instance, SelectPage instance.
 * 
 * @author Yehuda
 *
 */
public class PersistUtil implements DriverUtil{

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
	 * @param path
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
	 * @return WebDriver instance
	 */
	@Override
	public WebDriver getDriver() {
		return driver;
	}
	
	@Override
	public void setDriver(WebDriver driver) {
		this.driver = driver;		
	}

	/**
	 * @return SelectPage instance
	 */
	public SelectPage getSelect() {
		return select;
	}

	public void setSelect(SelectPage select) {
		this.select = select;
	}
}