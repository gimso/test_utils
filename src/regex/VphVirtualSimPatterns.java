package regex;

import java.util.regex.Pattern;

public class VphVirtualSimPatterns {
	//GROUPS
	public static final String AUTH_RESPONSE_RTT_GROUP = "rtt";

	//TEMPLATES
	//08-18 14:23:04.644 D/VirtualSim( 2996): USIM: RSIM post process using default values
	private static final String VIRTUAL_SIM_TEMPLATE = VphGeneralPatterns.LOGCAT_DATE_TEMPLATE +"[VDIWEC]\\/VirtualSim.*";
	
	//09-01 14:58:15.220 I/VSimControl( 3592): Received new RSim from servers
	private static final String RSIM_PROCESS_TEMPLATE = VIRTUAL_SIM_TEMPLATE + "RSIM post process.*";

	//09-01 14:58:24.670 D/VirtualSim( 3592):	AVFS: ME->Slave PDU HEADER: Got 'authentication'. Header(0088008122) Data(34) State(0x0005->0x0005)
	private static final String ATMEL_AUTH_REQUEST_TEMPLATE = VIRTUAL_SIM_TEMPLATE + "AVFS: ME->Slave PDU HEADER: Got 'authentication'\\..*";

	//09-01 14:58:25.330 D/VirtualSim( 3592): VSIM: Authentication received in 1980 ms (success)
	private static final String ATMEL_AUTH_RESPONSE_TEMPLATE = VIRTUAL_SIM_TEMPLATE+"VSIM: Authentication received in (?<"+AUTH_RESPONSE_RTT_GROUP+">\\d*).*";

	//PATTERNS
	public static final Pattern VIRTUAL_SIM_PATTERN = Pattern.compile(VIRTUAL_SIM_TEMPLATE);
	public static final Pattern RSIM_PROCESS_PATTERN = Pattern.compile(RSIM_PROCESS_TEMPLATE);
	public static final Pattern ATMEL_AUTH_REQUEST_PATTERN = Pattern.compile(ATMEL_AUTH_REQUEST_TEMPLATE);
	public static final Pattern ATMEL_AUTH_RESPONSE_PATTERN = Pattern.compile(ATMEL_AUTH_RESPONSE_TEMPLATE);
	
}
