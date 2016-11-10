package svcp.enums;

public enum SetFilesOptions {

	RESTORE_RSIM_(0X00), //restore from AP
	IN_HOME_COUNTRY(0X01), // local or roaming in same country/mcc
	NOT_IN_HOME_COUNTRY_LOCAL_SIM(0X02), //roaming
	UNUSED(0XFF);

	private int value;

	SetFilesOptions(int aValue) {
		this.value = aValue;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Gets PowerSupplyFromMe by value
	 * 
	 * @param intValue
	 * @return PowerSupplyFromMe
	 */
	public static SetFilesOptions getSetFilesOptions(int intValue) {

		for (SetFilesOptions setFilesOptions : SetFilesOptions.values())
			if (setFilesOptions.getValue() == intValue)
				return setFilesOptions;

		System.err.println("Unknown value (" + intValue + ") for Power Supply, return null");
		return null;
	}
}