package regex;

public class VPHTestPatterns {
	public static final String SVCP_VERSION = "(10)";

	/**
	 * The following groups included</br>
	 * Group 1 - the whole svcp message Group 2 - the svcp message header id
	 * Group 3 - the svcp message header opcode Group 4 - the svcp message
	 * header length
	 * 
	 * @param idGroup or default
	 * @param opcodeGroup or default
	 * @return pattern
	 */
	public static String svcpPattern(String id, String opcode,String optionalTlvs) {
		// using regex can define groups (with '(' and ')' ) then they can be
		// extracted from text
		// regex hex-string is \d (0-9) a-f, A-F.
		String defaultHexStr = "\\da-fA-f";

		String beginSVCPGroup = "(";
		String beginHeaderGroup = "(";
		// find and extract id
		id = (id == null)
				// if id is null, group the id using the defaultHexStr, the id
				// must be 2 digits
				? "([" + defaultHexStr + "]{2,2})"
				// else group by id as is
				: "(" + id + ")";

		// find and extract opcode
		opcode = (opcode == null)
				// if opcode is null, group the opcode using 0 or 8
				// (request/response) {1 digit} and hex string digit {1 digit},
				// the opcode must be 2 digits
				? "([08]{1,1}[" + defaultHexStr + "]{1,1})"
				// if got the opcode as one digit add 0 before is as default,
				// else used the opcode as is
				: opcode.length() < 2 ? "(0" + opcode + ")" : "(" + opcode + ")";

		// find and extract length, the length must be 4 digits
		String length = "([\\da-fA-f]{4})";
		String crc = "([\\da-fA-F]{2})";
		String endHeaderGroup = ")";
		String nextTlvs = "[\\da-fA-f]+";
		String endSVCPGroup = ")";
		/**
		 * Example: // 
		 * ( -start-svcp 
		 * ( -start-header 
		 * (10) -version
		 * ([\da-fA-f]{2,2}) -id 
		 * ([08]{1,1}[8]{1,1}) -opcode 
		 * ([\da-fA-f]{4})
		 * -length 
		 * ([\da-fA-F]{2}) -crc 
		 * ) -end-header
		 * (1[dD])([\da-fA-F]{2})[0aA]088 -optionlTlv 
		 * [\da-fA-f]+ 	-nextTlvs
		 * ) -end-svcp
		 * 
		 */
		// find and extract the whole SVCP
		String pattern = beginSVCPGroup + beginHeaderGroup + SVCP_VERSION + id + opcode + length + crc + endHeaderGroup + optionalTlvs + nextTlvs + endSVCPGroup;
		System.out.println("The SVCP pattern"+pattern);
		return  pattern;
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
