package regex;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import global.TimeAndDateConvertor;

/***
 * This class is utility for finding a regex by pattern
 * 
 * @author Yehuda
 *
 */
public class GetMatch {

	/***
	 * Get match by pattern
	 * 
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

	/***
	 * extract string from log according to pattern
	 * 
	 * @param input
	 * @param patternStr
	 * @param group
	 * @return input, group, or null
	 */
	public static String findInLog(String input, String patternStr, Integer group) {
		Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			if (group != null)
				return matcher.group(group);
			else
				return input;
		}
		return null;
	}

	/***
	 * extract string from log according to pattern
	 * 
	 * @param input
	 * @param patternStr
	 * @return input, or null
	 */
	public static String findInLog(String input, String patternStr) {
		return findInLog(input, patternStr, null);
	}

	/***
	 * extract string from log according to pattern
	 * 
	 * @param input list
	 * @param patternStr
	 * @param group
	 * @return match row, group,or null
	 */
	public static String findInLog(List<String> input, String patternStr, Integer group) {
		for (String s : input)
			if (findInLog(s, patternStr, group) != null)
				return findInLog(s, patternStr, group);
		return null;
	}

	/***
	 * extract string from log according to pattern
	 * 
	 * @param input list
	 * @param patternStr
	 * @return match row,or null
	 */
	public static String findInLog(List<String> input, String patternStr) {
		return findInLog(input, patternStr, null);
	}
	
	/**
	 * Get any logcat line and extract the date from it. Assumption: logcat line
	 * is with date
	 * 
	 * @param line
	 * @return Date
	 */
	public static Date dateFromLogcatLine(String line) {
		Pattern pattern = Pattern.compile(VPHTestPatterns.DATE);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find())
			return TimeAndDateConvertor.logcatStringDateToDate(matcher.group(1));
		return null;
	}
}