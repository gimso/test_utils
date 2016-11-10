package svcp.enums;

/**
 * 4.2. SVCP Payload An SVCP may have a payload following the SVCP header.</br>
 * The payload length shall be the length passed in the SVCP header. </br>
 * Tags order doesn't matter for functionality. </br>
 * Unknown tags will be ignored in the receiving side.</br>
 * A payload consists of one or more parameters. </br>
 * Each parameter shall be in the following TLV structures:</br>
 * 
 * @author Yehuda
 *
 */
public enum Tag {
	
	ALLOWED_MODULES(0x01),
    FW_VERSION(0x02),
    HW_VERSION(0x03),
    CONFIGURATION_NAME(0x04),
    MODE(0x05),
    LOG_LEVEL(0x06),
    LOG_LINE(0x07),
    UPDATE_SIZE(0x08),
    APPLY_UPDATE(0x09),
    PACKET_BEGIN(0x0A),
    PACKET_SIZE(0x0B),
    PACKET_PAYLOAD(0x0C),
    UICC_RELAY(0x0D),
    VSIM_TYPE(0x0E),
    SIM_GENERATION(0x0F),
    IMSI(0x10),
    POWER_SUPPLY_FROM_ME (0x11),
    ALLOW_TECHNICIAN_CODE(0x12),
	/**
	 * Cloud connection status report of the AP.</br>
	 * Encoding: One byte with the following encoding:</br>
	 * Host is disconnected from cloud 0x00</br>
	 * Host is connected from cloud 0x01</br>
	 * 
	 */
	CLOUD_3G_CONNECTION(0x13),
	/**
	 * ME modem connection status report of the AP.</br>
	 * Encoding: One byte with the following encoding:</br>
	 * Host is disconnected from cloud 0x00</br>
	 * Host is connected from cloud 0x01</br>
	 */
	ME_MODEM_4G_ONLINE(0x14),
	/**
	 * "Set Files" Options (0x15)</br>
	 * 1. Description: Defines options for "vSIM set files" (0x07) command,</br>
	 * out of the following options: Restore RSIM 0x00</br>
	 * In Home Country 0x01</br>
	 * Not In Home Country (Local SIM) 0x02</br>
	 * Unused 0xFF</br>
	 * 2. Encoding: One byte</br>
	 */
	SET_FILES_OPTIONS(0x15),
    // .. *Reserved for Future Use*
    FILE_PATH(0x19),
    FILE_ID(0x1A),
    FILE_TYPE(0x1B),
    FILE_DATA(0x1C),
    AUTHENTICATION_DATA(0x1D),
    APPLY_NOW(0x1F),
    PHONE_NUMBER(0x1E),//Add by @Yehuda 
    NAA_INIT(0x20),
    UICC_RESET(0x21),
    BOYCOTT(0x22),
    ME_TYPE(0x23),
    VSIM_CPU_RESET(0x24),
	/**
	 * Switch firmware versions (0x25)</br>
	 * 1. Description: </br>
	 * Host requests the vSIM to switch to the previously-stored firmware image
	 * (e.g., switch flash banks in Atmel), which should result loading the
	 * previous version and reboot of the Atmel CPU.</br>
	 * 2. Encoding: Empty payload</br>
	 */
	SWITCH_FIRMWARE_VERSIONS(0X25),
    // .. *Reserved for Future Use*
    RESULT_TAG(0x80),
    GET_ALL(0x81);

	private int value;

	Tag(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets Tag by value
	 * 
	 * @param value
	 * @return Tag
	 */
	public static Tag getTag(int value) {
		for (Tag tag : Tag.values()) {
			if (tag.getValue() == value) {
				return tag;
			}
		}
		System.err.println("Unknown value (" + value + ") for Tag, return null");
		return null;
	}
}