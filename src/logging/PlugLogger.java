package logging;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import jssc.SerialPortList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 *  This class provides methods for starting a serial log for the
 *         plug, connected by the Com Port, and storing it in files located on
 *         the user home folder. It also provides ways of searching these logs
 *         for testing purposes.
 * @author Or
 */
public class PlugLogger implements Runnable, SerialPortEventListener {

	public static final int BAUD_RATE_4800 = 4800;
	public static final int BAUD_RATE_9600 = 9600;
	public static final int BAUD_RATE_19200 = 19200;
	public static final int BAUD_RATE_38400 = 38400;
	public static final int BAUD_RATE_56000 = 56000;
	public static final int BAUD_RATE_57600 = 57600;
	public static final int BAUD_RATE_115200 = 115200;
	public static final int BAUD_RATE_230400 = 230400;
	
	private int baudRate = BAUD_RATE_230400;// default baud rate
	
	private static CommPortIdentifier portId;
	// static Enumeration portList;
	private static Enumeration portList = null;
	private static PrintWriter logfile;
	private InputStream inputStream;
	private SerialPort serialPort;
	private String portname;
	private String testname;
	private static File textFile;
	private Boolean portfound = false;
	private StringBuilder builder = new StringBuilder();
	private ExecutorService executorService;
	private PlugLogger plugLogger;
	
	
	/**
	 * This is the constructor of the logger, which creates a log file, sets the portname and the filename according to the testname
	 * @param portname
	 * @param testname
	 */
	
	public PlugLogger(String portname, String testname)

	{

		this.portname = portname;

		// Get the Test name from TestNG and set it for use as the log file name

		if (testname == null)

		{
			try

			{
				ITestResult iTestResult = Reporter.getCurrentTestResult();
				ITestNGMethod iTestNGMethod = iTestResult.getMethod();
				String methodName = iTestNGMethod.getMethodName();

				if (methodName != null) {

					this.testname = methodName;
				}

				else {
					this.testname = "General";
				}

			}

			catch (NullPointerException e) {
				this.testname = "General";
			}

		}

		else

		{
			this.testname = testname;
		}

		// Get date and set filename

		java.util.Date date = new java.util.Date();

		// Set the log file name

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH-mm-ss.SSSZ");
		String time = formatter.format(date);
		String userHomeFolder = System.getProperty("user.home");
		String filename = "Plug_Log_" + this.testname + "_" + time + ".txt";

		// if the directory Plug-Logs does not exist, create it

		File Dir = new File(userHomeFolder + "/Plug-Logs");

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

		// Create the Log file in the directory

		textFile = new File(Dir, filename);

		try {
			logfile = new PrintWriter(textFile);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		plugLogger = this;
	}

	
	public PlugLogger(String portname){
		this(portname, null);
	}

	public PlugLogger(int portNum) {
		this("COM" + portNum);
	}
	
	
	public void readPlugLog() {

		// Get the Serial portid by name
		
		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements() && (portfound == false)) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(portname)) {

					portfound = true;
				}
			}
		}

		if (portfound == false) {
			throw new RuntimeException(
					"Could not find the portname on the connected ports list");
		}

		// Open the Serial application

		try {
			if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				String s = null;
				String command = "rm -rf /private/var/lock/*";
				try {
		 
					Process p = Runtime.getRuntime().exec(command);
		 
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		 
					// read the output from the command
					//System.out.println("Here is the standard output of the command:\n");
					while ((s = stdInput.readLine()) != null) {
						System.out.println(s);
					}
		 
					// read any errors from the attempted command
					//System.out.println("Here is the standard error of the command (if any):\n");
					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}
		 
					//System.exit(0);
				} catch (IOException e) {
					System.out.println("exception happened - here's what I know: ");
					e.printStackTrace();
					System.exit(-1);
				}
			
				//Runtime.getRuntime().exec("rm -rf /private/var/lock/*");
			}
			serialPort = (SerialPort) portId.open("Simgo", 5000);
		} catch (PortInUseException e) {
			System.err.println("could not open the serial connection, probably the log is open by another application. Exception:"+ e);
		}

		
		// Set Serial Application parameters

		try {

			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			serialPort.setSerialPortParams(getBaudRate(), SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setOutputBufferSize(96 * 64);
			
			
		} catch (UnsupportedCommOperationException e) {
			System.err.println(e);
		}

		// Get the Input Stream

		try {
			
			inputStream = serialPort.getInputStream();
			
		} catch (IOException e) {
			System.err.println(e);
		}

		
		// Notify on receiving new input data to the log

		try {
			
			serialPort.addEventListener(this);
			
		} catch (TooManyListenersException e) {
			System.err.println(e);
		}
		
		
		serialPort.notifyOnDataAvailable(true);

		// Set the main loop thread

		// open thread with executorService - better way when close it often
		this.executorService = Executors.newFixedThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = Executors.defaultThreadFactory().newThread(plugLogger);
				thread.setDaemon(true);
				return thread;
			}
		});
		// start the thread
		this.executorService.execute(plugLogger);
	}

	public void run() {
		try {
			Thread.sleep(200);
			
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}

	public void serialEvent(SerialPortEvent event) {
		
		switch (event.getEventType()) {
		
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:

			byte[] readBuffer = new byte[1];

			try {

				while (inputStream.available() > 0) {

					readBuffer[0] = (byte) inputStream.read();

					String bytestr = new String(readBuffer);

					String timeStamp = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

					builder.append(bytestr);
					//!bytestr.contains("\r") &&
					if (bytestr.contains("\n")) {
						boolean containsCrash_1 = builder.toString().contains("Reason:Error: Host Interface ingress buffers depleted");
						boolean containsCrash_2 = builder.toString().contains("ASSERT: host_interface_all");
						
						if (containsCrash_1 || containsCrash_2) {
							try {
								throw new IOException("Serial was getting exception: " + builder.toString());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						logfile.print(timeStamp + " " + builder);
						// clearing the builder
						builder.setLength(0);
					}

					logfile.flush();

				}

			} catch (IOException e) {
				System.err.println(e);
			}
		
			break;
		}
	}




	public File getLogFile(){
		return textFile;
	}
	
	/**
	 * Stop plug log step by step, first the serialPort then inputStream and then executorService
	 */
	public void stopPlugLog() {
		try {serialPort.close();} catch (Exception e) {}
		try {inputStream.close();} catch (Exception e) {}
		this.executorService.shutdownNow();
	}
	
	public int getBaudRate() {
		return this.baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}
	
	/**
	 *  
	 * @return the first COM port finding 
	 */
	public static String getFirstAvailablePort() {
		return SerialPortList.getPortNames()[0];
	}

}