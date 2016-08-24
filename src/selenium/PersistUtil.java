package selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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
	private PersistPageSelect select;

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
	 * Get Persist URL
	 */
	@Override
	public String getUrl() {
	return PropertiesUtil.getInstance().getProperty("EC2_PERSIST_URL_INVENTORY");
	}
	
	/**
	 * initializing properties,driver,select and db connection
	 * @param path
	 */
	private PersistUtil() {

		// Fire up FireFox browser
		this.driver = new FirefoxDriver();
		this.select = new PersistPageSelect(driver);

		// Login to persist, take the version of the cloud and update the XML
		// for the TestRail application
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
	
	@Override
	public void setDriver(WebDriver driver) {
		this.driver = driver;		
	}

	/**
	 * @return SelectPage instance
	 */
	public PersistPageSelect getSelect() {
		return select;
	}

	public void setSelect(PersistPageSelect select) {
		this.select = select;
	}
}