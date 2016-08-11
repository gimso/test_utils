package persist.inventory;

import global.PersistException;
import selenium.PersistUtil;
import selenium.SelectPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This class is AddedSim board in sim board page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class SimBoards {
	private static final String ID_UNIT = "id_unit";
	private static final String ID_SLOT = "id_slot";
	private static final String LINK_TEXT_ADD_SIM_BOARD = "Add sim board";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public SimBoards() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add sim board with all parameters available
	 * 
	 * @param unit
	 * @param slot
	 * @return String result message
	 * @throws PersistException
	 */
	public String add(String unit, String slot){
		// go to sim boards page
		select.selectInventorySimBoards();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_SIM_BOARD)).click();

		// select unit
		if (unit != null) {
			persistUtil.selectByVisibleText(ID_UNIT, unit);
		}

		// select slot
		if (slot != null) {
			persistUtil.selectByVisibleText(ID_SLOT, slot);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 *
	 * Modify and update the sim board details by unit and slot
	 * 
	 * @param unit
	 * @param slot
	 * @param newUnit
	 * @param newSlot
	 * @return String result message
	 * @throws PersistException
	 */
	public String modifyByUnitAndSlot(String unit, String slot, String newUnit,
			String newSlot){
		// go to sim board page
		select.selectInventorySimBoards();

		// find the sim board to modify and click on it
		driver.findElement(By.linkText(unit + ":" + slot)).click();

		// select unit
		if (newUnit != null) {
			persistUtil.selectByVisibleText(ID_UNIT, newUnit);
		}

		// select slot
		if (newSlot != null) {
			persistUtil.selectByVisibleText(ID_SLOT, newSlot);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete sim board by unit and slot
	 * 
	 * @param unit
	 * @param slot
	 * @return String result message
	 * @throws PersistException
	 */
	public String deleteByUnitAndSlot(String unit, String slot){
		// go to sim board page
		select.selectInventorySimBoards();

		// delete
		persistUtil.deleteByLinkTextName(unit + ":" + slot);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		unitTesting();

	}

	/**
	 * 
	 */
	public static void unitTesting() {
		SimBoards simBoards = new SimBoards();
		String unit = "a";
		String slot = "2";
		
		// ADD
		System.out.println(simBoards.add(unit, slot));

		// MODIFY
		System.out.println(simBoards.modifyByUnitAndSlot(unit, slot, unit, slot));

		// DELETE
		System.out.println(simBoards.deleteByUnitAndSlot(unit, slot));

		simBoards.driver.close();
	}
	
	/**
	 * Delete All sim boards
	 */
	public String deleteAll(){
		select.selectInventorySimBoards();
		return persistUtil.deleteAllFromTablePage();	
	}
}
