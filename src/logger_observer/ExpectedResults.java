package logger_observer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO
public class ExpectedResults implements Subscriber{
	
	private Publisher logcatPub;
	private static final String 
	DATE = "^(\\d{2,2}-\\d{2,2}\\s\\d{2,2}:\\d{2,2}:\\d{2,2}\\.\\d{3,3}).*",
			_3G_CONNECTION = DATE + "(Network info:).*(3GMobile\\[HSDPA\\]).*(state:\\sCONNECTED\\/CONNECTED)", 
			ATMEL_DURATION = DATE + "(Authentication received in)", 
			SERVERS_DURATION = DATE + "(Received response after:).*(auth_response)",
			_4G_GUI_CONNECTION = DATE + "(onDataConnectionStateChanged\\sstate:2)";
	public ExpectedResults(Publisher publisher) {
		this.logcatPub = publisher;
		this.logcatPub.register(this);
	}

	@Override
	public void update(String line) {
		
		Pattern patterna = Pattern.compile(_3G_CONNECTION, Pattern.CASE_INSENSITIVE);
		Matcher matchera = patterna.matcher(line);
		if (matchera.find()) {
			Date date = extractDate(matchera);
			System.err.println("3G connected at: "+date);
			System.out.println(line);
		}
		Pattern patternb = Pattern.compile(ATMEL_DURATION, Pattern.CASE_INSENSITIVE);
		Matcher matcherb = patternb.matcher(line);
		if (matcherb.find()) {
			Date date = extractDate(matcherb);
			System.err.println("Authentication Receive in Atmel at: "+date);
			System.out.println(line);
		}
		Pattern patternc = Pattern.compile(SERVERS_DURATION, Pattern.CASE_INSENSITIVE);
		Matcher matcherc = patternc.matcher(line);
		if (matcherc.find()) {
			Date date = extractDate(matcherc);
			System.err.println("Authentication Response from GW at: "+date);
			System.out.println(line);
		}
		Pattern patternd = Pattern.compile(_4G_GUI_CONNECTION, Pattern.CASE_INSENSITIVE);
		Matcher matcherd = patternd.matcher(line);
		if (matcherd.find()) {
			Date date = extractDate(matcherd);
			System.err.println("4G service at: "+date);
			System.out.println(line);
		}
		
	}
public static void main(String[] args) throws IOException {
	Files.delete(Paths.get("C:\\Simgo\\Src\\vph_tests\\Logcat Logger\\2016-08-08_16-51-46.txt"));////"C:\\Simgo\\Src\\vph_tests\\Logcat Logger\\2016-08-08_17-04-53.txt"));
}
	/**
	 * @param matchera
	 * @return
	 */
	public Date extractDate(Matcher matchera) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				/* 08-08 11:48:03.371 */"dd-MM HH:mm:ss.SSS");
		Date date = null;
		String group = matchera.group(1);
		try {
			date = formatter.parse(group);
		} catch (ParseException e) {e.printStackTrace();}
		date = addCurrentYearToDate(date);
		return date;
	}
	
	/**
	 * The logcat log start now without a year in the date line 
	 * e.g."08-08 11:48:03.371 I/ServersControl( 3250)"
	 * the date above extract as 8.8.1970-11:48:03
	 * this method add current year in the 1970 date extracted from the format above
	 * @param extractDate
	 * @return actualDate
	 */
	public static Date addCurrentYearToDate(Date extractDate) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(extractDate);
		instance.add(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)-instance.get(Calendar.YEAR));
		Date actualDate = new Date(instance.getTimeInMillis());
		return actualDate;
	}


}
