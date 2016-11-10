package regex;

import java.util.regex.Pattern;

public class VphGUIPatterns {
	//TEMPLATES
	//07-25 17:19:21.263 I/GUI  
	private static final String GUI_MODULE_TEMPLATE =	 VphGeneralPatterns.LOGCAT_DATE_TEMPLATE +"[VDIWEC]\\/GUI.*";

	//07-25 17:19:21.263 I/GUI     ( 3350): onDataConnectionStateChanged state:2 networkType:13
	private static final String MODEM_4G_CONNECTED_TEMPLATE = GUI_MODULE_TEMPLATE+ "onDataConnectionStateChanged\\sstate:2";
	
	//PATTERNS
	public static final Pattern MODEM_4G_CONNECTED_PATTERN = Pattern.compile(MODEM_4G_CONNECTED_TEMPLATE);
}
