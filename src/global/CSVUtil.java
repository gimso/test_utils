package global;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class CSVUtil {
	public static File csvFile = new File("result.csv");
	/***
	 * Write results to CSV file:
	 * The CSV format: ID, Header, Result, Date
	 * Assumption: PropertiesUtil.getInstance().getProperty("reboot") is set already
	 * @param event
	 * @param result
	 * @param date
	 */
	public static void writeVPHTestsCSVFile(String event, Date date, String result) {
		String testId = PropertiesUtil.getInstance().getProperty("reboot");		
		result = result != null ? result : "";
		long time = date != null ? date.getTime() : -1;
		String csvData = testId + ", " + event + ", " + time + ", " + result + "\n";
		try {
			FileUtils.writeStringToFile(csvFile, csvData, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/***
	 * Write results to CSV file:
	 * The CSV format: ID, Header, Result, Date
	 * Assumption: PropertiesUtil.getInstance().getProperty("reboot") is set already
	 * @param event
	 * @param result
	 * @param date
	 */
	public static void writeVPHTestsCSVFile(String event, Date date) {
		writeVPHTestsCSVFile(event, date, "");
	}
	
	
}
