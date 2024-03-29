package global;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import testrail.api.TestRailAPI;
import selenium.PersistUtil;
public class ScreenshotUtil {
	
	/**
	 * Get the test name from the class instance, </br>
	 * If not exist create a Screenshot folder,</br>
	 * Create an inner file with the test name and date+time .png,</br>
	 * Create a screenshot of the current screen using method: </br>
	 * &nbsp &nbsp &nbsp org.openqa.selenium.TakesScreenshot.driver
	 * .getScreenshotAs(OutputType.FILE)</br>
	 * 
	 * @param String
	 *            testName
	 */
	public static void takeScreenShot(String testName) {
		File userDir = new File(System.getProperty("user.dir") + "\\Screenshots");

		if (!userDir.exists())
			userDir.mkdir();

		File srcFile = ((TakesScreenshot) PersistUtil.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);
		String date = new SimpleDateFormat("ddMMyy-HH_mm_ss_SSS").format(new Date());

		try {
			FileUtils.copyFile(srcFile, new File(userDir, testName + "_" + date + ".png"));
			System.err.println("Test failed, screenshot saved in " + userDir + "\\" + testName + "_" + date + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
  	 * Save the screenshot file in the local output file and print the path to
  	 * the console.</br>
 	 * 
 	 * @param ITestResult
 	 */
	public static void takeScreenShot(ITestResult result, String testNameField) {		
   	String testName = "cannot get the test name";		
 		Object instance = result.getInstance();		
 		try {		
 			testName = (String) instance.getClass()		
 					.getDeclaredField(testNameField).get(instance);		
 		} catch (IllegalArgumentException | IllegalAccessException		
 				| NoSuchFieldException | SecurityException e) {		
 			try {		
 				testName = TestRailAPI.getInstance().getMethodName();		
 			} catch (Exception e1) {		
 			}		
 		}		
 				
 		ScreenshotUtil.takeScreenShot(testName);		
 	}

	/**
	 * Save the screenshot file in the local output file and print the path to
	 * the console.</br>
	 * the name file will come from method name
	 * 
	 * @param ITestResult
	 */
	public static void takeScreenShot() {
		ScreenshotUtil.takeScreenShot(getMethodName());
	}

	/**
	 * get from testng method the method name
	 * 
	 * @return
	 */
	private static String getMethodName() {
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		ITestNGMethod iTestNGMethod = iTestResult.getMethod();
		return iTestNGMethod.getMethodName();
	}

}
