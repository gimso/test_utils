package logging;

import global.TimeAndDateConvertor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import beans.PhoneType;

/**
 * @author Or This Class is used for analyzing logs retrieved from the cloud on
 *         tests. It enables you to search for echo requests and get data from
 *         it, search for FSIM, FW-update and etc. Each of these analytics will
 *         be added specifically per test needs, as the logic differs. All the
 *         data here is based solely on what appears on the cloud logs.
 */
public class Analyze_cloud_logs {

	/**
	 * This function gets a Cloud log file name, and searches for "Echo"
	 * requests in it. If found, it sets it's data in a Hashmap, and then
	 * returns an ArrayList containing all the echo requests data as hashmaps.
	 * return type example - {allocation=84, Charging=OFF, trip=90,
	 * RSIM=972542568290, DSIM MCC=425, DSIM MNC=1, SIM=Using RSIM,
	 * Battery=100%, plug=000010001873, Reception=-49dBm,
	 * user=IPHONE_5SUserName, timestamp=120515 13:31:05.804}
	 * 
	 * @return An array list containing Hashmaps of echos, each one with a key
	 *         and value that can be accessed.
	 */
	public static ArrayList<HashMap<String, String>> getAllEchosInCloud(
			File filename) {
		// Read all the lines in the File as strings

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

			String line = "";
			Boolean found = false;

			while ((line = br.readLine()) != null)

			{

				// Check if an Echo request appears in the log

				if (line.contains("Echoed ")) {

					found = true;

					HashMap<String, String> echoHash = new HashMap<String, String>();

					// Find the timestamp on the echo line, add it to a hashmap

					Pattern time = Pattern
							.compile("(\\d{6} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})");

					Matcher m = time.matcher(line);

					if (m.find()) {
						echoHash.put("timestamp", ((m.group(1))));
						String locateTime = m.group(1);
					}

					// Remove unnecessary data and get a string containing all
					// the echo data

					String removeBegining[] = line
							.split("Echoed allocation for ");

					String echoData = removeBegining[1];

					// Perform RegEx expressions to set the data in the
					// following format: key:value, in a string array

					echoData = echoData.replaceAll("trip ", "trip:");
					echoData = echoData.replaceAll("\\) | \\(", ", ");
					echoData = echoData.replaceAll("\\: ", ":");

					String[] echo = echoData.split(", ");

					// Create a hashmap from the string array, containing unique
					// keys and values

					for (int i = 0; i < echo.length; i++) {
						String echoParam[] = echo[i].split(":");

						echoHash.put(echoParam[0], echoParam[1]);
					}

					result.add(echoHash);

				}

			}
			br.close();

			return result;

		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

	}
	/**
	 * This function get all Echo dates of specific plug from cloud log file
	 * @param cloudLoggerFile
	 * @param phone
	 * @return List<Date>
	 */
	public static List<Date> getAllEchosDatesInCloud(File cloudLoggerFile,
			PhoneType phone) {
		ArrayList<HashMap<String, String>> allEchosInCloud = getAllEchosInCloud(cloudLoggerFile);
		List<Date> echoCloud = new ArrayList<Date>();
		for (HashMap<String, String> mss : allEchosInCloud) {
			String timeStamp = mss.get("timestamp");
			String plug = mss.get("plug");
			if (plug.equalsIgnoreCase(phone.getTestData().getPlugId()))
				echoCloud.add(TimeAndDateConvertor
						.cloudStringToDate(timeStamp));
		}
		return echoCloud;
	}
}
