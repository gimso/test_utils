package technicianCode;

import global.PropertiesUtil;

import java.util.List;

import adb.AdbUtil;
import beans.PhoneType;

/**
 * This class is utility for implementing technician codes on Android devices
 *
 * @author Yehuda Ginsburg
 */
public class AndroidTechCodes {

	private AdbUtil adbUtil;

	public AndroidTechCodes() {
		this.adbUtil = new AdbUtil();
	}

	/**
	 * Received a phone number and call if the Process failed it return false
	 * 
	 * Note: return false just if there is internal error, it still can not have
	 * service at all and return true!
	 */
	private boolean call(String phoneNumber, PhoneType phone) {
		adbUtil.call(phoneNumber, phone.getTestData().getDeviceSN());
		return true;
	}

	/**
	 * Dial FSIM code (****01)(after 0.6.22 ver 00001)
	 * 
	 * @param phone
	 */
	public void fsim(PhoneType phone) {
		System.out.println("AndroidTechCodes.fsim()");
		call("000001", phone);
	}

	/**
	 * Dial ECHO code (****02)(after 0.6.22 ver 00002)
	 * 
	 * @param phone
	 */
	public void echo(PhoneType phone) {
		System.out.println("AndroidTechCodes.echo()");
		call("000002", phone);
	}

	/**
	 * Dial Echo's multiple times (according to "NUMBER_OF_ECHOS" define in
	 * properties.json file)
	 * 
	 * @param phone
	 */
	public void multipleEchoes(PhoneType phone) {
		System.out.println("AndroidTechCodes.multipleEchoes()");
		int iterationTime = Integer.valueOf(
				PropertiesUtil.getInstance().getProperty("NUMBER_OF_ECHOS"));

		// LogCatReader.clearLogcat();
		// TODO FIXME - check for: setCallState CONNECTING -> CONNECTING.
		// setCallState CONNECTING -> DIALING,
		// setCallState DIALING -> DISCONNECTED.
		// in less then a second between

		for (int i = 0; i < iterationTime; i++) {
			try {
				echo(phone);
				// random number between 7 -25 seconds
				int random = (int) (Math.random() * 18) + 7;
				Thread.sleep(1000 * random);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Dial Hard Reset code (****03)(after 0.6.22 ver 00003)
	 * 
	 * @param phone
	 */
	public void hardReset(PhoneType phone) {
		call("000003", phone);
	}

	/**
	 * 
	 * @return List of devices serial numbers
	 */
	public List<String> getDeviceSN() {
		return adbUtil.getDeviceSN();
	}

	/**
	 * Reboot the Phone
	 * 
	 * @param phone
	 */
	public void reboot(PhoneType phone) {
		System.out.println("AndroidTechCodes.reboot()");
		adbUtil.reboot(phone.getTestData().getDeviceSN());
	}

}
