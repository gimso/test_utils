package svcp.enums;

import java.util.Arrays;

import global.Conversions;

/**
 * Utility to get file id and SIM generation path for SET_FILES on the VFS
 * 
 * @author Yehuda
 *
 */
public enum FilePath {

	MASTER_FILE("3F00", "master file"), 
	DF_TELECOM("7f10", "df.telecom"), 
	USIM_3G_PATH("7FFF", "adf"), 
	SIM_2G_PATH("7F20", "df.gsm");

	private byte[] value;
	private String name;

	FilePath(byte[] value, String name) {
		this.value = value;
		this.name = name;
	}

	FilePath(String value, String name) {
		byte[] hexStringToByteArray = Conversions.hexStringToByteArray(value);
		this.value = hexStringToByteArray;
		this.name = name;
	}

	public byte[] getValue() {
		return value;
	}

	public String getName() {
		return name + String.format(" [0x%X]", Conversions.bytesToInt(value));
	}

	/**
	 * Gets the FilePath by value The byte array contains one or more values
	 * 
	 * @param value
	 * @return FilePath
	 */
	public static FilePath getFilePath(byte[] value) {
		if (value != null) {
			for (FilePath filePath : FilePath.values()) {
				if (Arrays.equals(filePath.getValue(), value))
					return filePath;
			}
		}
		System.err.println("Unknown value (" + value + ") for FileId , return null");
		return null;
	}

	public static FilePath getFilePath(String value) {
		return getFilePath(Conversions.hexStringToByteArray(value));
	}
}