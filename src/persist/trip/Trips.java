package persist.trip;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;
/**
 * This class is Added and Delete Trip (there is no way to modify from the gui) under
 * persist.trip.Trip page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Trips {
	private static final String LINK_TEXT_ADD_TRIP = "Add trip";
	private static final String ID_USER = "id_user";
	private static final String ID_PLUG = "id_plug";
	private static final String ID_INCOMING_ACCESS_NUMBER = "id_incoming_access_number";
	private static final String NAME_SAVE = "_save";
	
	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Trips() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}
	
	/**
	 * Add trip with all parameters
	 * @param user
	 * @param plug
	 * @param incomingAccessNumber
	 * @return String result message
	 * @
	 */
	public String add(
			String user,
			String plug,
			String incomingAccessNumber
			){
		// go to trip page
		select.selectTripTrips();
		
		// find add trip link text and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_TRIP)).click();
				
		// select user
		if (user != null ) {
			persistUtil.selectByVisibleText(ID_USER, user);
		}
		
		// select plug
		if (plug != null ) {
			persistUtil.selectByVisibleText(ID_PLUG, plug);
		}

		// select incoming access number
		if (incomingAccessNumber != null ) {
			persistUtil.selectByVisibleText(ID_INCOMING_ACCESS_NUMBER, incomingAccessNumber);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Delete trip by user name
	 * @param user
	 * @return String result message
	 * @
	 */
	public String deleteByUser(String user){
		// go to trip page
		select.selectTripTrips();
		//search for user
		persistUtil.searchForElement(user);
		// find the trip by user name and delete it
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td[2][text()='" + user
						+ "']";
				String xpathClick = "//tr[" + i + "]/td[1]/input";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					i = sizeOfElements;
					persistUtil.deleteSelectedItemsFromTable();
					break;
				}
			} catch (Exception e) {
			}
		}
		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Delete trip by plug id
	 * @param plugId
	 * @ 
	 */
	public String deleteByPlugID(String plugId)  {
		// go to trip page
		select.selectTripTrips();
		
		//search for plug id
		persistUtil.searchForElement(plugId);
		
		//find the trip by plug id and delete it 	
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td[text()='" + plugId + "']";
				String xpathClick = "//tr[" + i + "]/td[1]/input";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					i = sizeOfElements;
					persistUtil.deleteSelectedItemsFromTable();
					break;
				}
			} catch (Exception e) {
			}
		}
		return persistUtil.finalCheck();
	}
	
	public static void main(String[] args) {
		System.out.println(new Trips().deleteAll());
//		unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		Trips trips = new Trips();
		String user = "user-group-IL-plan-00-0-2";
		String plug = "000000042002";
		String incomingAccessNumber = "97221234011";

		System.out.println(trips.add(user, plug, incomingAccessNumber));

		System.out.println(trips.deleteByUser(user));

		trips.driver.close();
	}
	
	/**
	 * Check in the trip page the plug id exist - 
	 * if exist it means that it has a trip
	 * @param plugId
	 * @return true if exist
	 */
	public boolean isThereATripForPlugId(String plugId){
		// go to trip page
		select.selectTripTrips();
		//search for plug id
		persistUtil.searchForElement(plugId);
		
		int sizeOfElements = driver.findElements(By.xpath("//tr")).size();
		for (int i = 1; i < sizeOfElements; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td[4][text()='" + plugId + "']";			
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	/**
	 * Delete All trips
	 */
	public String deleteAll(){
		select.selectTripTrips();
		return persistUtil.deleteAllFromTablePage();	
	}
}
