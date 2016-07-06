package svcp.enums;

/**
 * SIM generation (0x0F)</br>
 * Description: Chooses SIM card generation (SIM / USIM)Description
 * Encoding</br>
 * SIM (2G) 0x00</br>
 * USIM (3G) 0x01</br>
 * Encoding: One byte.</br>
 * 
 * @author Yehuda
 */
public enum SimGeneration {

	SIM(0x00), 
	USIM(0X01);

	private int value;

	SimGeneration(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets SimGeneration by value 
	 * @param value
	 * @return SimGeneration
	 */
	public static SimGeneration getSimGeneration(int value) {
		for (SimGeneration simGeneration : SimGeneration.values())
			if (simGeneration.getValue() == value)
				return simGeneration;
		System.err.println("Unknown value (" + value + ") for SIM generation, return null");
		return null;
	}
}
