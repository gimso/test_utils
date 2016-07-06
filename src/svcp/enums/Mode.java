package svcp.enums;

/**
 * Description: Enable or Disable.</br>
 * Encoding: One byte with the following encoding:</br>
 * Description Encoding</br>
 * Disable 0x00</br>
 * Enable 0x01</br>
 * 
 * @author Yehuda
 *
 */
public enum Mode {

	DISABLE(0x00), 
	ENABLE(0x01);

	private int value;

	Mode(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}
	
	/**
	 * Gets Mode by value
	 * @param value
	 * @return Mode
	 */
	public static Mode getMode(int value) {
		for (Mode mode : Mode.values())
			if (mode.getValue() == value)
				return mode;
		System.err.println("Unknown tag with value " + value);
		return null;
	}
}