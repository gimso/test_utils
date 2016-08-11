package svcp.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import global.Conversions;

public enum AllowedModule {
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

	
	private static final int ALLOW_MODULE_VALUE_SIZE = 4;
	
	private byte[] value;

	AllowedModule(byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	/**
	 * Gets the AllowedModule by value
	 * The byte array contains one or more values
	 * @param value
	 * @return List of AllowedModule
	 */
	public static List<AllowedModule> getAllowedModules(byte[] value) {
		List<AllowedModule> allowedModules = new ArrayList<>();
		if 
		(value == null)
			return null;
		else if 
		(value.length == ALLOW_MODULE_VALUE_SIZE)
			allowedModules.add(returnAllowModule(value));
		else 
			for (int i = 0; i < value.length; i = i + ALLOW_MODULE_VALUE_SIZE) {
				byte[] temp = new byte[4];
				System.arraycopy(value, i, temp, 0, ALLOW_MODULE_VALUE_SIZE);
				allowedModules.add(returnAllowModule(temp));
			}
		return allowedModules;
	}

	
	/**
	 * Can receive String in diffrent size (not more than ALLOW_MODULE_VALUE_SIZE*2 though..)
	 * e.g. getAllowedModules("00000001", "00000005", "D") all return the list correctly
	 * @param one or more hexStringValue
	 * @return AllowedModules list
	 */
	public static List<AllowedModule> getAllowedModules(String... hexStringValue) {
		byte[] rv = new byte[hexStringValue.length * ALLOW_MODULE_VALUE_SIZE];
		int index = 0;
		for (String hex : hexStringValue) {
			byte[] temp = Conversions.hexStringToByteArray(hex, ALLOW_MODULE_VALUE_SIZE);
			System.arraycopy(temp, 0, rv, index, ALLOW_MODULE_VALUE_SIZE);
			index = index + ALLOW_MODULE_VALUE_SIZE;
		}
		return getAllowedModules(rv);
	}

	/**
	 * @param value - byte array
	 * @return the AllowedModule object from the byte array value
	 */
	private static AllowedModule returnAllowModule(byte[] value) {
		for (AllowedModule modules : AllowedModule.values()) {
			if (Arrays.equals(modules.getValue(),value))
				return modules;
		}
		
		System.err.println("Unknown value (" + value + ") for Allow Modules , return null");
		return null;
	}

}