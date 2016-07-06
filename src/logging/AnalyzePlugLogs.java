package logging;

import global.FileUtil;
import global.TimeAndDateConvertor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import testing_utils.TestOutput;
/**
 * This is Utility class of analyzing the plug log
 * @author Yehuda Ginsburg
 *
 */
public class AnalyzePlugLogs {
	/**
	 * 
	 * @param File echoPlugFile
	 * @return List<Date> of all echos in plug log
	 */
	public static List<Date> getAllEchosDatesInPlug(File echoPlugFile) {
		String echo = "PLUG: Got technician code to send cloud echo";
		List<String> echofileAsList = FileUtil.listFromFile(echoPlugFile);
		List<Date> allEchosDates = new ArrayList<Date>();
		for (int i = 0; i < echofileAsList.size(); i++) {
			String string = echofileAsList.get(i);
			if (string.contains(echo)) {
				String date = string.substring(0, 23);
				allEchosDates.add(TimeAndDateConvertor
						.convertPlugStringToDate(date));
			}
		}
		return allEchosDates;
	}
	
	/**
	 * 
	 * @return List of loading-fsim Messages 
	 */
	private List<String> loadingFsimMessages() {
		List<String> messages = new ArrayList<String>();
		messages.add("PLUG: Loading FSIM");
		messages.add("PLUG: SIM status is now Using FSIM");
		messages.add("USIM: EF.IMSI is now 080910100000000010");
		messages.add("PLUG: Plug is loading foo IMSI to release baseband");
		messages.add("PLUG: Got technician code to load FSIM and retry provisioning");
		return messages;
	}
	/**
	 * List of loading-rsim Messages 
	 * @return
	 */
	private List<String> loadingRsimMessages() {
		List<String> messages = new ArrayList<String>();

		messages.add("PLUG: Loading RSIM");
		messages.add("PLUG: SIM status is now Using RSIM");
		messages.add("USIM: EF.IMSI is now ");// [0-9]{18}"); //numbers from 0-9  exact 18 char {18}");

		return messages;

	}
	/**
	 * 
	 * @return List of authentications Messages 
	 */
	private List<String> gotAuthenticateMessages() {
		List<String> messages = new ArrayList<String>();
		messages.add("USIM: Got authenticate.");
		messages.add("PLUG: Switched state from Online to Wait auth response");
		messages.add("CLDP: Sending Auth request");
		messages.add("CLDP: Received Auth response");
		messages.add("PLUG: Authentication received in");
		messages.add("USIM: Auth:");
		messages.add("PLUG: Switched state from Wait auth response to Online");
		return messages;

	}
	
	/**
	 * get a list of messages and return TestOutpt (true+message) if finding at list one of them
	 * @param messages
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unused")
	private TestOutput foundOneOfTheString(List<String> messages, File file) {
		
		List<String> fileAsList = FileUtil.listFromFile(file);		
		
		boolean hasIt = false;
		
		check:
		for (String s : fileAsList) {
			for (String m : messages) {
				if (s.contains(m))
					hasIt = true;
				break check;
			}
		}
		
		if (hasIt = true)
			return new TestOutput(true, messages.get(0));
		else
			return new TestOutput(false, "Message " + messages.toString()
					+ " not found");
	}
	
	/**
	 * Check if fsim Succeeded by searching all messages must be during the fsim period of testing 
	 * @param file
	 * @return
	 */
	public static TestOutput checkIfFsimSucceeded(File file) {
		AnalyzePlugLogs apl = new AnalyzePlugLogs();
		List<String> fileAsList = FileUtil.listFromFile(file);
		boolean gotTechCode = false, loadingFSIM = false, startingTimer = false, loadingRSIM = false, gotAuthenticate = false;

		String gotTechCodeMessage 				= "PLUG: Got technician code to load FSIM and retry provisioning";
		String startingTimerMessage 			= "PLUG: Starting t1 timer, set to expire in";
		TestOutput loadingFsimMessage 			= apl.foundOneOfTheString(apl.loadingFsimMessages(), file);
		TestOutput loadingRsimMessage 			= apl.foundOneOfTheString(apl.loadingRsimMessages(), file);//"PLUG: Loading RSIM";
		TestOutput gotAuthenticateMessage 		= apl.foundOneOfTheString(apl.gotAuthenticateMessages(), file);//"USIM: Got authenticate";

		if (loadingFsimMessage.getResult()) {
			System.out.println(loadingFsimMessage.getOutput());
			loadingFSIM = true;
		}
		if (loadingRsimMessage.getResult()) {
			System.out.println(loadingRsimMessage.getOutput());
			loadingRSIM = true;
		}
		if (gotAuthenticateMessage.getResult()) {
			System.out.println(gotAuthenticateMessage.getOutput());
			gotAuthenticate = true;
		}

		for (String s : fileAsList) {
			if (s.contains(gotTechCodeMessage)) {
				System.out.println(s);
				gotTechCode = true;
			}
			if (s.contains(startingTimerMessage)) {
				System.out.println(s);
				startingTimer = true;
			}
		}
		
		String message = " String not found in file";
		if (!gotTechCode)message=gotTechCodeMessage+message;
		if (!startingTimer)message=startingTimerMessage+message; 
		if (!loadingFSIM)message=loadingFsimMessage.getOutput()+message;
		if (!loadingRSIM)message=loadingRsimMessage.getOutput()+message;
		if (!gotAuthenticate)message=gotAuthenticateMessage.getOutput()+message; 		
		
		if (gotTechCode && loadingFSIM && startingTimer && loadingRSIM && gotAuthenticate) {
			return new TestOutput(true, "FSIM Test succeeded");
		} else {
			return new TestOutput(false, message + ", FSIM Test failed");
		}
	}

	/**
	 * Check if the plug gets Authentication by messages in plug file
	 * @param logFile
	 * @return
	 */
	public static TestOutput checkIfGetAuthentication(File logFile) {
		AnalyzePlugLogs apl = new AnalyzePlugLogs();
		boolean gotAuthenticate;
		TestOutput gotAuthenticateMessage = apl.foundOneOfTheString(apl.gotAuthenticateMessages(), logFile);
		gotAuthenticate = gotAuthenticateMessage.getResult();

		if (gotAuthenticate) {
			return new TestOutput(true, "Sim transition Test succeeded");
		} else {
			String message = gotAuthenticateMessage.getOutput() + " String not found in file";
			return new TestOutput(false, message
					+ ", Sim transition Test failed");
		}

	}
	
	/**
	 * 
	 * @param expectedConfiguration
	 * @return
	 */
	public static TestOutput checkTheVersionConfig(
			String expectedConfiguration, File plugLogFile) {
		List<String> fileAsList = FileUtil.listFromFile(plugLogFile);
		Boolean result = false;
		String output = "";
		for (String s : fileAsList) {
			String configName = "Configuration name " + expectedConfiguration;
			if (s.contains(configName)) {
				output = "found it\n" + s;
				result = true;
			}
		}
		return new TestOutput(result, output);

	}
	public static void main(String[] args) {
		File plugLogFile = new File("C:\\Users\\Yehuda\\Plug-Logs\\Plug_Log_slowCloudConnection_2015-11-08T13-29-36.821+0200.txt");
		TestOutput checkTheVersionConfig = checkTheVersionConfig("delayed_connection_staging_sgs4", plugLogFile );
		System.out.println(checkTheVersionConfig);
	}
}