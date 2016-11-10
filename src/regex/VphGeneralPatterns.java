package regex;

import java.util.regex.Pattern;

public class VphGeneralPatterns {
	//GROUPS
	public static final String LOGCAT_DATE_GROUP = "date";
	
	//TEMPLATES
	//08-04 09:20:59.359 		
	public static final String	LOGCAT_DATE_TEMPLATE = "^(?<"+LOGCAT_DATE_GROUP+">\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}).*";
	
	//PATTERNS
	public static final Pattern LOGCAT_DATE_PATTERN = Pattern.compile(LOGCAT_DATE_TEMPLATE);

}
