package regex;

import java.util.regex.Pattern;


public class VphServersPatterns {

	//GROUPS
	public static final String SPEED_KB_GROUP = "speed";
	public static final String SPEED_QUALITY_GROUP = "quality";
	public static final String MODEM_ROAMING_GROUP = "roaming";
	public static final String MODEM_APN_GROUP = "apn";
	public static final String NETWORK_INFO_REASON_GROUP = "reason";
	public static final String MODEM_NETWORK_TYPE_GROUP = "networkType";
	public static final String SERVERS_RESPONSE_RTT_GROUP = "rtt";
	public static final String URL_GROUP = "url";
	public static final String CLOUD_GROUP = "cloud";
	public static final String REQUEST_TYPE_GROUP = "requestType";
	public static final String ALLOCATION_ID_GROUP = "allocationId";
	public static final String AUTH_REQUEST_GROUP = "authRequest";
	public static final String SERVERS_AUTH_RESPONSE_MSG_ID_GROUP = "msgId";

	//TEMPLATES:	
	//08-21 19:40:37.101 D/ServersControl( 3294): Received 4G network change: NetworkInfo: type: mobile[LTE], state: CONNECTED/CONNECTED, reason: connected, extra: uwap.orange.co.il, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false, subscription: 0
	public static final String SERVERS_CONTROL_TEMPLATE = VphGeneralPatterns.LOGCAT_DATE_TEMPLATE  +"[VDIWEC]\\/ServersControl.*";

	//09-04 15:34:24.429 D/ServersControl4G( 3365): Sending cloud request to http://gw.staging.gimso.net:80/api/v1/allocation/102929 2 {"device_params":{"device_id":"000040001016"},"timestamp":1472992464,"secondary_modem":{"rssi":-96},"simgo_network_registration":{"rssi":18},"modem_used":1}
	public static final String URL_TEMPLATE = 
			"(?<" + URL_GROUP + ">http:\\/\\/gw\\." + 
			"(?<" + CLOUD_GROUP + ">\\w+\\.\\w+\\.\\w+):80\\/api\\/v1\\/" +
			"(?<" + REQUEST_TYPE_GROUP + ">\\w+)\\/" + 
			"(?<" + ALLOCATION_ID_GROUP + ">\\d+))";
		
	//09-01 14:58:12.950 I/ServersControl( 3592): Network info: NetworkInfo: type: 3GMobile[HSDPA], state: CONNECTED/CONNECTED, reason: connected, extra: internetd.gdsp, roaming: true, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false, subscription: 0
	public static final String MODEM_3G_CONNECTED_TEMPLATE =  SERVERS_CONTROL_TEMPLATE+"NetworkInfo:.*"
			+ "3GMobile\\[(?<"+MODEM_NETWORK_TYPE_GROUP+">\\w+)].*"
			+ "state:\\sCONNECTED\\/CONNECTED.*"
			+ "reason: (?<"+NETWORK_INFO_REASON_GROUP+">.*?), "
			+ "extra: (?<"+MODEM_APN_GROUP+">.*?),"
			+ " roaming: (?<"+MODEM_ROAMING_GROUP+">\\w*),";
	
	// 09-08 03:12:13.450 I/ServersControl( 3812): onStartCommand with intent action com.simgo.BOOT_COMPLETED
	public static final String BOOT_COMPLETED_TEMPLATE =  SERVERS_CONTROL_TEMPLATE + "com\\.simgo\\.BOOT_COMPLETED";
			
	//	09-01 12:15:48.984 I/ServersControl:ServersManager( 3420): Sending authentication request: 008800812210d0504abc5040ff7d7fb5717c2bc634e8105160b0f731eb8000fddccf1255a4083c
	public static final String SERVERS_AUTH_REQUEST_TEMPLATE = SERVERS_CONTROL_TEMPLATE + 
			"ServersManager.*Sending authentication request: (?<" + AUTH_REQUEST_GROUP + ">0088"+VphSVCPPatterns.DEFAULT_HEX_STRING+"+)";
	
	//09-01 15:56:17.614 I/ServersControl:ServersManager( 3012): Received response: SVCP message id(0xf0) opcode(AUTHENTICATION) Response:SUCCESS after:2033
	public static final String SERVERS_AUTH_RESPONSE_TEMPLATE  = SERVERS_CONTROL_TEMPLATE 
			+ "ServersManager.*"
			+ "Received response: SVCP message id\\(0x(?<"+SERVERS_AUTH_RESPONSE_MSG_ID_GROUP+">"+VphSVCPPatterns.DEFAULT_HEX_STRING+"{1,2}).*opcode\\(AUTHENTICATION\\) Response:SUCCESS after:"
			+ "(?<"+SERVERS_RESPONSE_RTT_GROUP+">\\d+).*";
	
	//09-01 14:58:58.328	D/ServersControl( 3592): Received 4G network change: NetworkInfo: type: mobile[LTE], state: CONNECTED/CONNECTED, reason: connected, extra: uwap.orange.co.il, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false, subscription: 0
	public static final String MODEM_4G_CONNECTED_TEMPLATE = SERVERS_CONTROL_TEMPLATE 
			+ "Received 4G network change: NetworkInfo:.*"
			+ "mobile\\[(?<"+MODEM_NETWORK_TYPE_GROUP+">.*)\\].*state:\\sCONNECTED\\/CONNECTED.*"
			+ "reason: (?<"+NETWORK_INFO_REASON_GROUP+">\\w+).*"
			+ "extra: (?<"+MODEM_APN_GROUP+">[\\w.]+), "
			+ "roaming: (?<"+MODEM_ROAMING_GROUP+">true|false)" 	;
	
	//08-25 01:12:21.486 I/ServersControl( 4126): Received Data speed quality: EXCELLENT KBits per second: 9842,92
	public static final String SERVERS_SPEED_TEST_TEMPLATE  =  SERVERS_CONTROL_TEMPLATE 
			+ "Received Data speed quality: (?<"+SPEED_QUALITY_GROUP+">\\w+) "
			+ "KBits per second: (?<"+SPEED_KB_GROUP+">[\\d,]+)";
	
	// PETTERNS //
	public static final Pattern SERVERS_CONTROL_PATTERN = Pattern.compile(SERVERS_CONTROL_TEMPLATE);
	public static final Pattern MODEM_3G_CONNECTED_PATTERN = Pattern.compile(MODEM_3G_CONNECTED_TEMPLATE);
	public static final Pattern SERVERS_AUTH_REQUEST_PATTERN = Pattern.compile(SERVERS_AUTH_REQUEST_TEMPLATE);
	public static final Pattern SERVERS_SPEED_TEST_PATTERN = Pattern.compile(SERVERS_SPEED_TEST_TEMPLATE);
	public static final Pattern SERVERS_AUTH_RESPONSE_PATTERN = Pattern.compile(SERVERS_AUTH_RESPONSE_TEMPLATE);
	public static final Pattern MODEM_4G_CONNECTED_PATTERN = Pattern.compile(MODEM_4G_CONNECTED_TEMPLATE);
	public static final Pattern BOOT_COMPLETED_PATTERN = Pattern.compile(BOOT_COMPLETED_TEMPLATE);
	
}
