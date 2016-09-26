package regex;

import java.util.regex.Pattern;

/**
 * Create a Template and pattern to extract SVCP hexString messages from logs
 * @author Yehuda
 *
 */
public class VphSVCPPatterns {
	// GROUPS
	public static final String SVCP_VERSION_GROUP = "version";
	public static final String SVCP_ID_GROUP = "id";
	public static final String SVCP_OPCODE_GROUP = "opcode";
	public static final String SVCP_LENGTH_GROUP = "length";
	public static final String SVCP_CRC_GROUP = "crc";
	public static final String SVCP_GROUP = "svcp";
	public static final String SVCP_HEADER_GROUP = "header";
	public static final String SVCP_OPTIONAL_TLV_GROUP = "tlvs";
	
	// HELPERS CONSTANTS
	public static final String DEFAULT_HEX_STRING = "[\\da-fA-f]";
	public static final String SVCP_VERSION = "10";

	// TEMPLATES
	public static final String SVCP_TEMPLATE = VphGeneralPatterns.LOGCAT_DATE_TEMPLATE + "(?<" + SVCP_GROUP + ">"
			+ "(?<" + SVCP_HEADER_GROUP + ">" + "(?<" + SVCP_VERSION_GROUP + ">" + SVCP_VERSION + ")" + "(?<"
			+ SVCP_ID_GROUP + ">%1$s)" + "(?<" + SVCP_OPCODE_GROUP + ">%2$s)" + "(?<" + SVCP_LENGTH_GROUP
			+ ">[\\da-fA-f]{4})" + "(?<" + SVCP_CRC_GROUP + ">[\\da-fA-F]{2})" + ")" + "(?<" + SVCP_OPTIONAL_TLV_GROUP
			+ ">%3$s)?" + DEFAULT_HEX_STRING + "+)";

	/**
	 * Fill the format string with the following parameters or defaults 
	 * @param id
	 * @param opcode
	 * @param optionalTlvs
	 * @return String
	 */
	public static String getSVCPTemplate(String id, String opcode, String optionalTlvs) {
		id = (id == null) ? DEFAULT_HEX_STRING + "{2,2}" : id;
		opcode = (opcode == null) ? DEFAULT_HEX_STRING + "{2,2}"
				: opcode.length() < 2 ? DEFAULT_HEX_STRING + opcode : opcode;
		optionalTlvs = (optionalTlvs == null) ? DEFAULT_HEX_STRING : optionalTlvs;

		return String.format(SVCP_TEMPLATE, id, opcode, optionalTlvs);
	}
	
	/**
	 * Compile the template
	 * @param id
	 * @param opcode
	 * @param optionalTlvs
	 * @return Pattern
	 */
	public static Pattern getSvcpPattern(String id, String opcode, String optionalTlvs) {
		return Pattern.compile(getSVCPTemplate(id, opcode, optionalTlvs));
	}

	/**
	 * in order to find authentication opcode, we must narrow the search and
	 * look for authentication data tlv as well
	 * 
	 * @return
	 */
	public static String authTlv() {
		String AUTHENTICATION_DATA_Type = "(1[dD])";// [0x1D]
		String AUTHENTICATION_DATA_Length = "([\\da-fA-F]{2})";
		String AUTHENTICATION_DATA_BeginValue = "[0aA]088";// SIM-USIM auth
		return AUTHENTICATION_DATA_Type + AUTHENTICATION_DATA_Length + AUTHENTICATION_DATA_BeginValue;
	}

}