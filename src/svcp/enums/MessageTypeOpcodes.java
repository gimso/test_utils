package svcp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Each SVCP opcode maps to specific action in the SVCP protocol.</br>
 * When the opcode has its Most Significant Bit set to 0,</br>
 * it is a request opcode. When the opcode has its MSB</br>
 * set to 1, it is a response opcode.</br>
 * For example: 0x01 is Status request while 0x81 is Status response. </br>
 * * @author Yehuda
 */
public enum MessageTypeOpcodes {

	/**
	 * <b> Status (0x01) </b></br>
	 * Status opcode can be used to query vSIM for its status. If sent without
	 * any parameters, the status opcode operates as a keep-alive mechanism
	 */
	STATUS(0x01),
	/**
	 * <b>Set Logging (0x02) </b></br>
	 * Set logging opcode can be used to set the logging mode of the vSIM
	 */
	SET_LOGGING(0x02),
	/**
	 * <b>Log Line (0x03) </b></br>
	 * Log line opcode is used by the vSIM to send a log line
	 */
	LOG_LINE(0x03),
	/**
	 * <b>vSIM Firmware update (0x04)</b></br>
	 * vSIM firmware update opcode is used to update the firmware of the vSIM.
	 * The process of updating the vSIM firmware is made of three steps: 1.
	 * Prepare the update – in this step the size of the new firmware is sent to
	 * the vSIM and any previously prepared firmware (or parts of it) are
	 * discarded. 2. Firmware packets – in this steps the firmware's payload is
	 * sent to the vSIM. Each firmware packet will indicate its beginning point,
	 * its size, and its payload. Firmware packets shall be sent in consecutive
	 * order. 3. Apply update – in this step the new firmware will be applied to
	 * the vSIM.
	 */
	VSIM_FIRMWARE_UPDATE(0x04),
	/**
	 * <b>vSIM Configuration Update (0x05)</b></br>
	 * vSIM configuration update opcode is used to update the configuration of
	 * the vSIM. The process of updating the vSIM configuration is the same as
	 * in vSIM firmware update.
	 */
	VSIM_CONFIGURATION_UPDATE(0x05),
	/**
	 * <b>vSIM relays (0x06) </b></br>
	 * vSIM relays opcode is used to control the vSIM UICC relays. Possible
	 * relays are differ from platform to platform, and it is the responsibility
	 * of the sender to verify the request relay is supported.
	 */
	VSIM_RELAYS(0x06),
	/**
	 * <b>vSIM set files (0x07) </b></br>
	 * This opcode can be used to set specific files in vSIM. "Apply" tags
	 * should always be accompanied to a file (usually the last file in a series
	 * of files), and never as a standalone tag. Once an "Apply" tag would be
	 * received in the vSIM side, the latter will reject new "Set files"
	 * commands until the apply process has been completed. "Apply Now" tag will
	 * activate the pre-configured default method for the specific ME, while
	 * other "Apply" types will override the default methods.
	 */
	SET_FILES(0x07),
	/**
	 * <b>Authentication (0x08)</b></br>
	 * Apply for Authentication request from ATMEL and responses from AP
	 */
	VSIM_AUTHENTICATION(0x08),
	/**
	 * <b>Outgoing Call (0x09)</b></br>
	 * This opcode is used for initiating an outgoing voice call
	 */
	VSIM_OUTGOING_CALL(0x09),
	/**
	 * <b>Outgoing SMS (0x0A)</b></br>
	 * This opcode is used for initiating an outgoing SMS.
	 */
	VSIM_OUTGOING_SMS(0x0A),
	/**
	 * <b>Reset vSIM (0x0B) </b></br>
	 * Reset command is used to perform reset/refresh actions to the vSIM
	 */
	RESET_VSIM(0x0B),
	/**
	 * <b>vSIM Type In Use (0x0C) </b></br>
	 * This opcode is used for either: 1. When initiated by host: Instructing
	 * the vSIM to set FSIM / previously-used RSIM, aka "Use FSIM" /
	 * "re-use RSIM". 2. When initiated by vSIM: Notify the host that FSIM had
	 * been autonomously applied. Remark: "Use FSIM" is Not applicable for VPH
	 * products
	 */
	VSIM_TYPE_IN_USE(0x0C),
	/**
	 * <b>General Request / Response (0x0D / 0x8D)</b></br>
	 * General request is still not defined (TBD). General response opcode can
	 * be used to response with general information which is not related to a
	 * specific request. E.g., notification of received corrupted packet.
	 */
	GENERAL_RESPONSE(0x0D),
	/**
	 * <b>ME_CONTROL(0x0E)</b></br>
	 * Currently relevant only for Virtual portable hotspot (VPH) products. This
	 * opcode allows the vSIM to request specific ME actions from host, in
	 * devices where AP controls the ME.
	 */
	ME_CONTROL(0x0E);

	private int value;
	private static final Map<Integer, MessageTypeOpcodes> OPCODE_MAP = new HashMap<>();;

	MessageTypeOpcodes(int value) {
		this.value = value;
	}

	/**
	 * initial the map dynamically to avoid value changes
	 */
	static {
		for (MessageTypeOpcodes opcode : MessageTypeOpcodes.values())
			OPCODE_MAP.put(opcode.getValue(), opcode);
	}

	public int getValue() {
		return value;
	}

	/**
	 * Add "0x80" to value what makes the Most Significant Bit to 1
	 * 
	 * @return
	 */
	public int getResponseValue() {
		return value + 0x80;
	}

	/**
	 * Gets the opcode by its value
	 *
	 * @param value
	 *            - The value of the opcode
	 * @return EOpcode the opcode or null
	 * 
	 */
	public static MessageTypeOpcodes getMessageTypeOpcode(int value) {
		for (Map.Entry<Integer, MessageTypeOpcodes> entry : OPCODE_MAP.entrySet())
			if (value == entry.getKey())
				return entry.getValue();
			else if (((value & 0xff) - 0x80) == entry.getKey())
				return entry.getValue();

		System.err.println("Unknown value (" + value + ") for MessageTypeOpcodes, return null");
		return null;
	}

}