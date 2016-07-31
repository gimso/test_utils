package svcp.util;

import global.Conversions;
import svcp.beans.TLV;
import svcp.enums.ApplyUpdate;
import svcp.enums.CloudConnection;
import svcp.enums.FileType;
import svcp.enums.LogLevel;
import svcp.enums.METype;
import svcp.enums.Mode;
import svcp.enums.PowerSupplyFromMe;
import svcp.enums.ResultTags;
import svcp.enums.SimGeneration;
import svcp.enums.Tag;
import svcp.enums.UICCRelay;
import svcp.enums.VsimType;

/**
 * Sample cvsp's tlv's and header
 * 
 * @author Yehuda
 *
 */
public class SVCPTag {

	/**
	 * Create me_type tlv</br>
	 * <i> --- It's not fully implemented yet, in the mean time we treating it
	 * as 0-9 Hex numbers, not ASCII --- </i></br>
	 * ME Type (0x23)</br>
	 * Description: The mobile equipment type</br>
	 * Encoding: <Any number of Bytes.?></br>
	 * Example:< ..></br>
	 * 
	 * @return byte[]
	 */
	public static TLV meType(METype meType) {
		if(meType == null)
			return new TLV (Tag.ME_TYPE);
		else
			return new TLV(Tag.ME_TYPE, meType.getValue());
		
	}

	/**
	 * Create svcp.config-name tlv 5.4. Configuration name (0x04)</br>
	 * Description: The configuration name loaded to the vSIM.</br>
	 * Encoding: Any number of ASCII characters, where each character is
	 * alpha-numeric.</br>
	 * Example: "2.0.0-3"
	 * 
	 * @param configName
	 * @return byte[]
	 */
	public static TLV configName(String configName) {
		if(configName == null)
			return new TLV (Tag.CONFIGURATION_NAME);
		else
			return new TLV(Tag.CONFIGURATION_NAME, Conversions.stringToASCIIByteArray(configName));
	}

	/**
	 * Create hw-version tlv </br>
	 * 5.3. HW Version (0x03)</br>
	 * Description: The HW version the vSIM is configured to run on.</br>
	 * Encoding: Any number of ASCII characters, where each character is between
	 * "0" and "9", or "." (dot) or "-" (hyphen).</br>
	 * Example: "2.0.0-3"
	 * 
	 * @param hwVersion
	 * @return byte[]
	 */
	public static TLV hwVersion(String hwVersion) {
		if(hwVersion == null)
			return new TLV (Tag.HW_VERSION);
		else
			return new TLV(Tag.HW_VERSION, Conversions.stringToASCIINumericByteArray(hwVersion));
	
	}

	/**
	 * Create fw-version tlv 5.2. FW Version (0x02)</br>
	 * Description: The FW version running on the vSIM.</br>
	 * Encoding: Any number of ASCII characters, where each character is between
	 * "0" and "9", or "." (dot) or "-" (hyphen).</br>
	 * Example: "1.4.05-3"
	 * 
	 * @param fwVersion
	 * @return byte[]
	 */
	public static TLV fwVersion(String fwVersion) {
		if(fwVersion == null)
			return new TLV (Tag.FW_VERSION);
		else
			return new TLV(Tag.FW_VERSION, Conversions.stringToASCIINumericByteArray(fwVersion));
	}

	/**
	 * Create result tag with OK value (0x00) Result Tag (0x80) </br>
	 * Description: A tag for the result code.</br>
	 * Encoding: One Byte.</br>
	 * Example: ..</br>
	 * 
	 * @return byte[] result tlv
	 */
	public static TLV resultTag(ResultTags resultTag) {
		byte value = (byte) resultTag.getValue();
		return new TLV(Tag.RESULT_TAG, value);
	}

	/**
	 * Create vsim id TLV 5.1. vSIM ID (0x01) </br>
	 * Description: A unique ID given to each vSIM. </br>
	 * Encoding: 12 bytes where each byte is between "0" and "9". </br>
	 * Example: 000010004321
	 * 
	 * @param vsimId
	 * 
	 * @return byte[]
	 */
	public static TLV vsimId(String vsimId) {
		if(vsimId == null)
			return new TLV (Tag.VSIM_ID);
		else
			return new TLV(Tag.VSIM_ID, Conversions.stringNumsToByteArray(vsimId));
	}

	/**
	 * Mode (0x05) </br>
	 * Description: Enable or Disable. </br>
	 * Encoding: One byte with the following encoding: </br>
	 * Description Encoding </br>
	 * Disable 0x00 Enable 0x01
	 * 
	 * @return mode-enable
	 */
	public static TLV mode(Mode mode) {
		if(mode == null)
			return new TLV (Tag.MODE);
		else
			return new TLV(Tag.MODE, mode.getValue());
	}

	/**
	 * Log Level (0x06) </br>
	 * Description: </br>
	 * Level of Log line. Encoding: One ASCII character with the following
	 * meaning: </br>
	 * ASCII Debug level: "D" Debug, "I" Information, "W" Warning, "E" Error,
	 * "C" Critical.
	 * 
	 * @return log-level_Debug
	 */
	public static TLV logLevel(LogLevel logLevel) {
		byte valueLogLevel = logLevel.getValue();
		return new TLV(Tag.LOG_LEVEL, valueLogLevel);
	}

	/**
	 * Log Line (0x07)</br>
	 * Description: A line of log from the vSIM.</br>
	 * Encoding: An ASCII string, shall not be null terminated.</br>
	 * Example: "This is a log line"</br>
	 * <b> Log line opcode does not have any response opcode.</b>
	 * 
	 * @param logLine
	 * 
	 * @return tlv log-line
	 */
	public static TLV logLine(String logLine) {
		byte[] value = Conversions.stringToASCIIByteArray(logLine);
		return new TLV(Tag.LOG_LINE, value);
	}

	/**
	 * Update Size (0x08) </br>
	 * Description: Size of an update to the vSIM</br>
	 * Encoding: Four bytes</br>
	 * Example: 0x00037D10 (update has size of 228624 bytes)
	 * 
	 * @param updateSize
	 * @return byte[]
	 */
	public static TLV updateSize(long updateSize) {
		byte[] value = Conversions.intToFourBytesArray(updateSize);
		return new TLV(Tag.UPDATE_SIZE, value);
	}

	/**
	 * Apply Update (0x09)</br>
	 * Description: Request to apply pending vSIM firmware</br>
	 * Encoding: One byte with the following encoding:</br>
	 * Description Encoding</br>
	 * for Apply update and perform soft-reset 0x00</br>
	 * for Apply update and wait for external reset 0x01</br>
	 * 
	 * @return byte[]
	 */
	public static TLV applyUpdate(ApplyUpdate applyUpdate) {
		byte value = (byte) applyUpdate.getValue(); // external-reset
		return new TLV(Tag.APPLY_UPDATE, value);
	}

	/**
	 * Packet Begin (0xA)</br>
	 * Description: The location of the first byte of the packet in the complete
	 * update</br>
	 * Encoding: Four bytes</br>
	 * Example: 0x00037000 (the packet begins at 225280)</br>
	 * 
	 * @param packetBegin
	 * @return byte[]
	 */
	public static TLV packetBegin(long packetBegin) {
		byte[] value = Conversions.intToFourBytesArray(packetBegin);
		return new TLV(Tag.PACKET_BEGIN, value);
	}

	/**
	 * Packet Size (0xB)</br>
	 * Description: The size of a packet</br>
	 * Encoding: Two bytes</br>
	 * Example: 0x05DC (the packet size is 1500 bytes)</br>
	 * 
	 * @param packetSize
	 * 
	 * @return byte[]
	 */
	public static TLV packetSize(int packetSize) {
		byte[] value = Conversions.intToTwoBytesArray(packetSize);
		return new TLV(Tag.PACKET_SIZE, value);
	}

	/**
	 * Packet Payload (0x0C)</br>
	 * Description: The payload of the packet</br>
	 * Encoding: Any number of bytes</br>
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static TLV packetPayload(byte[] value) {
		return new TLV(Tag.PACKET_PAYLOAD, value);
	}

	/**
	 * 6. vSIM relays (0x06) vSIM relays opcode is used to control the vSIM UICC
	 * relays.</br>
	 * Possible relays are differ from platform to platform, and it is the
	 * responsibility of the sender to verify the request relay is
	 * supported. </br>
	 * vSIM relays request shall have the following parameters: Offset Tag name
	 * Encoding M/O Length</br>
	 * 0 UICC relay 0xD O 2</br>
	 * 2 UICC relay 0xD O 2</br>
	 * 
	 * Offset*2 UICC relay 0xD O 2</br>
	 * </br>
	 * Description: A UICC relay in the platform. Depending on the platform it
	 * uses, the vSIM can control different relays to achieve different
	 * functionality. </br>
	 * A UICC relay tag defines the UICC source and UICC destination.</br>
	 * </br>
	 * Encoding: Two bytes. First byte is for the UICC source (ISO 7816 slave)
	 * and the second byte is the UICC destination (ISO 7816 master). Values
	 * shall have the following encoding:</br>
	 * Embedded UICC 1 0x00</br>
	 * Embedded UICC 2 0x01</br>
	 * Physical UICC 0x02</br>
	 * Virtual UICC (Source) / vSIM (Destination) 0x03 </br>
	 * External baseband 0x04</br>
	 * Internal baseband 1 0x05</br>
	 * Internal baseband 2 0x06</br>
	 * </br>
	 * </br>
	 * Examples:</br>
	 * 1. (0x00, 0x05)  Connect embedded UICC 1 to internal baseband 1, where
	 * internal baseband 1 is reading from embedded UICC 1.</br>
	 * 2. (0x02, 0x04)  Connect physical UICC to external baseband, where
	 * external baseband is reading from physical UICC.</br>
	 * 3. (0x03, 0x04)  Connect virtual UICC to external baseband, where
	 * external baseband is reading from virtual UICC (vSIM).</br>
	 * 4. (0x02, 0x03)  Connect physical UICC to vSIM, where virtual UICC
	 * (vSIM) is reading from physical UICC.</br>
	 * </br>
	 * Values Description:</br>
	 * 1. Embedded UICC 1 (0x00)  An embedded Data SIM card number 1
	 * (source).</br>
	 * 2. Embedded UICC 2 (0x01)  An embedded Data SIM card number 2
	 * (source).</br>
	 * 3. Physical UICC (0x02) - A physical SIM card, inserted in the Home SIM
	 * slot (source).</br>
	 * 4. Virtual UICC (vSIM) (0x03) - A virtual SIM card (ISO7816 Slave/Master
	 * vSIM interface), (can be used as either source or destination).</br>
	 * 5. External baseband (0x04) - The mobile equipment (ME) which uses Simgo
	 * solution (e.g., customer's cellphone), (destination).</br>
	 * 6. Internal baseband 1 (0x05) - Cover's internal Cellular module, SIM
	 * slot number 1 (destination).</br>
	 * 7. Internal baseband 2 (0x06) - Cover's internal Cellular module, SIM
	 * slot number 2 (destination).</br>
	 * 
	 */
	public static TLV uiccRelays(UICCRelay src, UICCRelay dest) {
		byte[] value = new byte[] { src.getValue(), dest.getValue() };
		return new TLV(Tag.UICC_RELAY, value);
	}

	/**
	 * 5.18. File Data (0x1C)</br>
	 * 1. Description: Send the file content data for the "vSIM set files"
	 * (0x07) command.</br>
	 * 2. Encoding: Any number of bytes.</br>
	 * 3. Example: ..</br>
	 * 
	 * @param value
	 *
	 * @return
	 */
	public static TLV fileData(byte[] value) {
		return new TLV(Tag.FILE_DATA, value);
	}

	/**
	 * 5.15. File Path (0x19)</br>
	 * 1. Description: Chooses file path for the "vSIM set files" (0x07)
	 * command.</br>
	 * 2. Encoding: Two bytes.</br>
	 * 3. Example: See "File data" tag (0x1C) examples.</br>
	 * 
	 * @return
	 */
	public static TLV filePath(byte[] path) {
		return new TLV(Tag.FILE_PATH, path);
	}

	/**
	 * 5.16. File ID (0x1A)</br>
	 * 1. Description: Chooses file ID for the "vSIM set files" (0x07)
	 * command.</br>
	 * 2. Encoding: Two bytes.</br>
	 * 3. Example: See "File data" tag (0x1C) examples.</br>
	 * 
	 * @return
	 */
	public static TLV fileId(byte[] fileId) {
		return new TLV(Tag.FILE_ID, fileId);
	}

	/**
	 * 5.21. Apply Now (0x1F)</br>
	 * 1. Description: Apply the update immediately. This tag usually refers to
	 * the "vSIM set files" (0x07) command.</br>
	 * 2. Encoding: One Byte.</br>
	 * 3. Example: ..</br>
	 *
	 * @return
	 */
	public static TLV applyNow() {
		return new TLV(Tag.APPLY_NOW);
	}

	/**
	 * 5.17. File Type (0x1B)</br>
	 * 1. Description: Chooses file Type for the "vSIM set files" (0x07)
	 * command, out of the following options (Two values can be or'd):</br>
	 * File Type Encoding</br>
	 * Master File (root) 0x00</br>
	 * Directory File 0x01</br>
	 * Elementary File 0x02</br>
	 * Linear File 0x04</br>
	 * Transparent 0x08</br>
	 * File Control Parameters (FCP) 0xFF</br>
	 * 2. Encoding: One byte.</br>
	 * 3. Example: See "File data" tag (0x1C) examples.</br>
	 * 
	 * @return
	 */
	public static TLV fileType(FileType fileType) {
		byte value = fileType.getValue();
		return new TLV(Tag.FILE_TYPE, value);
	}

	/**
	 * 5.19. Authentication Data (0x1D)</br>
	 * 1. Description: Authentication data for SIM unit, or Authentication
	 * response for ME.</br>
	 * 2. Encoding: Any number of bytes.</br>
	 * 3. Example: ..</br>
	 * 
	 * @param authData
	 * 
	 * @return
	 */
	public static TLV authenticationData(String authData) {
		byte[] value = Conversions.stringToASCIIByteArray(authData);
		return new TLV(Tag.AUTHENTICATION_DATA, value);
	}

	/**
	 * 5.20. Phone Number (0x1E)</br>
	 * 1. Description: Outgoing Call / SMS data for the cloud.</br>
	 * 2. Encoding: Any number of bytes.</br>
	 * 3. Example: ..</br>
	 * 
	 * @param phoneNumber
	 * 
	 * @return
	 */
	public static TLV phoneNumber(String phoneNumber) {
		byte[] value = Conversions.stringToASCIIByteArray(phoneNumber);
		return new TLV(Tag.PHONE_NUMBER, value);
	}

	/**
	 * 5.22. Apply after NAA Init (0x20)</br>
	 * 1. Description: Apply the update following an NAA initialization (Chapter
	 * 6.4.7 REFRESH, in ETSI TS 102 223 V12.0.0 (2014-05)).</br>
	 * <a href=
	 * "https://www.etsi.org/deliver/etsi_ts/102200_102299/102223/12.00.00_60/ts_102223v120000p.pdf">
	 * link here, page 42</a></br>
	 * This tag usually refers to the "vSIM set files" (0x07) command.</br>
	 * 2. Encoding: One Byte.</br>
	 * 3. Example: ..</br>
	 */
	public static TLV naaInit() {
		return new TLV(Tag.NAA_INIT);
	}

	/**
	 * 5.23. Apply after UICC reset (0x21)</br>
	 * 1. Description: Apply the update following a UICC reset (Chapter 6.4.7
	 * REFRESH, in ETSI TS 102 223 V12.0.0 (2014-05)).</br>
	 * This command refers to the "vSIM set files" (0x07) command.</br>
	 * <a href=
	 * "https://www.etsi.org/deliver/etsi_ts/102200_102299/102223/12.00.00_60/ts_102223v120000p.pdf">
	 * link here, page 42</a></br>
	 * 2. Encoding: One Byte.</br>
	 * 3. Example:</br>
	 */
	public static TLV uiccReset() {
		return new TLV(Tag.UICC_RESET);
	}

	/**
	 * 5.24. Apply after boycott (0x22)</br>
	 * 1. Description: Apply the update following a boycott action (ignore
	 * phone). This command refers to the "vSIM set files" (0x07) command.</br>
	 * 2. Encoding: One Byte.</br>
	 * 3. Example: ..</br>
	 */
	public static TLV boycott() {
		return new TLV(Tag.BOYCOTT);
	}

	/**
	 * 5.27. "Get All" (0x81)</br>
	 * 1. Description: A general command for receiving all the relevant
	 * tags.</br>
	 * 2. Encoding: One byte.</br>
	 * 3. Example: ..</br>
	 */
	public static TLV getAll() {
		return new TLV(Tag.GET_ALL);
	}

	/**
	 * 1.14. vSIM type (0x0E)</br>
	 * 1. Description: Chooses vSIM file type "vSIM type in use" (0x0C)
	 * command.</br>
	 * Description Encoding</br>
	 * FSIM 0x00</br>
	 * RSIM 0x01</br>
	 * 2. Encoding: One byte.</br>
	 * 
	 * @param vsimType
	 * @return TLV
	 */
	public static TLV vsimType(VsimType vsimType) {
		if (vsimType == null)
			return new TLV(Tag.VSIM_TYPE);
		else
			return new TLV(Tag.VSIM_TYPE, (byte)vsimType.getValue());
	}
	
	/**
	 * 1.15. SIM generation (0x0F)</br>
	 * 4. Description: Chooses SIM card generation (SIM / USIM)</br>
	 * Description Encoding</br>
	 * SIM (2G) 0x00</br>
	 * USIM (3G) 0x01</br>
	 * 5. Encoding: One byte.</br>
	 * 
	 * @param vsimType
	 * @return TLV
	 */
	public static TLV simGeneration(SimGeneration simGeneration) {
		if (simGeneration == null)
			return new TLV(Tag.SIM_GENERATION);
		else
			return new TLV(Tag.SIM_GENERATION, simGeneration.getValue());
	}
	
	/**
	 * 1.16. IMSI (0x10)</br>
	 * 1. Description: Data of the unique international mobile subscriber
	 * identity (IMSI) currently in use.</br>
	 * 2. Encoding: 15 ASCII characters where each character is between "0" and
	 * "9".</br>
	 * 3. Example:</br>
	 * 
	 * @param simGeneration</br>
	 * @return TLV
	 */
	public static TLV imsi(String imsi) {
		if (imsi == null)
			return new TLV(Tag.IMSI);
		else
			return new TLV(Tag.IMSI, Conversions.stringToASCIIByteArray(imsi));
	}
	
	/**
	 * 1.29. vSIM CPU Reset (0x24) </br>
	 * Description: Trigger a complete vSIM CPU Reset,</br>
	 * which will result in a power cycle and reboot of the Atmel CPU.</br>
	 * Encoding: Empty payload.</br>
	 * 
	 */
	public static TLV vsimCpuReset() {
		return new TLV(Tag.VSIM_CPU_RESET);
	}
	
	/**
	 * 6.17. Power Supply from ME (0x11) </br>
	 * 1. Description: Status of the power supply from ME to vSIM. </br>
	 * 2. Encoding: One byte with the following encoding:v Description
	 * Encoding </br>
	 * Power supply from ME is Off 0x00 </br>
	 * Power supply from ME is ON 0x01v
	 * 
	 * @param powerSupplyFromMe
	 * @return
	 */
	public static TLV powerSupplyFromME(PowerSupplyFromMe powerSupplyFromMe) {
		if (powerSupplyFromMe != null)
			return new TLV(Tag.POWER_SUPPLY_FROM_ME, powerSupplyFromMe.getValue());
		else
			return new TLV(Tag.POWER_SUPPLY_FROM_ME);
	}

	/**
	 * 
	 * Cloud Connection (0x13) </br>
	 * 1. Description: Cloud connection status report of the AP. </br>
	 * 2. Encoding: One byte with the following encoding: </br>
	 * Description Encoding </br>
	 * Host is disconnected from cloud 0x00 </br>
	 * Host is connected from cloud 0x01 </br>
	 */
	public static TLV cloudConnection(CloudConnection cloudConnection) {
		if (cloudConnection == null)
			return new TLV(Tag.CLOUD_CONNECTION);
		else
			return new TLV(Tag.CLOUD_CONNECTION, (byte) cloudConnection.getValue());
	}
	
	/**
	 * Switch firmware versions (0x25)</br>
	 * 1. Description: </br>
	 * Host requests the vSIM to switch to the previously-stored firmware image
	 * (e.g., switch flash banks in Atmel), which should result loading the
	 * previous version and reboot of the Atmel CPU.</br>
	 * 2. Encoding: Empty payload</br>
	 */
	public static TLV switchFirmwareVersions() {	
		return new TLV(Tag.SWITCH_FIRMWARE_VERSIONS);
	}

}