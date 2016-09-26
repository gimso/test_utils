package svcp.enums;

/**
 * 
 * Cloud Connection (0x13) </br>
 * 1. Description: Cloud connection status report of the AP. </br>
 * 2. Encoding: One byte with the following encoding: </br>
 * Description Encoding </br>
 * Host is disconnected from cloud 0x00 </br>
 * Host is connected from cloud 0x01 </br>
 * 
 * @author Yehuda
 *
 */
public enum Cloud3GConnection {

	HOST_IS_DISCONNECTED_FROM_CLOUD((byte) 0x00), 
	HOST_IS_CONNECTED_FROM_CLOUD((byte) 0x01);

	private byte value;

	Cloud3GConnection(byte aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets Cloud3GConnection by value
	 * 
	 * @param intValue
	 * @return Cloud3GConnection
	 */
	public static Cloud3GConnection getCloud3GConnection(int intValue) {
		byte value = (byte) intValue;
		for (Cloud3GConnection cloudConnection : Cloud3GConnection.values())
			if (cloudConnection.getValue() == value)
				return cloudConnection;

		System.err.println("Unknown value (" + value + ") for Cloud Connection Status, return null");
		return null;
	}
}