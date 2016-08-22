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
	public static final String YYYY_MM_DD_HH_MM_SS_NO_SPACE="yyyy-MM-dd_HH-mm-ss";//2016-08-08_11-45-18.txt
	public static final String LOGCAT_DD_MM_HH_MM_SS_SSS = /* 08-08 11:48:03.371 */"MM-dd HH:mm:ss.SSS";
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
	 * 08-17 18:16:25.868 V/GUI     ( 3137): restartDimScreenTimeout for 50000

	 */
	public static Date logcatStringDateToDate(String logcatDate) {
		logcatDate = getCurrentYear() + logcatDate ;
		return stringToDate(logcatDate, "yyyy"+LOGCAT_DD_MM_HH_MM_SS_SSS);
	}
	
	/**
	 * The logcat log start now without a year in the date line e.g."08-08
	 * 11:48:03.371 I/ServersControl( 3250)" the date above extract as
	 * 8.8.1970-11:48:03 this method add current year in the 1970 date extracted
	 * from the format above
	 * @return the current year as "yyyy" pattern
	 */
	private static int getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
}
