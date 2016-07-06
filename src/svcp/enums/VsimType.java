package svcp.enums;

/**
 * 6.14. vSIM type (0x0E)</br>
 * 1. Description: Chooses vSIM file type "vSIM type in use" (0x0C)
 * command.</br>
 * 2. Encoding: One byte.</br>
 * 
 * @author Yehuda
 *
 */
public enum VsimType {

	FSIM(0x00), 
	RSIM(0x01), 
	NONE(0xFF);

	private int value;

	VsimType(int aValue) {
		this.value = aValue;
	}

	public int getValue() {
		return value;
	}
	
	/**
	 * Gets the VsimType by value
	 * @param intValue
	 * @return VsimType
	 */
	public static VsimType getVsim(int intValue) {
		for (VsimType vSimType : VsimType.values()) {
			if (vSimType.getValue() == intValue) {
				return vSimType;
			}
		}
		System.err.println("Unknown value (" + intValue + ") for vSIM type, return null");
		return null;
	}
}