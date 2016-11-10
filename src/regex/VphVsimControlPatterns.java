 package regex;

import java.util.regex.Pattern;

public class VphVsimControlPatterns {
	//GROUPS
	public static final String AUTH_REQ_GROUP = "AuthReq";
	public static final String AUTH_REQ_SVCP_ID_GROUP = "id";
	public static final String AUTH_RESPONSE_TIME = "rtt";
	public static final String ATMEL_VERSION_GROUP = "fwVersion";
	
	//TEMPLATES
	
	//08-09 11:16:16.170 I/VSimControl( 3262): Received handle data: id:-40 opcode:VSIM_AUTHENTICATION request data:Bundle[{authentication=008800812210646dedc7b59886939121f9e30006d3a710c16b555391218000db9b44d10bbbf403}]
	public static final String VSIM_CONTROL_TEMPLATE = VphGeneralPatterns.LOGCAT_DATE_TEMPLATE +"[VDIWEC]\\/VSimControl.*";

	//08-28 17:32:24.392 I/VSimControl( 3479): Received handle data: id:1 opcode:STATUS response code: SUCCESS data:Bundle[{me_state=1, fw_version=4.3.1.201608221126, imsi=000000000000000000, me_type=9, configuration=global_vph, vsim_type=255}]
	public static final String ATMEL_VERSION_TEMPLATE = VSIM_CONTROL_TEMPLATE +"Received handle data.*opcode:STATUS.*fw_version=(?<"+ATMEL_VERSION_GROUP+">\\d\\.\\d\\.\\d\\.\\d+)";

	//08-09 11:16:16.170 I/VSimControl( 3262): Received handle data: id:-40 opcode:VSIM_AUTHENTICATION request data:Bundle[{authentication=008800812210646dedc7b59886939121f9e30006d3a710c16b555391218000db9b44d10bbbf403}]
	public static final String VSIM_AUTH_REQUEST_TEMPLATE = VSIM_CONTROL_TEMPLATE + "id:(?<"+AUTH_REQ_SVCP_ID_GROUP+">-?\\d+).*opcode:VSIM_AUTHENTICATION.*authentication=(?<"+AUTH_REQ_GROUP+">[\\da-fA-F]+)";
	
	//08-09 11:16:19.760 I/VSimControl( 3262): Received incoming message from servers service: AUTHENTICATION data: Bundle[mParcelledData.dataSize=368]
	public static final String VSIM_AUTH_RESPONSE_TEMPLATE = VSIM_CONTROL_TEMPLATE +"Received incoming message.*AUTHENTICATION.*";
	
	//PATTERNS
	public static final Pattern VSIM_CONTROL_PATTERN = Pattern.compile(VSIM_CONTROL_TEMPLATE);
	public static final Pattern VSIM_AUTH_REQUEST_PATTERN = Pattern.compile(VSIM_AUTH_REQUEST_TEMPLATE);
	public static final Pattern VSIM_AUTH_RESPONSE_PATTERN = Pattern.compile(VSIM_AUTH_RESPONSE_TEMPLATE);
	public static final Pattern ATMEL_VERSION_PATTERN = Pattern.compile(ATMEL_VERSION_TEMPLATE);
	
}
