package svcp.enums;

/**
 * File Types Enum
 * 
 * @author Yehuda
 *
 */
public enum FileType {
	
	MASTER_FILE_ROOT(0x00),
	DIRECTORY_DEDICATED_FILE(0X01), 
	ELEMENTARY_TRANSPARENT_BINARY_FILE(0X02), 
	ELEMENTARY_LINEAR_FIXED_LENGTH_FILE(0X03),
	ELEMENTARY_LINEAR_VARIABLE_LENGTH_FILE(0X04), 
	ELEMENTARY_CYCLIC_FILE(0X05), 
	FILE_CONTROL_PARAMETERS_FCP(0xFF);

	private int value;

	FileType(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets the FileType by its value
	 * @param value
	 * @return FileType
	 */
	public static FileType getFileType(int value) {
		for (FileType fileType : FileType.values())
			if (fileType.getValue() == value)
				return fileType;
		System.err.println("Unknown value (" + value + ") for File Type, return null");
		return null;
	}
}