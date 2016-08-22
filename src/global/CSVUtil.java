package global;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class CSVUtil {
	private File file; 
	private long id;
	private String fileName;

	private static CSVUtil csvUtil;

	/**
	 * Get instance from no parameter CTOR
	 * 
	 * @return PropertiesUtil
	 */
	public static CSVUtil getInstance(String name, Date id) {
		if (csvUtil == null)
			return new CSVUtil( name,  id);
		else
			return csvUtil;
	}

	public static CSVUtil getInstance(Date id) {
		return getInstance(null, id);
	}

	public static CSVUtil getInstance() {
		return getInstance(null, null);
	}

	private CSVUtil(String name, Date id) {
		name = name != null ? name : "result.csv";
		this.setFileName(name);
		if (id != null)
			this.id = id.getTime();
		this.file = new File(fileName);
	}

	
	/***
	 * Write results to CSV file:
	 * The CSV format: ID, Header, Result, Date
	 * Assumption: PropertiesUtil.getInstance().getProperty("reboot") is set already
	 * @param event
	 * @param result
	 * @param date
	 */
	public  void writeVPHTestsCSVFile(String event, Date date, String result) {
		result = result != null ? result : "";
		long time = date != null ? date.getTime() : -1;
		String csvData = this.id + ", " + event + ", " + time + ", " + result + "\n";
		try {
			FileUtils.writeStringToFile(file, csvData, true);
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
	public  void writeVPHTestsCSVFile(String event, Date date) {
		writeVPHTestsCSVFile(event, date, "");
	}

	public  long getId() {
		return id;
	}

	public  void setId(long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getCsvFile() {
		return file;
	}

	public void setCsvFile(File csvFile) {
		this.file = csvFile;
	}
	
	
}
