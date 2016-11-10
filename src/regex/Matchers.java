package regex;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import global.TimeAndDateConvertor;


public class Matchers {
	private Date date;
	private String optional;
	private String[] optionals;
	
	public static String getResult(String input, Pattern pattern, String groupToExtract) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			if (groupToExtract != null)
				return matcher.group(groupToExtract);
			else
				return input;
		}
		return null;
	}

	public static String getResult(String input, Pattern pattern) {
		return getResult(input, pattern, null);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = TimeAndDateConvertor.logcatStringDateToDate(date);
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String[] getOptionals() {
		return optionals;
	}

	public void setOptionals(String[] optionals) {
		this.optionals = optionals;
	}
}
