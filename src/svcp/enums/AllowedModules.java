package svcp.enums;

import java.util.Arrays;

import global.Conversions;

public enum AllowedModules {
	LOG_MAIN 				(new byte[]{0,0,0,0x1}),
	LOG_UNIT_TESTS 			(new byte[]{0,0,0,0x2}),
	LOG_REACTOR 			(new byte[]{0,0,0,0x3}),
	LOG_SC_PHY_SAM3S_USART 	(new byte[]{0,0,0,0x4}),
	LOG_SC_PHY_PASSTHRU 	(new byte[]{0,0,0,0x5}),
	LOG_SC_TPORT_MASTER 	(new byte[]{0,0,0,0x6}),
	LOG_SC_TPORT_SLAVE 		(new byte[]{0,0,0,0x7}),
	LOG_SC_APP_VFS 			(new byte[]{0,0,0,0x8}),
	LOG_VSIM 				(new byte[]{0,0,0,0x9}),
	LOG_CRSH				(new byte[]{0,0,0,0xA}), 
	LOG_VBRD 				(new byte[]{0,0,0,0xB}),
	LOG_SIMULATOR 			(new byte[]{0,0,0,0xC}),
	LOG_USBD 				(new byte[]{0,0,0,0xD}), 
	LOG_TIMER 				(new byte[]{0,0,0,0xE}),
	LOG_HOST_IF 			(new byte[]{0,0,0,0xF}),
	LOG_SVCP_PRTCL 			(new byte[]{0,0,0,0x10}),
	LOG_TLV 				(new byte[]{0,0,0,0x11}),
	LOG_COL_BUF 			(new byte[]{0,0,0,0x12}),
	LOG_FW_UPDATE 			(new byte[]{0,0,0,0x13}),
	LOG_LOGGER 				(new byte[]{0,0,0,0x14});

	
	private byte[] value;

	AllowedModules(byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	/**
	 * Gets the AllowedModules by value
	 * @param value
	 * @return ApplyUpdate
	 */
	public static AllowedModules getAllowedModules(byte[] value) {
		for (AllowedModules modules : AllowedModules.values()) {
			if (Arrays.equals(modules.getValue(),value))
				return modules;
		}

		System.err.println("Unknown value (" + value + ") for Allow Modules , return null");
		return null;
	}
	
	/**
	 * 
	 * @param hexStringValue
	 * @return AllowedModules
	 */
	public static AllowedModules getAllowedModules(String hexStringValue) {
		return getAllowedModules(Conversions.hexStringToByteArray(hexStringValue,4));
	}

}
