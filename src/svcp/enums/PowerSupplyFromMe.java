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
public enum PowerSupplyFromMe {
	
	POWER_SUPPLY_FROM_ME_IS_OFF((byte)0x00), 
	POWER_SUPPLY_FROM_ME_IS_ON((byte)0x01);
	
	private byte value;

	PowerSupplyFromMe(byte aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}
	
	/**
	 * Gets PowerSupplyFromMe by value 
	 * @param intValue
	 * @return PowerSupplyFromMe
	 */
	public static PowerSupplyFromMe getPowerSupplyMode(int intValue) {
		byte value = (byte) intValue;
		for (PowerSupplyFromMe powerSupplyFromMe : PowerSupplyFromMe.values())
			if (powerSupplyFromMe.getValue() == value)
				return powerSupplyFromMe;

		System.err.println("Unknown value (" + value + ") for Power Supply, return null");
		return null;
	}
}