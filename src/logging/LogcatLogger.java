package logging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import adb.AdbUtil;
import global.FileUtil;

public class LogcatLogger implements Runnable {

	public final static String LOGCAT = "Logcat Logger";
	//init on CTOR
	private String findSpecificString;
	//init on run()
	private String pathToFile;
	private ExecutorService executorService;
	private Process process;

	/**
	 * CTOR with default filterd values - for receiving svcp's messages
	 */
	public LogcatLogger() {
		this("VSimControl", "ServersControl");
	}

	/**
	 * CTOR that filter the logcat by specific string
	 * can add to the filter one or many strings to search by 
	 * all the strings that are inserted  exist in the filter
	 * 
	 * @param findStr
	 */
	public LogcatLogger(String... findStr) {
		String temp = "";
		for (String filter : findStr) {
			temp += filter+"|";
		}
		this.findSpecificString = temp.substring(0,temp.length()-1);// remove the last '|'
	}

	/**
	 * clear the logcat log
	 */
	public void clearLogcat() {
		AdbUtil.executeCommandLine("adb logcat -c ");
	}

	/**
	 * @param String
	 *            of folder name, path of the file has to be save
	 * @return the Path to save the file and the time to insert in the class
	 *         name
	 */
	public String getPathAndTime(String folder) {
		String path = System.getProperty("user.dir");
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String time = formatter.format(date);
		String cmdToFile = path + "\\" + folder + "\\" + time + ".txt";
		return cmdToFile;
	}

	/**
	 * get the logcat file from path attribute
	 * 
	 * @return file
	 */
	public File getLogCatFile() {
		return new File(this.pathToFile);
	}

	/**
	 * Stop the Thread who runs the logcat reader
	 */
	public void stopLogCat() {
		// wait for other threads 2 sec
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		// kill the process
		while (this.process.isAlive()) 
			this.process.destroyForcibly();
		// kill the thread
		this.executorService.shutdownNow();
	}

	/**
	 * Start logcat reader Thread, create a Thread pull ExecutorService used to
	 * execute and shutdown, set the Thread as daemon to close when main is
	 * closed
	 */
	public void startLogCat() {
		LogcatLogger logcatReader = this;
		// Initialized the ExecutorService as class member to shutdown when needed
		this.executorService = Executors.newFixedThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = Executors.defaultThreadFactory().newThread(logcatReader); // stub
				thread.setDaemon(true);
				return thread;
			}
		});
		
		// start the thread
		this.executorService.execute(logcatReader);
		
		// sleep until thread is running
		try { Thread.sleep(3000);} catch (InterruptedException e) {}
	}

	/**
	 * Run the logcat-save-to-file method the command contains also the
	 * parameters pkg and findSpecificString if they arn't empty
	 */
	@Override
	public void run() {
		runLogcatCommand();
	}

	/**
	 * When the Thread has started: runs te following commands:
	 * A. create the logcat command
	 * B. initial the file path 
	 * C. excute the command
	 * D. initial the process
	 * E. write to file from the process
	 */
	private void runLogcatCommand() {
		String command = "adb logcat -c && adb shell \"logcat -v time | grep -E '"+this.findSpecificString+"'\"";

		System.out.println("Logcat filter: "+command);
		this.pathToFile = getPathAndTime(LOGCAT);
		Process process = AdbUtil.executeCommandLine(command);
		this.process = process;
		FileUtil.writeToFileFromProcess(process, this.pathToFile);
	}

	public String getFindSpecificString() {
		return findSpecificString;
	}

	public void setFindSpecificString(String findSpecificString) {
		this.findSpecificString = findSpecificString;
	}


	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}