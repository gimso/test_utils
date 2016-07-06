package persist.usage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import global.PersistUtil;
import global.SelectPage;

public class Allocations {
	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Allocations() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Find the column where plug id equals the parameter we get and delete it
	 * (if exist) Delete the plug by id
	 * 
	 * @param name
	 * @return String result message
	 * @
	 */
	public String deleteByPlugId(String plugId)  {
		// go to allocation page
		select.selectUsageAllocations();
		
		//search plug id by url
		persistUtil.searchForElement(plugId);
		
		// get the size of all elements in the table
		int sizeOfElements = persistUtil.getDriver()
				.findElements(By.xpath("//tr")).size();
		
		// when find in the column the id it clicked on the select check box of
		// the column
		for (int i = 1; i < sizeOfElements + 1; i++) {
			try {
				String xpathCheck = "//tr[" + i + "]/td[8]/a[text()='" + plugId
						+ "']";
				String xpathClick = "//tr[" + i + "]/td[1]/input";
				if (driver.findElement(By.xpath(xpathCheck)) != null) {
					driver.findElement(By.xpath(xpathClick)).click();
					persistUtil.deleteSelectedItemsFromTable();
					i=sizeOfElements;
				}
			} catch (Exception e) {
			}
		}
		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Delete All allocations
	 */
	public String deleteAll(){
		select.selectUsageAllocations();
		return persistUtil.deleteAllFromTablePage();	
	}
	
	public static void main(String[] args) {
		System.out.println(new Allocations().deleteByPlugId("000010001753"));
	}
}
