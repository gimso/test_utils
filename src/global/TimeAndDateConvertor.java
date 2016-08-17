package global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Time and Date Utility parser and more
 * 
 * @author Yehuda Ginsburg
 *
 */
public class TimeAndDateConvertor {
	
	public static final String LOGCAT_DD_MM_HH_MM_SS_SSS = /* 08-08 11:48:03.371 */"dd-MM HH:mm:ss.SSS";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DD_MM_YYYY = "dd.MM.yyyy";
	public static final String PLUG_DATE_YYYY_MM_DD_HH_MM_SS_SSS2 = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String CLOUD_DATE_DD_M_MYY_HH_MM_SS_SSS = "ddMMyy HH:mm:ss.SSS";//120515 13:31:05.804
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String XX_MIN_XX_SEC = "%02d Min, %02d Sec";
	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "YYYY.MM.dd HH:mm:ss.SSS";

	/**
	 * when in Cloud need to date in this format "yyyy-MM-dd", like in
	 * persist.usage.User date box, this utility return from any java.util.Date
	 * to that String format
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, YYYY_MM_DD);
	}
	
	/**
	 * when in Cloud need to date in this format "yyyy-MM-dd", like in
	 * persist.usage.User date box, this utility return from any java.util.Date
	 * to that String format
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * convert the string from cloud log to date and add 3 hours (GMT+3:00) -
	 * Israel summer Time Zone
	 * 
	 * @param cloudDateAsString
	 *            in ddMMyy HH:mm:ss.SSS format
	 * @return
	 */
	public static Date cloudStringToDate(String cloudDateAsString) {
		Date cloudDate = stringToDate(cloudDateAsString, CLOUD_DATE_DD_M_MYY_HH_MM_SS_SSS);
		cloudDate.setTime(cloudDate.getTime() + TimeZone.getDefault().getOffset(cloudDate.getTime()));
		return cloudDate;
	}

	/**
	 * Convert from date in plug log to java.util.Date
	 * was in format "yyyy.MM.dd.HH.mm.ss" 2015-07-07 14:23:59.919 now the
	 * format is yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param string
	 * @return
	 */
	public static Date plugStringToDate(String string) {
		return stringToDate(string, PLUG_DATE_YYYY_MM_DD_HH_MM_SS_SSS2);
	}

	public static Date stringToDate(String string, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			return simpleDateFormat.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Get a difference between two dates
	 * 
	 * @param before
	 *            the oldest date
	 * @param after
	 *            the newest date
	 * @param TimeUnit enums
	 *            Seconds/Minutes etc.
	 * @return the difference long value
	 */
	public static long getDiffBetweenDates(Date before, Date after, TimeUnit timeUnit) {
		long diffInMillies = after.getTime() - before.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Get two dates and compare them
	 * @param dateBefore
	 * @param dateAfter
	 * @return true if dateBefore is before  dateAfter
	 */
	public static boolean isBeforeDate(Date dateBefore, Date dateAfter) {
		return dateBefore.before(dateAfter);
	}	
	
	/**
	 * convert a Date to cloud date string
	 * @param Date date in (Mon Mar 14 18:17:06 IST 2016) format
	 * @return date as a string in (2016-03-13 08:00:00) format
	 * @author Dana
	 */
	public static String dateToCloudTripString(Date date) {
		//change date time to match GMT
		Date dateToLocalTimezone = new Date(date.getTime()-TimeZone.getDefault().getOffset(date.getTime()));	 
		//insert it into format 2016-03-13 08:00:00
		return dateToString(dateToLocalTimezone,YYYY_MM_DD_HH_MM_SS);
	}
	
	/**
	 * Extract date from logcat date
	 * Add to it todays year, unfotunetly the date in logcat came without year...
	 * @param logcatDate
	 * @return Date
	 */
	public static Date extractLogcatDate(String logcatDate) {
		Date date = stringToDate(logcatDate, LOGCAT_DD_MM_HH_MM_SS_SSS);
		return addCurrentYearToDate(date);
	}
	
	/**
	 * The logcat log start now without a year in the date line e.g."08-08
	 * 11:48:03.371 I/ServersControl( 3250)" the date above extract as
	 * 8.8.1970-11:48:03 this method add current year in the 1970 date extracted
	 * from the format above
	 * 
	 * @param extractDate
	 * @return actualDate
	 */
	public static Date addCurrentYearToDate(Date extractDate) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(extractDate);
		instance.add(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - instance.get(Calendar.YEAR));
		Date actualDate = new Date(instance.getTimeInMillis());
		return actualDate;
	}
	
	/**
	 * Extract date from string date in format "YYYY.MM.dd HH:mm:ss.SSS"
	 * 
	 * @param String date
	 * @return java.util.Date
	 */
//	public static Date extractDateFromString(String date) {
//		try {
//			return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS).parse(date);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
//	/**
//	 * Extract String in format "YYYY.MM.dd HH:mm:ss.SSS" from date 
//	 * @param date
//	 * @return date-string
//	 */
//	public static String dateToString(Date date) {
//		return new SimpleDateFormat().format(date);
//	}
	
//	/**
//	 * return string like the pattern "XX Min, XX Sec"
//	 * @param date end
//	 * @param date begin
//	 * @return minutes and seconds between dates as String 
//	 */
//	public static String secondsBetween(Date end, Date begin) {
//		return secondsBetween(end, begin, XX_MIN_XX_SEC);
//	}
//	
//	/**
//	 * @param date end
//	 * @param date begin
//	 * @return minutes and seconds between dates as String 
//	 */
//	public static String secondsBetween(Date end, Date begin, String pattern) {
//		long millis = (end.getTime() - begin.getTime());
//		String rv = String.format(pattern, 
//				TimeUnit.MILLISECONDS.toMinutes(millis),
//				TimeUnit.MILLISECONDS.toSeconds(millis)- TimeUnit.MINUTES.toSeconds(
//				TimeUnit.MILLISECONDS.toMinutes(millis)));
//		return rv;
//	}
//	
//	/**
//	 * same as  secondsBetween(Date end, Date begin) but in string format "YYYY.MM.dd HH:mm:ss.SSS"
//	 * @param end
//	 * @param begin
//	 * @return minutes and seconds between dates as String 
//	 */
//	public static String secondsBetween(String end, String begin) {
//		if (end != null && begin != null) {
//			Date dEnd = stringToDate(end, YYYY_MM_DD_HH_MM_SS_SSS);
//			Date dBegin = stringToDate(begin, YYYY_MM_DD_HH_MM_SS_SSS);
//			return secondsBetween(dEnd, dBegin);
//		}
//		return null;
//	}
		
	
}
