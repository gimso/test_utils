package svcp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.13. UICC Relay (0x0D) </br>
 * Description: A UICC relay in the platform.</br>
 * Depending on the platform it uses, </br>
 * the vSIM can control different relays to achieve different
 * functionality.</br>
 * A UICC relay tag defines the UICC source and UICC destination.</br>
 * Encoding: Two bytes. </br>
 * First byte is for the UICC source (ISO 7816 slave) and the second byte is the
 * UICC</br>
 * destination (ISO 7816 master)
 * 
 * @author Yehuda
 *
 */
public enum UICCRelay {
	
	EMBEDDED_UICC_1(0x00), 
	EMBEDDED_UICC_2(0x01), 
	PHYSICAL_UICC(0x02), 
	VIRTUAL_UICC__VSIM(0x03), 
	EXTERNAL_BASEBAND(0x04), 
	INTERNAL_BASEBAND_1(0x05), 
	INTERNAL_BASEBAND_2(0x06), 
	// when not relay on anything, or not connected
	UNKNOWN(0xFF);

	private int value;

	UICCRelay(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets UICCRelay by its value
	 *
	 * @param value
	 *            The value of the relay
	 * @return UICCRelay null
	 */
	public static UICCRelay getUICCRelay(int value) {
		for (UICCRelay euiccRelay : UICCRelay.values())
			if (euiccRelay.getValue() == value)
				return euiccRelay;
		System.err.println("Unknown value (" + value + ") for UICC relay, return null");
		return null;
	}

	/**
	 * List of all Sources
	 */
	public static List<UICCRelay> srcList() {
		List<UICCRelay> rv = new ArrayList<>();
		rv.add(EMBEDDED_UICC_1);
		rv.add(EMBEDDED_UICC_2);
		rv.add(PHYSICAL_UICC);
		rv.add(VIRTUAL_UICC__VSIM);
		rv.add(UNKNOWN);
		return rv;
	}

	/**
	 * List of all Destinations
	 */
	public static List<UICCRelay> destList() {
		List<UICCRelay> rv = new ArrayList<>();
		rv.add(EXTERNAL_BASEBAND);
		rv.add(INTERNAL_BASEBAND_1);
		rv.add(INTERNAL_BASEBAND_2);
		rv.add(VIRTUAL_UICC__VSIM);
		return rv;
	}
}