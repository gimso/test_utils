package selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
	

	@Override
	public String getUrl() {
		return PropertiesUtil.getInstance().getProperty("SIMGO_VSIM_GUI");
	}
	
	
	public SimDriverUtil() {
		// Fire up FireFox browser
		this.driver = new FirefoxDriver();
		this.select = new SimPageSelect(driver);
		// Login to Simgo vSIM home page
		driver.get(getUrl());
		login();
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
