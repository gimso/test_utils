package technicianCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import testing_utils.TestOutput;
import global.PropertiesUtil;
import beans.PhoneType;


/**
 * @author orshachar
 *		This class provides methods of running tests on iOS devices, via a swift interface. 
 *		The only way to perform such tests is by building a "fake" application, and running unit 
 *      tests as part of it. In this case for each test there is a seperate app, which is built
 *      via a terminal (shell) command called xcodebuild.  
 */
/**
 * @author nirberman
 *
 */
public class IOSTechCodes {
	
	private static String xcodeFolder = PropertiesUtil.getInstance().getProperty("XCODE_PROJECT_FOLDER");
	private static String fsimProjectName = "FSIM";
	private static String echoProjectName = "Echo";
	

	/**
	 * @param phone
	 * @return TestOutput
	 */
	
	
	public static TestOutput fsim(PhoneType phone) {
	
		TestOutput output = runXcodeTest(fsimProjectName, phone);
		// TestOutput to = new TestOutput(false, "Failed to send FSIM technician code on" + phone);
		return output;

	}
	
	/**
	 * @param phone
	 * @return TestOutput
	 */
	
	
	public static TestOutput echo(PhoneType phone)
	{   
		TestOutput output = runXcodeTest(echoProjectName, phone);
		return output;
	}
	
	
	
	/**
	 * @param projectName, phone
	 * @return TestOutput
	 * This method simply runs the xcodebuild command using the relevant project name, on the phone
	 * as a testing destination 
	 */ 
	
	
	private static TestOutput runXcodeTest(String projectName, PhoneType phone )
	{
		
		TestOutput to = new TestOutput(false, "Failed to send" + projectName +  "technician code on" + phone);
		String projectFolder = xcodeFolder + "/" + projectName;
		String xcodeBuildCommand = "xcodebuild test -project " + projectName + ".xcodeproj -scheme " + projectName + " -destination 'platform=iOS,name=" + phone.getName() + "'";
		
		
		

		File wd = new File("/bin");
		
		Process proc = null;
		try {
		   proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
		}
		catch (IOException e) {
		   e.printStackTrace();
		}
		if (proc != null) {
		   BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		   PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
		   
		   
		   
		   
		   // Here is the actual building of the Xcode Project, which must be done from the project's library. This build also performs unit tests
		   // the project will run using a build command in xcode, which contains tests in XCTests format as part of the xcode project
		   
		   
		   
		   // Browse to The Project's Folder
		 
		   out.println("cd " + projectFolder);
		   
		   System.out.println("Browsed to" + projectFolder);
		   
		   
		   
		   out.println(xcodeBuildCommand);
		  
		   System.out.println("Sent The following Xcodebuild Command: " + xcodeBuildCommand);
		   
		   
		   
		   // The XCTest returns a result as an exit code, on which you can search for a pass indication string
		   
		   
		   out.println("exit");
		   try {
		      String line;
		      while ((line = in.readLine()) != null) {
		         System.out.println(line);
		         if (line.contains("** TEST SUCCEEDED **"))
		         {
		        	 System.out.println("The iPhone had Successfully sent an " + projectName +  " to the plug");
		        	 to.setResult(true);
		        	 to.setOutput("The iPhone had Successfully sent an " + projectName + " to the plug");
		         }
		      }
		      proc.waitFor();
		      in.close();
		      out.close();
		      proc.destroy();
		      
		      
		      
		   }
		   catch (Exception e) {
		      e.printStackTrace();
		   }
		   
		
		
		
			}
		
		// Return FSIM output
	    try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	    return to;
		
	}
	
	
	
	
	
	
	

	
	

	}

