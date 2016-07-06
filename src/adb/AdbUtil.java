package adb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import logging.LogcatLogger;

/**
 * This class implements ADB</br> It executed adb commands, to manipulate
 * android devices. i.e. calling, checking data, make a reboot etc. This
 * implementation created to avoid the need, to create an Instrumentation
 * application and do all this manipulation in the background of the application
 * as a Service
 * 
 * @author Yehuda Ginsburg
 */
public class AdbUtil {

	private static final String ADB_OUTPUT = "AdbOutput";
	private static final String LOCAL_DISK = "/c";
	private static final String CMD_EXE = "cmd.exe";
	private static final String HOME = System.getProperty("user.home");
	private static final String ADB_PATH = "cd " + HOME
			+ "\\android-sdks\\platform-tools && ";

	private Map<String, String> devices;
	private List<String> deviceSN;

	/**
	 * CTOR the class must initialized the devices and devicesSN before do any
	 * command
	 */
	public AdbUtil() {
		this.devices = getDevicesSerialNumbersAndModel();
		this.deviceSN = printDevicesMap(devices);
	}

	/**
	 * run command on cmd if process is alive after 5 seconds the process
	 * will destroy
	 * 
	 * @param cmd
	 * @return
	 */
	public static Process executeCommandLine(String cmd) {
		try {
			String[] cmdarray = new String[] { CMD_EXE, LOCAL_DISK, cmd };
			Process process = Runtime.getRuntime().exec(cmdarray);
			try {
				process.waitFor(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return process;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * run adb command on cmd and save the output in local file if process is
	 * alive after 5 seconds the process will destroy
	 * 
	 * @param cmd
	 */
	public static synchronized File runAdb(String command, String pathOfFile) {
		try {
			String[] cmdarray = new String[] { CMD_EXE, LOCAL_DISK,
					command + " >> " + pathOfFile };

			Runtime.getRuntime().exec(cmdarray);
			if (!new File(pathOfFile).isFile())
				throw new RuntimeException("file " + pathOfFile
						+ " does not exist");
			Thread.sleep(5000);

			return new File(pathOfFile);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Received a Map of devices names and s/n and return the s/n only as a List
	 * 
	 * @param devices
	 * @return List of devices s/n
	 */
	public static List<String> printDevicesMap(Map<String, String> devices) {
		List<String> devicesSerialNumbers = new ArrayList<String>();
		for (Map.Entry<String, String> entry : devices.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			devicesSerialNumbers.add(key);
			System.out.println("serial number = " + key + " , " + value);
		}
		return devicesSerialNumbers;

	}

	/**
	 * Extract from getDevicesSerialNumbersAndModel method a map of Serial as a
	 * key and Model as a value
	 * 
	 * @return Map of serial and model
	 */
	public static Map<String, String> getSerialANdModel() {
		Map<String, String> serialAndModel = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : getDevicesSerialNumbersAndModel()
				.entrySet()) {
			serialAndModel.put("Serial", entry.getKey());

			serialAndModel.put("Model", entry.getValue().substring(6));
		}
		return serialAndModel;
	}

	/**
	 * Running adb command "devices" to get list of s/n of devices, return a Map
	 * with s/n and device name, if no devices print error message
	 */
	public static Map<String, String> getDevicesSerialNumbersAndModel() {

		Map<String, String> map = new HashMap<String, String>();

		String twoSpaces = "  ";
		String oneSpace = " ";
		
		String[] commands = { CMD_EXE, LOCAL_DISK, "adb devices -l" };

		try {
			Process process = Runtime.getRuntime().exec(commands);

			try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

				// read the output from the command
				String s = null;

				while ((s = stdInput.readLine()) != null) {
					String key = null;
					String value = null;

					if (s.contains(twoSpaces)) {
						String[] strings = s.split(twoSpaces);
						key = strings[0];

						for (String st : s.split(oneSpace))
							if (st.startsWith("model:"))
								value = st;
					}
					
					if (key != null && value != null)
						map.put(key, value);
				}
				
			} catch (IOException e) {
				throw e;
			}

			process.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (map.isEmpty())
			System.err.println("Android devices/emulators are not connected");

		return map;
	}

	/**
	 * extracting list of all models of android devices connected
	 * 
	 * @return List
	 * @throws IOException
	 */
	public static List<String> getModels() throws IOException {
		List<String> models = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		String s = null;

		String[] commands = { CMD_EXE, LOCAL_DISK, "adb devices -l" };
		Process process = Runtime.getRuntime().exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		// read the output from the command
		while ((s = stdInput.readLine()) != null) {
			builder.append(s);
		}

		String string = builder.toString();
		Set<String> set = new HashSet<String>(Arrays.asList(string.split(" ")));
		for (String se : set)
			if (se.contains("model"))
				models.add(se.substring(6));
		return models;
	}

	/**
	 * Receiving a device s/n and running the netcfg command.</br>This command
	 * return a list of connection the phone are using. </br>The "rmnet_usb0"
	 * point on the data from the local operator, </br>The return value are true
	 * if there is data available. </br><b>Note</b>: if the phone has wifi
	 * enabled, it "rmnet_usb0" will be DOWN even if there is a availability for
	 * data from the local operator </br>Make sure to use wifiDisabling() method
	 * before;
	 */
	public boolean checkInternetConnection() {
		for (String device : deviceSN) {
			String command = "adb -s " + device + " shell  netcfg";
			String s = runADBGetString(command);
			if (s.contains("rmnet_usb0 DOWN")) {
				System.err.println("No Data Connection");
				return false;
			} else if (s.contains("rmnet_usb0 UP")) {
				System.out.println("There is Data Connection");
				return true;
			}
		}
		return false;
	}

	/**
	 * after reboot try to reconnect to adb when adb recognized the device wait
	 * another 30 seconds then call the number received as parameter
	 * 
	 * @param phoneToCall
	 */
	public void callAfterReboot(String phoneToCall) {
		for (int i = 0; i < 6;) {
			try {
				this.devices = getDevicesSerialNumbersAndModel();
				this.deviceSN = printDevicesMap(this.devices);
				System.out.println("Yeeh Connected");

				sleep(30000);
				i = 50;
				this.call(phoneToCall);
			} catch (Exception e) {
				sleep(5000);
				System.err.println("Not Connected Yet");
				i++;
			}
		}
	}

	/**
	 * Right after reboot, the phone is not up and need a time to be fully
	 * 'awake'</br> the method wait for 15 seconds, Then try to re-connect, if
	 * adb is not recognized the device it wait another 10 seconds and try
	 * again.<br/>
	 * If it recognized by adb, Wait for another 1.2 Minutes, <br/>
	 * <i>The reason is that after the phone is up we wait until there is a
	 * fully service again.</i> then return true.<br/>
	 * if after 9 times (1.30 Min) the phone is not up, return false.<br/>
	 * 
	 * @return true phone is up
	 */
	public boolean afterReboot() {
		boolean isOn = false;
		sleep(15000);
		for (int i = 1; i < 9;) {
			try {
				this.devices = getDevicesSerialNumbersAndModel();
				this.deviceSN = printDevicesMap(this.devices);
				System.out.println("Yeeh Connected");
				i = 60;
				sleep(80000);
				return true;
			} catch (Exception e) {
				String waitFor = "now is waiting " + i + "0 sec";
				System.err.println("Not Connected Yet, " + waitFor
						+ " Try again");
				sleep(10000);
				i++;
				isOn = false;
			}
		}
		return isOn;
	}

	/**
	 * reboot all devices
	 */
	public void reboot() {
		for (String device : deviceSN) {
			String command = "adb -s " + device + " reboot";
			runADB(command);
		}
	}
	public void reboot(String deviceSN) {
		System.out.println("AndroidTechCodes.reboot()");
		String command = "adb -s " + deviceSN
				+ " reboot";
		AdbUtil.executeCommandLine(command);

	}

	/**
	 * Simple Thread.sleep Received milliseconds to wait
	 */
	public void sleep(int milliSeconed) {
		try {
			Thread.sleep(milliSeconed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * click the power button (not reboot just click!) used to wake up devices
	 */
	public void clickPowerButton() {
		System.out.println("ADBMethods.powerButton()");
		for (String device : deviceSN) {
			String command = "adb -s " + device + " shell input keyevent 26";
			runADB(command);
		}
	}

	/**
	 * wake up the device, by click the power button and swipe to unlock the
	 * device works fine on SGS4 only
	 */

	public void wakeUp() {
		System.out.println("ADBMethods.wakeUp()");

		for (String device : deviceSN) {
			String powerButton = /* "adb shell */" input keyevent 26";
			// String swipeToUnlock = /* "adb shell */"z";
			String swipeToUnlock = /* "adb shell */"input swipe 532 1900 595 17";
			// String swipeToUnlock2 = /* "adb shell */"input swipe 400 1900 600
			// 17";
			// String swipeToUnlock3 = /* "adb shell */"input swipe 300 1900 700
			// 17";

			String command = "adb -s " + device + " shell \"" + powerButton
					+ " && " + swipeToUnlock;
			// + " && " + swipeToUnlock2 + " && " + swipeToUnlock3 +"\"";
			runADB(command);
		}
	}

	/**
	 * Received a phone number and call if the Process failed it return false
	 * 
	 * Note: return false just if there is internal error, it still can not have
	 * service at all and return true!
	 * 
	 */

	public boolean call(String phoneNumber, String deviceSN) {
		String command = "adb -s " + deviceSN
				+ " shell am start -a android.intent.action.CALL -d tel:"
				+ phoneNumber;
		AdbUtil.executeCommandLine(command);
		return true;
	}

	/**
	 * 
	 * Received a phone number and call if the Process failed it return false
	 * 
	 * Note: return false just if there is internal error, it still can not have
	 * service at all and return true!
	 * 
	 * Throws RuntimeException if adb didn't get the command
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public boolean call(String phoneNumber) {
		for (String device : deviceSN) {
			String command = "adb -s " + device
					+ " shell am start -a android.intent.action.CALL -d tel:"
					+ phoneNumber;
			Process process = runADB(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String s = null;
			try {
				while ((s = stdInput.readLine()) != null) {
					String call = "Starting: Intent { act=android.intent.action.CALL dat=tel:xxxxx }";
					if (!s.contains(call)) {
						throw new RuntimeException(call + "not exist");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Commands are not fully implemented yet <<airplane mode on>> adb shell
	 * settings put global airplane_mode_on 1; am broadcast -a
	 * android.intent.action.AIRPLANE _MODE --ez state true
	 * 
	 * <<airplane mode off>> adb shell settings put global airplane_mode_on 0;
	 * am broadcast -a android.intent.action.AIRPLANE _MODE --ez state true
	 * 
	 * //adb shell settings put global data_roaming 1; am broadcast -a
	 * android.intent.action.AIRPLANE _MODE --ez state true
	 * //android.permission.CHANGE_NETWORK_STATE //
	 * android.net.conn.CONNECTIVITY_CHANGE
	 * 
	 * <wifi disabling> 1. adb shell settings put global wifi_on 0 2. adb reboot
	 * --the wifi will be turn off when boot again
	 * 
	 */

	/**
	 * enable the airplane-mode, needs a reboot to be fully affected
	 */
	public void airplaneModeOn() {
		for (String device : deviceSN) {
			String command = "adb -s " + device + " shell "
					+ " settings put global airplane_mode_on 1; "
					+ "am broadcast -a android.intent.action.AIRPLANE_MODE"
					+ " --ez state true";
			String command2 = "adb -s " + device + " reboot";
			runADBMultiCommands(command, command2);
			afterReboot();
		}
	}

	/**
	 * disable the airplane-mode, needs a reboot to be fully affected
	 */
	public void airplaneModeOff() {
		for (String device : deviceSN) {
			String command = "adb -s " + device + " shell "
					+ "settings put global airplane_mode_on 0; "
					+ "am broadcast -a android.intent.action.AIRPLANE_MODE"
					+ " --ez state true";
			String command2 = "adb -s " + device + " reboot";
			runADBMultiCommands(command, command2);
			afterReboot();
		}
	}

	/**
	 * wifi disabling, needs a reboot to be fully affected
	 */
	public void wifiDisabling() {
		for (String device : deviceSN) {
			String command = "adb -s " + device + " shell "
					+ " settings put global wifi_on 0";
			String command2 = "adb -s " + device + " reboot";
			runADBMultiCommands(command, command2);
			afterReboot();
		}
	}

	/**
	 * shut down the device
	 */
	public void powerOff() {
		for (String device : deviceSN) {
			String command = "adb -s " + device + " shell " + "reboot -p";
			runADB(command);
		}
	}

	/**
	 * ends phone calls if there is any for all devices connected
	 */

	public void endCalls() {
		System.out.println("ADBMethods.endCalls()");
		for (String device : deviceSN) {
			String command = "adb -s " + device
					+ " shell input keyevent KEYCODE_ENDCALL";
			runADB(command);
		}
	}

	/**
	 * ends phone calls if there is any for specific device
	 * 
	 * @param phone
	 */
	public void endCalls(String deviceSN) {
		String command = "adb -s " + deviceSN
				+ " shell input keyevent KEYCODE_ENDCALL";
		AdbUtil.executeCommandLine(command);
	}

	/**
	 * Receiving two commands and run them one after the other, best for 2
	 * commands that dependent on each other
	 * 
	 * @param command
	 * @param command2
	 * @return the process to reuse the adb prints on the console.
	 */
	public Process runADBMultiCommands(String command, String command2) {
		Process process = null;
		try {

			String[] commands = { CMD_EXE, LOCAL_DISK, ADB_PATH + command };
			String[] commands2 = { CMD_EXE, LOCAL_DISK, ADB_PATH + command2 };

			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(commands);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.err.println(s);
				throw new RuntimeException(s);
			}
			runtime.exec(commands2);
			BufferedReader stdInput2 = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			BufferedReader stdError2 = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			String s2 = null;
			while ((s2 = stdInput2.readLine()) != null) {
				System.out.println(s2);
			}
			// read any errors from the attempted command
			while ((s2 = stdError2.readLine()) != null) {
				System.err.println(s2);
				throw new RuntimeException(s2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return process;
	}

	/**
	 * combined together String array the Runtime class needs to run an adb
	 * command<br/>
	 * print out the console messages, if error accrued print an error messages
	 * 
	 * @return the process to reuse the adb prints on the console.
	 */

	public Process runADB(String command) {
		Process process = null;
		try {
			String[] commands = { CMD_EXE, LOCAL_DISK, ADB_PATH + command };

			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(commands);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.err.println(s);
				throw new RuntimeException(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return process;
	}

	/**
	 * same as runADB
	 * 
	 * @param command
	 * @return the String that printed in the process
	 * 
	 */
	public String runADBGetString(String command) {
		Process process = null;
		String output = null;
		try {

			String[] commands = { CMD_EXE, LOCAL_DISK, ADB_PATH + command };

			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(commands);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				output += s;
			}
			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.err.println(s);
				throw new RuntimeException(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * send sms from all devices connected FIXME sends sms messages, for now
	 * works just if phones screens 'awake'.
	 * 
	 * @param smsMessage
	 * @param phoneNumber
	 */
	public void SMSSender(String smsMessage, String phoneNumber) {
		System.out.println("ADBMethods.sendSMS()");
		for (String device : deviceSN) {
			String command = "adb -s "
					+ device
					+ " shell \"am start -a android.intent.action.SENDTO -d sms:"
					+ phoneNumber + " --es sms_body " + "\"" + smsMessage
					+ "\" " + "&& input keyevent 61 && input keyevent 66 \"";
			runADB(command);
		}
	}

	/**
	 * open a web browser of the url address parameter for now works just if
	 * phones screens 'awake'.
	 * 
	 * @param URLAddress
	 */
	public void connectToUrl(String URLAddress) {
		System.out.println("ADBMethods.goToURLAndCheckData()");

		String enter = "input keyevent KEYCODE_ENTER";
		String tab = "input keyevent KEYCODE_TAB";

		for (String device : deviceSN) {
			String command = "adb -s " + device + " " + "shell \"am start -a "
					+ "android.intent.action.VIEW " + "-d " + URLAddress
					+ " && " + tab + " && " + enter + "\"";
			runADB(command);
		}
	}

	/**
	 * dial the number but not call use for all devices connected
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public boolean dial(String phoneNumber) {
		for (String device : deviceSN) {
			String command = "adb -s " + device
					+ " shell am start -a android.intent.action.DIAL -d tel:"
					+ phoneNumber;
			Process process = runADB(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String s = null;
			try {
				while ((s = stdInput.readLine()) != null) {
					String call = "Starting: Intent { act=android.intent.action.CALL dat=tel:xxxxx }";
					if (!s.contains(call)) {
						throw new RuntimeException(call + "not exist");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public Map<String, String> getDevices() {
		return devices;
	}

	public List<String> getDeviceSN() {
		return deviceSN;
	}
	
	/**
	 * 
	 * @return true if mobile has data available
	 */
	public boolean isMobileDataAvailable() {
		String cmdToFile = new LogcatLogger().getPathAndTime(ADB_OUTPUT);
		String command = "adb logcat -s -v time STATUSBAR-NetworkController:v | find \"onDataConnectionStateChanged: state=\"";

		File file = AdbUtil.runAdb(command, cmdToFile);
		try {
			String check = new String(Files.readAllBytes(file.toPath()));
			String[] strings = check.split("\n");
			check = strings[strings.length - 1];
			if (check.contains("state=0")) {
				return false;
			} else if (check.contains("state=2")) {
				return true;
			} else {
				throw new RuntimeException("cannot get status from log");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage() + "\ncannot get status from log");
		}
	}
	
	/**
	* Reboot the device then wait for device to get connected
	*
	*/
	public static void rebootThenWaitForDevice() {
		executeCommandLine("adb reboot");
		isDeviceConnected();
	}
	
	/**
	* send adb logcat coammand, it wait by default to device until log stream started, unless there is unexpected exception it reruns true;
	*/
	private static boolean isDeviceConnected() {
		try {
			
			Process process = Runtime.getRuntime().exec(new String[] { CMD_EXE, LOCAL_DISK,"adb logcat"});

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String s = null;
			while ((s = stdInput.readLine()) != null){
				
				try {Thread.sleep(1200);} catch (InterruptedException e) {}
				
				if (!s.equals("- waiting for device -")){
					//in order to avoid offline issues, wait 5 seconds after getting connection
					try {Thread.sleep(5000);} catch (InterruptedException e) {}
					
					return true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isDeviceOffline() {
		String offline = "offline";
		String command =  "adb devices -l" ;
		Process process = executeCommandLine(command);
		
		try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				if (s.contains(offline))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (process != null)
			process.destroy();
		return false;
	}
}
