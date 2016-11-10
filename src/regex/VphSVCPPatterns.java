package regex;

import java.io.File;
import java.util.regex.Pattern;

import global.Conversions;
import svcp.beans.SVCPMessage;

/**
 * Create a Template and pattern to extract SVCP hexString messages from logs
 * 
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
	private static final String RESPONSE_HEX_STRING = "[8-9a-fA-f]";
	public static final String SVCP_VERSION = "10";

	// TEMPLATES
	public static final String SVCP_TEMPLATE = VphGeneralPatterns.LOGCAT_DATE_TEMPLATE + "(?<" + SVCP_GROUP + ">"
			+ "(?<" + SVCP_HEADER_GROUP + ">" + "(?<" + SVCP_VERSION_GROUP + ">" + SVCP_VERSION + ")" + "(?<"
			+ SVCP_ID_GROUP + ">%1$s)" + "(?<" + SVCP_OPCODE_GROUP + ">%2$s)" + "(?<" + SVCP_LENGTH_GROUP
			+ ">[\\da-fA-f]{4})" + "(?<" + SVCP_CRC_GROUP + ">[\\da-fA-F]{2})" + ")" + "(?<" + SVCP_OPTIONAL_TLV_GROUP
			+ ">%3$s)?" + DEFAULT_HEX_STRING + "+)";

	/**
	 * Fill the format string with the following parameters or defaults
	 * 
	 * @param id
	 * @param opcode
	 * @param optionalTlvs
	 * @return String
	 */
	public static String getSVCPTemplate(String id, String opcode, String optionalTlvs) {
		id = (id == null) ? DEFAULT_HEX_STRING + "{2,2}" : id;
		opcode = (opcode == null) ? DEFAULT_HEX_STRING + "{2,2}"
				: opcode.length() < 2 ? RESPONSE_HEX_STRING + opcode : opcode;
		optionalTlvs = (optionalTlvs == null) ? DEFAULT_HEX_STRING : optionalTlvs;

		String format = String.format(SVCP_TEMPLATE, id, opcode, optionalTlvs);

		return format;
	}

	public static void main(String[] args) {
		// String length = "000B";
		// System.out.println(hexStringToDecimalInt);
		File f = new File("C:\\Users\\Yehuda\\Downloads\\2.1.1\\package.json");
		System.out.println(f);
		String svcps = "1001810037A78001012301090212342E342E312E32" + "3031363130" + "3236313530"
				+ "30040A676C6F62616C5F7670680E01FF10090000000000000000001101011096030022A7060101071D535643503A205265636569766564205374617475732072657175657374"
				+ "1098030022A9060101071D535643503A2053656"
				+ "E64696E672053746174757320726573706F6E7365109A030042CB060102073D5653494D3A2046697273742053564350206D6573736167652061727269766564202D2073656E6469"
				+"1001810037A78001012301090212342E"
				;
//		boolean hasMore = true;
//		printSvcps(svcps);
	}

	/**
	 * @param svcps
	 */
	public static void printSvcps(String svcps) {
		int headerLength = 12;
		int totalLength = svcps.length();
		int remainingLength = totalLength;

		int beginIndex = 0;
		int beginLengthIndex = 6;
		int endLengthIndex = beginLengthIndex + 4;

		while (remainingLength>0) {
			System.err.println("remaining:\t"+svcps.substring(beginIndex,totalLength));
			String substring = svcps.substring(beginLengthIndex, endLengthIndex);
			int hexStringToDecimalInt = Conversions.hexStringToDecimalInt(substring) * 2;
			System.out.println(substring);
			int packetLength = headerLength + hexStringToDecimalInt;

			if (remainingLength < packetLength){
				System.err.println("Wrong SVCP structure, length is too long"+svcps.substring(beginIndex,totalLength));
				break;
			}
			String singelSvcp = svcps.substring(beginIndex, beginIndex + packetLength);
			System.out.println(new SVCPMessage(singelSvcp));

			beginIndex += packetLength;
			beginLengthIndex = beginIndex + 6;
			endLengthIndex = beginLengthIndex + 4;
			remainingLength -= packetLength;
		}
		System.err.println("END");
	}

	/**
	 * Compile the template
	 * 
	 * @param id
	 * @param opcode
	 * @param optionalTlvs
	 * @return Pattern
	 */
	public static Pattern getSvcpPattern(String id, String opcode, String optionalTlvs) {
		return Pattern.compile(getSVCPTemplate(id, opcode, optionalTlvs),Pattern.CASE_INSENSITIVE);
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