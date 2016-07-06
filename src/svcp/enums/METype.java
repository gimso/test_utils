package svcp.enums;

/**
 * 6.30. ME Type (0x23)</br>
 * 1. Description: The mobile equipment type</br>
 * 2. Encoding: One byte</br>
 * 
 * @author Yehuda
 */
public enum METype {
	
	SAMSUNG_GALAXY_3(0x00), 
	SIERRA_MC7710(0x01),
	APPLE_IPHONE_4S(0x02),
	APPLE_IPHONE_5S(0x03),
	MF60(0x04),
	SAMSUNG_GALAXY_4(0x05),
	SAMSUNG_GALAXY_5(0x06),
	APPLE_IPHONE_6(0x07),
	SAMSUNG_GALAXY_6(0x08),
	VIRTUAL_PORTABLE_HOST_SPOT(0x09),
	GENERIC_BASEBAND(0xFE);

	private int value;

	METype(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}
	
	/**
	 * Gets the METype by value
	 * @param value
	 * @return METype
	 */
	public static METype getMEType(int value) {
		for (METype meType : METype.values())
			if (meType.getValue() == value)
				return meType;
		System.err.println("Unknown value (" + value + ") for ME Type, return null");
		return null;
	}
}