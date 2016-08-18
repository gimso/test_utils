package regex;

public class VPHTestPatterns {
	public static final String SVCP_VERSION = "(10)";
	
	//Groups
	public static final int SERVER_AUTH_RESP_RT_GROUP = 3;
	public static final int ATMEL_AUTH_RESP_RT_GROUP = 3;
	public static final int AUTH_DATA_GROUP = 4;
	public static final int DATE_GROUP = 1;
	
	public static final String
		//08-04 09:20:59.359 		
		DATE = "^(\\d{2,2}-\\d{2,2}\\s\\d{2,2}:\\d{2,2}:\\d{2,2}\\.\\d{3,3}).*",
			
		//08-04 09:20:59.359 I/ServersControl( 3126): Network info: NetworkInfo: type: 3GMobile[HSDPA], state: CONNECTED/CONNECTED, reason: connected, extra: internetd.gdsp, roaming: true, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false, subscription: 0
		_3G_CONNECTION = DATE + "(Network info:).*(3GMobile\\[HSDPA\\]).*(state:\\sCONNECTED\\/CONNECTED)", 
		
		//08-18 14:23:04.644 D/VirtualSim( 2996): USIM: RSIM post process using default values
		RSIM = DATE + "(RSIM post process)",
		
		//08-09 11:16:40.990 D/VirtualSim( 3262): USIM: Got authentication. Header(0088008122) Data(34) State(0x0005->0x0005)
		ATMEL_GOT_AUTH_REQ = DATE + "(VirtualSim).*(USIM: Got authentication)",
			
		//08-09 11:16:16.170 I/VSimControl( 3262): Received handle data: id:-40 opcode:VSIM_AUTHENTICATION request data:Bundle[{authentication=008800812210646dedc7b59886939121f9e30006d3a710c16b555391218000db9b44d10bbbf403}]
		VSIM_CONTROL_GOT_AUTH_REQ = DATE +"(VSimControl).*(opcode:VSIM_AUTHENTICATION).*authentication=([\\da-fA-F]+)",
			
		//08-09 11:16:16.220 D/ServersControl( 3262): Sending cloud request to http://gw.skylab.simgo.me:80/api/v1/authentication/2850 1 {"auth_request":"008800812210646dedc7b59886939121f9e30006d3a710c16b555391218000db9b44d10bbbf403","device_id":"000040001016"}
		SERVERS_CONTROL_GOT_AUTH_REQ= DATE +"(ServersControl).*Sending cloud request to\\s(.*authentication\\/\\d+).*\"auth_request\":\"([a-fA-F\\d]+)",
			
		//08-09 11:16:19.750 D/ServersControl( 3262): Received response after: 3552 {"resp_status":{"info":"Success!","code":0},"auth_response":"db08f7f2de3b3e4555491015f62eb477bfbae8cc30fb2df7460dd710385a1fe26a96bef29a8849fd26fe72c6087b148386cc917b0b"}
		SERVERS_CONTROL_GOT_AUTH_RES  = DATE +"(ServersControl).*Received response after: (\\d+).*auth_response\":\"([\\da-fA-F]+)",
			
		//08-09 11:16:19.760 I/VSimControl( 3262): Received incoming message from servers service: AUTHENTICATION data: Bundle[mParcelledData.dataSize=368]
		VSIM_CONTROL_GOT_AUTH_RES = DATE +"(VSimControl).*Received incoming message.*AUTHENTICATION", 
			
		//08-09 11:16:44.050 D/VirtualSim( 3262): VSIM: Authentication received in 3360 ms (success)
		ATMEL_GOT_AUTH_RES = DATE +"(VirtualSim).*Authentication received in (\\d+)",
			
		//07-25 17:19:21.263 I/GUI     ( 3350): onDataConnectionStateChanged state:2 networkType:13
		_4G_CONNECTION = DATE +"(GUI).*onDataConnectionStateChanged\\s(state:2)";
	
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
