package global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
	
	/**
	 * when in Cloud need to date in this format "yyyy-MM-dd", 
	 * like in persist.usage.User date box, this utility
	 * return from any java.util.Date to that String format
	 * 
	 * @param date
	 * @return
	 */ 
	public static String convertDateToString(Date date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		
	}

	
	/**
	 * convert the string from cloud log to date and add 3 hours (GMT+3:00) -
	 * Israel summer Time Zone
	 * 
	 * @param cloudDateAsString
	 *            in ddMMyy HH:mm:ss.SSS format
	 * @return
	 */
	public static Date convertCloudStringToDate(String cloudDateAsString) {
		try {
			//      												  120515 13:31:05.804
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyy HH:mm:ss.SSS");
			Date cloudDate = simpleDateFormat.parse(cloudDateAsString);
			Date cloudDateToLocalTimezone = new Date(cloudDate.getTime()+TimeZone.getDefault().getOffset(cloudDate.getTime()));
			System.out.println("Cloud date = "+cloudDate);
			System.out.println("The local date is = "+cloudDateToLocalTimezone);
			
			return cloudDateToLocalTimezone;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Convert from date in plug log to java.util.Date
	 * was in format "yyyy.MM.dd.HH.mm.ss" 2015-07-07 14:23:59.919 now the
	 * format is yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param string
	 * @return
	 */
	public static Date convertPlugStringToDate(String string) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
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
	 * @param date1 	the oldest date
	 * @param date2		the newest date
	 * @param timeUnit	Seconds/Minutes etc.
	 * @return the difference long value as String
	 */
	public static String getDiffBetweenDates(Date before, Date after,
			TimeUnit timeUnit) {
		long diffInMillies = after.getTime() - before.getTime();
		return String.valueOf(timeUnit.convert(diffInMillies,
				TimeUnit.MILLISECONDS));
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
	 * if date string is in format dd.MM.yyyy i.e. 01.01.2015 returns it as
	 * java.util.Date
	 * 
	 * @param string
	 */
	public static Date convertDateStringToDate(String dateAsString) {
		
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	    try {
			return format.parse(dateAsString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * convert a Date to cloud date string
	 * @param Date date in (Mon Mar 14 18:17:06 IST 2016) format
	 * @return date as a string in (2016-03-13 08:00:00) format
	 * @author Dana
	 */
	public static String convertDateToCloudTripString(Date date) {
		//change date time to match GMT
		Date dateToLocalTimezone = new Date(date.getTime()-TimeZone.getDefault().getOffset(date.getTime()));	 
		//insert it into format 2016-03-13 08:00:00
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(dateToLocalTimezone);
	}
		
	
}
