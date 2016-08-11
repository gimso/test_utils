package persist.distribution;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import selenium.PersistUtil;
import selenium.SelectPage;

public class Plugs {
	private static final String LINK_TEXT_ADD_PLUG_TYPE = "Add plug type";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Plugs () {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	public String add()  {
		// go to plug type page
		select.selectUsagePlugTypes();

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public String modified()  {
		// go to plug type page
		select.selectUsagePlugTypes();

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public String delete()  {
		// go to plug type page
		select.selectUsagePlugTypes();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

}
