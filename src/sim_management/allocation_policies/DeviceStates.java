package sim_management.allocation_policies;

import selenium.SimDriverUtil;
import selenium.SimPageSelect;

/**
 * Delete device states
 * 
 * @author Dana
 *
 */
public class DeviceStates {

	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;

	public DeviceStates() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
	}

	/**
	 * Delete device state by device id
	 * 
	 * @param deviceId
	 * @return message
	 */
	public String deleteByDeviceId(String deviceId) {
		select.selectDeviceStates();
		simDriverUtil.deleteByLinkTextName(deviceId);
		return simDriverUtil.finalCheck();
	}

	/**
	 * Delete all device states
	 * 
	 * @return message
	 */
	public String deleteAll() {
		select.selectDeviceStates();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
