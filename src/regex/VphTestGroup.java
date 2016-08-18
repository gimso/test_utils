package regex;
/**
 * Return
 * @author Yehuda
 *
 */
public enum VphTestGroup {
	WHOLE_SVCP_MESSAGE(1),
	HEADER(2),
	SVCP_VERSION(3),
	SVCP_HEADER_ID(4),
	SVCP_HEADER_OPCODE(5),
	SVCP_HEADER_LENGTH(6),
	HEADER_CRC(7);
	
	private int value;

	VphTestGroup(int aValue) {
		this.value = aValue;
	}

	public int getValue() {
		return  value;
	}
	
	/**
	 * Gets Mode by value
	 * @param value
	 * @return Mode
	 */
	public static VphTestGroup getVphTestGroup(int value) {
		for (VphTestGroup testGroup : VphTestGroup.values())
			if (testGroup.getValue() == value)
				return testGroup;
		System.err.println("Unknown VphTestGroup with value " + value);
		return null;
	}
	
}
