package svcp.enums;

/**
 * 1. Description: Status of the power supply from ME to vSIM. </br>
 * 2. Encoding: One byte with the following encoding: </br>
 * Description Encoding </br>
 * Power supply from ME is Off 0x00 </br>
 * Power supply from ME is ON 0x01 </br>
 * 
 * @author Yehuda
 *
 */
public enum SetFilesOptions {

	RESTORE_RSIM_(0X00), IN_HOME_COUNTRY(0X01), NOT_IN_HOME_COUNTRY_LOCAL_SIM(0X02), UNUSED(0XFF);

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
	public static SetFilesOptions getPowerSupplyMode(int intValue) {

		for (SetFilesOptions setFilesOptions : SetFilesOptions.values())
			if (setFilesOptions.getValue() == intValue)
				return setFilesOptions;

		System.err.println("Unknown value (" + intValue + ") for Power Supply, return null");
		return null;
	}
}