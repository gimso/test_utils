package svcp.enums;

/**
 * 6.32. Result Tag (0x80)</br>
 * 1. Description: A tag for the result code</br>
 * 2. Encoding: One Byte</br>
 * 
 * @author Yehuda
 */
public enum ResultTags {

	OPERATION_WAS_SUCCESSFUL_OPCODE_ACCEPTED(0X01),
	OPERATION_SUFFERED_FROM_GENERIC_FAILURE(0X02),
	OPCODE_NOT_SUPPORTED(0X03),
	MISSING_TAGS(0X04),
	ERROR_PARSING_TLV(0X05),
	UNKNOWN_TAG_VALUE(0X06),
	FORBIDDEN_VALUE(0X07),
	VSIM_FIRMWARE_WAITING_FOR_HEADER(0X08),
	VSIM_FIRMWARE_WRONG_PACKET_OFFSET(0X09),
	VSIM_FIRMWARE_NOT_COMPLETE(0X0A),
	VSIM_FIRMWARE_WRONG_FW_SIZE(0X0B),
	VSIM_FIRMWARE_TOTAL_SIZE_IS_TOO_BIG(0X0C),
	VSIM_FIRMWARE_PACKET_SIZE_IS_TOO_BIG(0X0D),
	VSIM_RELAY_NOT_SUPPORT_IN_THIS_PLATFORM(0X0E),
	SVCP_PACKET_TOO_BIG_FOR_VSIM(0X0F),
	VSIM_SET_FILE_FAILED_FILE_NOT_FOUND(0X10),
	VSIM_SET_FILE_FAILED_FILE_TOO_SMALL(0X11),
	VSIM_SET_FILE_FAILED_FILE_TOO_BIG(0X12),
	CLOUD_PROTOCOL_ERROR(0X13),
	CLOUD_PROTOCOL_INVALID_ARGUMENT(0X14),
	CLOUD_PROTOCOL_RESOURCE_UNAVAILABLE(0X15),
	CLOUD_PROTOCOL_REJECTED(0X16),
	CORRUPTED_PACKET_HAD_BEEN_RECEIVED(0X17),
	REUSE_RSIM_COMMAND_REJECTED(0X18),
	COMMAND_REJECTED_DURING_FILES_APPLY(0X19),
	CLOUD_PROTOCOL_NO_INTERNET_CONNECTION(0X1A),
	VSIM_FIRMWARE_CORRUPTED_IMAGE(0X1B),
	VSIM_FIRMWARE_WRITE_FAILURE(0x1C),
	/**
	 * Restarts FW Update procedure by host
	 */
	VSIM_FIRMWARE_INCOMPLETE_PAGE(0X1D), 
	WRONG_CRC(0X1E);
	
	private int value;

    ResultTags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
     
	/**
	 * Gets the ResultTags by value
	 * @param value
	 * @return ResultTags
	 */
	public static ResultTags getResultTag(int value) {
		for (ResultTags eResult : ResultTags.values())
			if (eResult.getValue() == value)
				return eResult;
		System.err.println("Unknown value (" + value + ") for Result Tag, return null");
		return null;
	}
}