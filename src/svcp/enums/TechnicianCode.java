package svcp.enums;

/**
 * 6.18. Allow Technician code (0x12) </br>
 * 4. Description: Enable/Disable vSIM from handling technician codes from
 * ME.</br>
 * 5. Encoding: One byte with the following encoding:</br>
 * Description Encoding</br>
 * Technician code = disabled 0x00</br>
 * Technician code = enabled 0x01</br>
 * 
 * @author Yehuda
 *
 */
public enum TechnicianCode {
	
	DISABLED(0X01),
	ENABLED(0X02);

	private int value;

	TechnicianCode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Gets TechnicianCode by value
	 * @param value
	 * @return TechnicianCode
	 */
	public static TechnicianCode getTechnicianCode(int value) {
		for (TechnicianCode eResult : TechnicianCode.values())
			if (eResult.getValue() == value)
				return eResult;
		System.err.println("Unknown value (" + value + ") for Technician Code, return null");
		return null;
	}
}
