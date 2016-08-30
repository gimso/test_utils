package persist.inventory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import selenium.PersistUtil;
import selenium.PersistPageSelect;
/**
 * This class is Added and Modified Sims in 
 * persist.inventory.Sims page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Sims {
	private static final String ID_COMMENTS = "id_comments";
	private static final String ID_ROAMING_MODE = "id_roaming_mode";
	private static final String ID_APN = "id_apn";
	private static final String ID_OUTGOING_NUMBER = "id_outgoing_number";
	private static final String ID_INCOMING_NUMBER = "id_incoming_number";
	private static final String ID_NUMBER = "id_number";
	private static final String ID_ALLOWED = "id_allowed";
	private static final String ID_PAYTYPE = "id_paytype";
	private static final String ID_BALANCE = "id_balance";
	private static final String ID_MNC_LENGTH = "id_mnc_length";
	private static final String ID_OFFSET = "id_offset";
	private static final String ID_BOARD = "id_board";
	private static final String ID_GROUP = "id_group";
	private static final String LINK_TEXT_ADD_SIM = "Add sim";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private PersistPageSelect select;
	private PersistUtil persistUtil;

	public Sims() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();}

	/**
	 * Add sim with all parameters available
	 * 
	 * @param group
	 * @param board
	 * @param offset
	 * @param mncLength
	 * @param balance
	 * @param paytype
	 * @param allowed
	 * @param number
	 * @param incomingNumber
	 * @param outgoingNumber
	 * @param simGroupApnOverride
	 * @param roamingModeOverride
	 * @param comments
	 * @return String result message
	 * @
	 */
	public String add(
			String group,
			String board,
			String offset,
			String mncLength,
			String balance,
			String paytype,
			Boolean allowed,
			String number,
			String incomingNumber,
			String outgoingNumber,
			String simGroupApnOverride,
			String 	roamingModeOverride,
			String 	comments
				)  {
		// go to sims page
		select.selectInventorySims();

		// find the add button and click on it
		driver.findElement(By.linkText(LINK_TEXT_ADD_SIM)).click();

		// select group
		if (group != null) {
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// select board
		if (board != null) {
			persistUtil.selectByVisibleText(ID_BOARD, board);
		}

		// select offset
		if (offset  != null) {
			persistUtil.selectByVisibleText(ID_OFFSET, offset);
		}

		// select mnc length
		if (mncLength != null) {
			persistUtil.selectByVisibleText(ID_MNC_LENGTH, mncLength);
		}
		
		// insert balance default 100
		if (balance != null) {
			driver.findElement(By.id(ID_BALANCE)).clear();
			driver.findElement(By.id(ID_BALANCE)).sendKeys(balance);
		}

		// select paytype
		if (paytype != null) {
			persistUtil.selectByVisibleText(ID_PAYTYPE, paytype);
		}

		// insert allowed (default not allowed)
		if (allowed != null && allowed) {
			driver.findElement(By.id(ID_ALLOWED)).click();
		}

		// insert number
		if (number != null) {
			driver.findElement(By.id(ID_NUMBER)).clear();
			driver.findElement(By.id(ID_NUMBER)).sendKeys(number);
		}

		// insert incoming number
		if (incomingNumber != null) {
			driver.findElement(By.id(ID_INCOMING_NUMBER)).clear();
			driver.findElement(By.id(ID_INCOMING_NUMBER)).sendKeys(incomingNumber);
		}
		
		// insert outgoing number
		if (outgoingNumber != null) {
			driver.findElement(By.id(ID_OUTGOING_NUMBER)).clear();
			driver.findElement(By.id(ID_OUTGOING_NUMBER)).sendKeys(outgoingNumber);
		}
		
		// select simgroup apn override
		if (simGroupApnOverride != null) {
			persistUtil.selectByVisibleText(ID_APN, simGroupApnOverride);
		}

		// select roaming mode override
		if (roamingModeOverride != null) {
			persistUtil.selectByVisibleText(ID_ROAMING_MODE, roamingModeOverride);
		}
		
		// insert comments
		if (comments != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(comments);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	/**
	 * Add sim with the requires parameters only, the rest will fill with default values
	 * @param board
	 * @param offset
	 * @return String result message
	 * @
	 */
	public String add(String board, String offset)  {
		return add(null, board, offset, null, null, null, false, null, null,
				null, null, null, null);
	}
	
	/**
	 *  Modify and update the sim details by board and offset
	 * @param board
	 * @param offset
	 * @param group
	 * @param mncLength
	 * @param balance
	 * @param paytype
	 * @param allowed
	 * @param number
	 * @param incomingNumber
	 * @param outgoingNumber
	 * @param simGroupApnOverride
	 * @param roamingModeOverride
	 * @param comments
	 * @return String result message
	 * @
	 */
	public String modifyByBoardAndOffset(
			String board,
			String offset,
			String newBoard,
			String newOffset,
			String group,
			String mncLength,
			String balance,
			String paytype,
			Boolean allowed,
			String number,
			String incomingNumber,
			String outgoingNumber,
			String simGroupApnOverride,
			String 	roamingModeOverride,
			String 	comments
			)  {
		// go to sims page
		select.selectInventorySims();

		// find the link text of sim unit name, board and offset
		driver.findElement(By.linkText(board + ":" + offset)).click();

		if (group != null) {
			// select group
			persistUtil.selectByVisibleText(ID_GROUP, group);
		}

		// select board
		if (newBoard != null) {
			persistUtil.selectByVisibleText(ID_BOARD, newBoard);
		}

		// select offset
		if (newOffset != null) {
			persistUtil.selectByVisibleText(ID_OFFSET, newOffset);
		}

		if (mncLength != null) {
			// select mnc length
			persistUtil.selectByVisibleText(ID_MNC_LENGTH, mncLength);
		}
		
		if (balance != null) {
			// insert balance default 100
			driver.findElement(By.id(ID_BALANCE)).clear();
			driver.findElement(By.id(ID_BALANCE)).sendKeys(balance);
		}
		
		if (paytype != null) {
			// select paytype
			persistUtil.selectByVisibleText(ID_PAYTYPE, paytype);
		}
		
		// insert allowed (default not allowed)
		persistUtil.clickCheckIfNeeded(ID_ALLOWED, allowed);

		if (number != null) {
			// insert number
			driver.findElement(By.id(ID_NUMBER)).clear();
			driver.findElement(By.id(ID_NUMBER)).sendKeys(number);
		}
		
		if (incomingNumber != null) {
			// insert incoming number
			driver.findElement(By.id(ID_INCOMING_NUMBER)).clear();
			driver.findElement(By.id(ID_INCOMING_NUMBER)).sendKeys(
					incomingNumber);
		}
		
		if (outgoingNumber != null) {
			// insert outgoing number
			driver.findElement(By.id(ID_OUTGOING_NUMBER)).clear();
			driver.findElement(By.id(ID_OUTGOING_NUMBER)).sendKeys(
					outgoingNumber);
		}
		
		if (simGroupApnOverride != null) {
			// select simgroup apn override
			persistUtil.selectByVisibleText(ID_APN, simGroupApnOverride);
		}
		
		if (roamingModeOverride != null) {
			// select roaming mode override
			persistUtil.selectByVisibleText(ID_ROAMING_MODE,
					roamingModeOverride);
		}
		
		if (comments != null) {
			// insert comments
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(comments);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}
	
	public String deleteByBoardAndOffset(String board, String offset) {
		// go to sims page
		select.selectInventorySims();
		//delete
		persistUtil.deleteByLinkTextName(board + ":" + offset);		
		
		//check the page result
		return persistUtil.finalCheck();
	}
	
	public static void main(String[] args) {
//		System.out.println(new Sims().deleteAll());
		unitTesting();
	}

	/**
	 * 
	 */
	public static void unitTesting() {
		Sims sims = new Sims();
//		String group = "Unassigned";
		String board = "SIMUNIT:0";
		String offset = "1";
//		String mncLength = "2";
//		String balance = "1510";
//		String paytype = "Prepaid";
//		Boolean allowed = true;
//		String number = "0";
//		String incomingNumber = "0";
//		String outgoingNumber = "0";
//		String simGroupApnOverride = "test";
//		String roamingModeOverride = "Allow Roaming";
//		String comments = "no comments";
//		try {
//			System.out.println(sims.add(group, board, offset, mncLength,
//					balance, paytype, allowed, number, incomingNumber,
//					outgoingNumber, simGroupApnOverride, roamingModeOverride,
//					comments));
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
		
			System.out.println(sims.add(board, offset));
		
			System.out.println(sims.modifyByBoardAndOffset(board, offset,null,"3",
					null, null, null, null, null, null,
					null, null, null,
					null, null));
		
			System.out.println(sims.deleteByBoardAndOffset(board, "3"));
		
		sims.driver.close();
	}

	//********* special functions **********//
	
	/**
	 *  Disallowed all sims at once
	 */
	public void disAllowedAllSims() {
		select.selectInventorySims();
		driver.findElement(By.id("action-toggle")).click();
		new Select(driver.findElement(By.name("action")))
				.selectByVisibleText("Disallow SIMs");
	}
	
	
	/**
	 * Allowed all sims at once
	 */
	public void allowAllSims() {
		select.selectInventorySims();
		driver.findElement(By.id("action-toggle")).click();
		new Select(driver.findElement(By.name("action")))
				.selectByVisibleText("Allow SIMs");

	}
	
	/**
	 * Delete All sims
	 */
	public String deleteAll(){
		select.selectInventorySims();
		return persistUtil.deleteAllFromTablePage();	
	}
}
