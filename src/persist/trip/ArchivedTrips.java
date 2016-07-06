package persist.trip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

/**
 * 
 * @author Yehuda Ginsburg
 *
 */
public class ArchivedTrips {
	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public ArchivedTrips() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * check if an archived trip found in this page by plug id and date
	 * 
	 * @param String
	 *            plugId
	 * @param Date
	 *            date
	 * @return boolean
	 */
	public boolean findArchivedTripByPlugIdAndDate(String plugId, Date date) {
		// go to ArchivedTrips page
		select.selectTripArchivedTrips();

		// narrow the search using plug id in url query
		persistUtil.searchForElement(plugId);

		// get the date and convert it by String format
		SimpleDateFormat sdf = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);
		String dateString = sdf.format(date);

		// get the column number in the table by th name
		String coulmnName = "Archived at";
		int coulmnIndex = persistUtil.getColumnIndexByHeaderName(coulmnName);
		if (coulmnIndex == -1)
			return false;

		// check if find element with this plug id and date in this page
		String xpathCheck = "//tr/td[" + coulmnIndex + "][contains(text(),'" + dateString + "')]";
		// if there is an element in the table return true else return false
		return driver.findElements(By.xpath(xpathCheck)).size() > 0 ? true : false;
	}

}