package svcp.enums;

/**
 * Apply update </br>
 * Description: Request to apply pending vSIM firmware </br>
 * Encoding: One byte with the following encoding: </br>
 * Apply update and perform soft-reset 0x00 </br>
 * Apply update and wait for external reset </br>
 * 
 * @author Yehuda
 */
public enum ApplyUpdate {
	
	APPLY_UPDATE_AND_PERFORM_SOFT_RESET(0x00), 
	APPLY_UPDATE_AND_WAIT_FOR_EXTERNAL_RESET(0x01);
	
	private int value;

	ApplyUpdate(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Gets the ApplyUpdate by value
	 * @param value
	 * @return ApplyUpdate
	 */
	public static ApplyUpdate getApplyUpdate(int value) {
		for (ApplyUpdate applyUpdate : ApplyUpdate.values())
			if (applyUpdate.getValue() == value)
				return applyUpdate;

		System.err.println("Unknown value (" + value + ") for Apply Update , return null");
		return null;
	}
}