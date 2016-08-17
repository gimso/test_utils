package selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import global.PropertiesUtil;

/**
 * This class uploads a Chrome driver, performs login to Simgo vSIM web page 
 * @author Dana
 *
 */

public class SimDriverUtil implements DriverUtil{
	
	private WebDriver driver;
	private SimPageSelect select;

	private static volatile SimDriverUtil instance = null;
	
	public static synchronized SimDriverUtil getInstance(){
		if (instance == null) {
			instance = new SimDriverUtil();
		}
		return instance;
	}
	

	public SimDriverUtil() {
		// Fire up FireFox browser
		this.driver = new FirefoxDriver();
		this.select = new SimPageSelect(driver);

		// Login to Simgo vSIM home page
		driver.get(PropertiesUtil.getInstance().getProperty("SIMGO_VSIM_GUI"));

		// if element can't be found (because of Internet speed etc), wait another 10 seconds without throwing exception
		WebDriverWait wait = new WebDriverWait(driver, 10);
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

	/**
	 * set WebDriver
	 */
	@Override
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	public SimPageSelect getSelect() {
		return select;
	}


	public void setSelect(SimPageSelect select) {
		this.select = select;
	}

}
