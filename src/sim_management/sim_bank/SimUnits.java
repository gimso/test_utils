package sim_management.sim_bank;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;

/**
 * 
 * @author Dana
 *
 */
public class SimUnits {

	private static final String ID_STATUS = "id_status";
	private static final String ID_SLOT1_STATUS = "id_simplacement_set-0-status";
	private static final String NAME_SAVE = "_save";
	private static final String NAME_ACTION = "action";
	private static final String XPATH_SELECT_ALL = ".//*[@id='action-toggle']";
	private static final String ACTION_TRIGGER_SYNCHRONIZATION = "Trigger synchronization";
	private static final String ACTION_PING_SIM_UNIT = "Ping SIM Unit";
	private static final String ACTION_DELETE_SELECTED_SIM_UNIT = "Delete selected sim units";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;

	public SimUnits() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}

	/**
	 * Modify a sim unit's status, or sim slot status, by sim unit's name.</br>
	 * Note: the sim slot is hard coded, will always be the first slot.
	 * 
	 * @param name
	 * @param unitStatus
	 * @param slotStatus
	 * @return message
	 */
	public String modifyByName(String name, String unitStatus, String slotStatus) {
		// go to sim units page
		select.selectSimUnits();
		// select the sim unit to modify
		driver.findElement(By.linkText(name)).click();
		// select sim unit's status
		if (unitStatus != null) {
			simDriverUtil.selectByVisibleText(ID_STATUS, unitStatus);
		}
		// select sim slot's status
		if (slotStatus != null) {
			simDriverUtil.selectByVisibleText(ID_SLOT1_STATUS, slotStatus);
		}
		// click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Trigger all sim units synchronization
	 * 
	 * @return message
	 */
	public String triggerSynchronizationAllSimUnits() {
		// select sim profiles page
		select.selectSimUnits();
		// select all, then select the action "trigger synchronization", and
		// click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_TRIGGER_SYNCHRONIZATION);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Ping all existing sim units
	 * 
	 * @return message
	 */
	public String pingAllSimUnits() {
		// select sim profiles page
		select.selectSimUnits();
		// select all, then select the action "ping sim unit", and click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_PING_SIM_UNIT);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all sim units
	 * 
	 * @return message
	 */
	public String deleteSelectedSimUnit() {
		// select sim profiles page
		select.selectSimUnits();
		// select all, then select the action "delete selected sim unit", and
		// click on "go"
		simDriverUtil.selectActionForAllElements(XPATH_SELECT_ALL, NAME_ACTION, ACTION_DELETE_SELECTED_SIM_UNIT);
		return simDriverUtil.finalCheck();
	}
}
