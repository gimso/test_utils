package svcp.enums;

import global.Conversions;

/**
 * Utility to get file id and SIM generation path for SET_FILES on the VFS
 * 
 * @author Yehuda
 *
 */
public enum SetFiles {

	USIM_3G_PATH(new byte[] { 127, -1 }),	//"7FFF"
	SIM_2G_PATH	(new byte[] { 127, 32 }),	//"7F20"
	IMSI_FILE_ID(new byte[] { 111, 7  });	//"6F07"

	private byte[] value;

	SetFiles(byte[] value) {
		this.value = value;
	}

	SetFiles(String value) {
		this.value = Conversions.hexStringToByteArray(value);
	}

	public byte[] getValue() {
		return value;
	}
}
