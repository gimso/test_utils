package global;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class CSVUtil {
	/***
	 * Write results to CSV file:
	 * The CSV format: ID, Header, Result, Date
	 * Assuming PropertiesUtil.getInstance().getProperty("reboot") is set already
	 * @param header
	 * @param result
	 * @param date
	 */
	public static void writeVPHTestsCSVFile(String header, String result, Date date) {
		String timestampId = PropertiesUtil.getInstance().getProperty("reboot");
		String simpleDate = TimeAndDateConvertor.dateToString(date,TimeAndDateConvertor.YYYY_MM_DD_HH_MM_SS_SSS);
		
		String csv = timestampId + "," + header + "," + result + "," + simpleDate + "\n";
		File file = new File("result.csv");
		try {
			FileUtils.writeStringToFile(file, csv, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
