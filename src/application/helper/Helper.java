package application.helper;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Helper{
	
	public static final String TAG = Helper.class.getSimpleName();
	public static String HardResetCode = "00003";
	
	public enum OS{
		Windows,Mac
	}
	
	/**
	 * Method will return the current running os
	 * @return current OS
	 */
	public static String getCurrentOS(){
			String os = System.getProperty("os.name");
			if(os.contains(OS.Windows.toString()))
				return OS.Windows.toString();
			else if(os.contains(OS.Mac.toString()))
				return OS.Mac.toString();
			return os;
	}
	
	
	/**
	 * Load properties from file into java properties
	 *
	 */
	public static Properties loadProperties(String fileName){
		Properties testProperties = new Properties();
		try{
			testProperties.load(Helper.class.getClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			System.err.println(TAG + " load General Properties:" + e.getMessage());
			e.printStackTrace();
		}
		return testProperties;
	}
	
	
	/**
	 * Method get qualified path and returns the full path
	 * @param qualifiedPath - given qualified path
	 * @return full path 
	 */
	public static String getFullPath(String qualifiedPath){
		File file = new File(System.getProperty("user.dir"));
		return file.getParentFile().getAbsolutePath() + qualifiedPath;
	}
}
