package global;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class CSVUtil {
	private File file; 
	private long id;
	private String fileName;

	public CSVUtil(String name, Date id) {
		this.setFileName(name);
		if(id!=null)this.id = id.getTime();
		this.file = new File(fileName);
	}

	public CSVUtil(Date id) {
		this("result.csv", id);
	}
	
	public CSVUtil() {
		this("result.csv", null);
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
