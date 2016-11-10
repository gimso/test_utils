package svcp.enums;

/**
 * 
 * 6.20. ME Modem (4G) Online (0x14)</br>
 * 4. Description: ME modem connection status report of the AP.</br>
 * 5. Encoding: One byte with the following encoding:</br>
 * Description Encoding</br>
 * Host is disconnected from cloud 0x00</br>
 * Host is connected from cloud 0x01</br>
 * 
 * 
 * @author Yehuda
 *
 */
public enum MeModem4gOnline {

	HOST_IS_DISCONNECTED_FROM_CLOUD((byte) 0x00), 
	HOST_IS_CONNECTED_FROM_CLOUD((byte) 0x01);

	private byte value;

	MeModem4gOnline(byte aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets MeModem4gOnline by value
	 * 
	 * @param intValue
	 * @return MeModem4gOnline
	 */
	public static MeModem4gOnline getMeModem4gOnline(int intValue) {
		byte value = (byte) intValue;
		for (MeModem4gOnline modem4gOnline : MeModem4gOnline.values())
			if (modem4gOnline.getValue() == value)
				return modem4gOnline;

		System.err.println("Unknown value (" + value + ") for 4G Connection Status, return null");
		return null;
	}
}