package logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

import global.PropertiesUtil;

/**
 * This Class contains methods for getting the cloud logs and storing them in
 * the user folder. The idea is to start logging in the beginning of each test
 * and stop it when it stops. the testname and date will be indicated on the log
 * file.
 * 
 * @author Or
 */
public class CloudLogger implements Runnable {
	private static Socket socket;
	static PrintWriter logfile;
	static BufferedReader instream;
	static BufferedWriter bw;
	static File textFile;
	String testname;
	
	/**
	 * CTOR with default test name
	 */
	public CloudLogger() {
		this(null);
	}

	public CloudLogger(String testname) {	
		// set the test name String
		setTestName(testname);
		// set the textFile File
		setTextFile();
		//set the log file PrintWriter
		setLogFile();
	}


	/**
	 * The test name will be used for the log filename
	 * Start the thread
	 */
	public void readCloudLog() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * When the Thread called, open the socket and write to logFile
	 */
	public void run() {
		String ioOut = "-1";
		// Connect to QA cloud
		try {
			socket = new Socket(PropertiesUtil.getInstance().getProperty( "LOGGER_DNS_NAME"), 5550);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (socket.isConnected()) {
				// Insert Username and Password in the Telnet session
				// Usually on BufferedWriter, you can use the "write" and
				// "newline" methods. but as this runs
				// On Mac OS X, these methods did not fit, as they automatically
				// use "\n" string, while OS X, built
				// on unix, expects "\r\n" to indicate a line end.
				ioOut = instream.readLine();
				if (ioOut.startsWith("Please")) {
					System.out.println("Password Entered");
					bw.append("gimso!Tsoltar\r\n");
					bw.flush();
				}
				if (ioOut.startsWith("Press")) {
					System.out.println("Reached log");
					bw.append("d\r\n");
					bw.flush();
				}
				if (Thread.interrupted()) {
					try {
						instream.close();
						bw.close();
						socket.close();
						return;
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Could not close socket");
						System.exit(-1);
					}
				}
				// Write the output to the log file
				logfile.println(ioOut);
				logfile.flush();
			}
		} catch (UnknownHostException e) {
			System.out.println("Could not open Telnet session to QA cloud");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("There was a problem with file creation, or I/O excepetion");
			System.exit(1);
		}
	}
	
	public File getLogFile() {
		return textFile;
	}
	
	public void stopCloudLog() {
		try {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * set the test name from the parameter or from testNG or just use General 
	 * @param testname or null
	 */
	private void setTestName(String testname) {
		if (testname == null) {
			try {
				ITestResult iTestResult = Reporter.getCurrentTestResult();
				ITestNGMethod iTestNGMethod = iTestResult.getMethod();
				String methodName = iTestNGMethod.getMethodName();
				if (methodName != null) {
					this.testname = methodName;
				} else {
					this.testname = "General";
				}
			} catch (NullPointerException e) {
				this.testname = "General";
			}
		} else {
			this.testname = testname;
		}
	}

	/**
	 * create and initialized the name of textFile 
	 */
	private void setTextFile() {
		// Get date and set filename
		Date date = new Date();
		// Set the Log File name
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSZ");
		String time = formatter.format(date);
		String userHomeFolder = System.getProperty("user.home");
		String filename = "Cloud_Log_" + this.testname + "_" + time + ".txt";
		// if the directory Plug-Logs does not exist, create it
		File Dir = new File(userHomeFolder + "/Cloud-Logs");
		if (!Dir.exists()) {
			System.out.println("creating directory: " + "Plug-Logs");
			boolean result = false;
			try {
				Dir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}
		}
		// Create the Log file
		textFile = new File(Dir, filename);
	}

	/**
	 * Initialized the log file PrintWriter with the textFile 
	 */
	private void setLogFile() {
		try {
			logfile = new PrintWriter(textFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}