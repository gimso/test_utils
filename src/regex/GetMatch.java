package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/***
 * This class is utility for finding a regex by pattern 
 * @author Yehuda
 *
 */
public class GetMatch {
	
	/***
	 * Get match by pattern 
	 * @param input
	 * @param stringPattern
	 * @param groupToExtract
	 * @return input (if match) or group (if defined) or null (if not find)
	 */
	public static String getMathcer(String input, String stringPattern, Integer groupToExtract) {
		Pattern pattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			if (groupToExtract != null)
				return matcher.group(groupToExtract);
			else
				return input;
		}
		return null;
	}
}