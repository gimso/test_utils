package global;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.Reporter;

import beans.PhoneType;
import beans.SimStatus;
import jdbc.JDBCUtil;
import persist.usage.Allocations;
import persist.usage.Plugs;
import technicianCode.AndroidTechCodes;
import testing_utils.TestOutput;

public class PlugUtil {

	private static final String USIM = "USIM";
	private static final String SIM = "SIM";

	/**
	 * Get the plug id from PhoneType as parameter , Get a list from the DB of
	 * all allocated SIM's, Search by the comment (the comment insert by QA
	 * department when physically potting the RSIM in the SIM unit) if its USIM
	 * or SIM.
	 * 
	 * @param PhoneType
	 * @return TestOutput
	 */
	public static boolean isPlugAllocatedToUsim(PhoneType phone) {
		String plugId = phone.getTestData().getPlugId();
		
		SimStatus simStatus = new JDBCUtil().getPlugSimStatus(plugId);
		
		if (simStatus == null){
			System.err.println("Cannot find allocation for plug "+plugId);
			return false;
		}
		
		if (simStatus.isUsim())
			return true;
		else if (simStatus.isSim()){
			System.err.println("Plug "+plugId+" is allocated to SIM instead of USIM");
			return false;
		}else {
			System.err.println("Plug "+plugId+" is not allocated");
			return false;
		}
	}	
	
	/**
	 * check if plug allocated to SIM
	 * @param phone
	 * @return
	 */
	public static boolean isPlugAllocatedToSim(PhoneType phone) {
		String plugId = phone.getTestData().getPlugId();
		
		SimStatus simStatus = new JDBCUtil().getPlugSimStatus(plugId);
		
		if (simStatus == null){
			System.err.println("Cannot find allocation for plug "+plugId);
			return false;
		}
		
		if (simStatus.isSim())
			return true;
		else if (simStatus.isUsim()){
			System.err.println("Plug "+plugId+" is allocated to USIM instead of SIM");
			return false;
		}else{
			System.err.println("Plug "+plugId+" is not allocated");
			return false;
		}
	}	

	/**
	 * Get a list of all 'forced' plugs, <br>
	 * Get the plug id from PhoneType parameter,<br>
	 * If exist in the list 'unforce' it <br>
	 * (if not its already not forced)<br>
	 * 
	 * @param phone
	 * @throws PersistException
	 */
	public static void unforcingRSIMAllocation(PhoneType phone) throws PersistException {
		Plugs plugs = new Plugs();
		List<String> plugIds = new JDBCUtil().getAllForcedPlugs();

		if (plugIds != null) {
			for (String plug : plugIds) {
				if (phone.getTestData().getPlugId().equalsIgnoreCase(plug)) {
					plugs.modifyById(plug, null, null, null, null, null, null, null, null, "---------", null, null,
							null, null, null);
				}
			}
		}
	}

	

	/**
	 * check for all RSIM available according to the parameter, then force plug
	 * to to one of them if there is indeed free RSIM to force.
	 * 
	 * @param rsimType
	 * @param phone
	 * @return
	 * @throws PersistException
	 */
	public static TestOutput forceSim(String rsimType, PhoneType phone) {

		List<String> simsAvailble = null;
		if (rsimType.equals(SIM))
			simsAvailble = getAllAvailableSIMFromSimUnit();
		else if (rsimType.equals(USIM))
			simsAvailble = getAllAvailableUSIMFromSimUnit();
		else
			return new TestOutput(false, rsimType + " Rsim Type not found");

		boolean forced = false;
		try {
			forced = forcePlugToAnotherSim(simsAvailble, phone.getTestData().getPlugId());
		} catch (PersistException e) {
			e.printStackTrace();
		}

		if (forced)
			return new TestOutput(true, phone.getTestData().getDeviceName() + "was forced to " + rsimType);
		else
			return new TestOutput(false, "Unknown Error when forcing Sim");
	}

	/**
	 * Get all SIM's SimStatus and check if its not forced, allowed, not
	 * allocated and if its RSIM type is USIM and return them if they exist
	 * 
	 * @return
	 */
	public static List<String> getAllAvailableUSIMFromSimUnit() {
		List<String> sims = new ArrayList<String>();
		List<SimStatus> simStatus = new JDBCUtil().getAllSimsStatus();
		for (SimStatus sim : simStatus) {
			if (sim.getPlugId().equals("None") && sim.getAllowed() == 1 && sim.getForced().equals("No") && sim.isUsim())
				sims.add(sim.getSimName());
		}
		return sims;
	}

	/**
	 * Get all SIM's SimStatus and check if its not forced, allowed, not
	 * allocated and if its RSIM type is SIM and return them if they exist
	 * 
	 * @return
	 */
	public static List<String> getAllAvailableSIMFromSimUnit() {
		List<String> sims = new ArrayList<String>();
		List<SimStatus> simStatus = new JDBCUtil().getAllSimsStatus();

		for (SimStatus sim : simStatus) {
			if (sim.getPlugId().equals("None") && sim.getAllowed() == 1 && sim.getForced().equals("No") && sim.isSim())
				sims.add(sim.getSimName());
		}
		return sims;
	}

	/**
	 * when get a list of available SIM's and the plug id, first delete the
	 * allocation of the specific plug. then go to usage.plug/(plug-id) page,
	 * force it to first available SIM in the list and save.
	 * 
	 * @param simsAvailble
	 * @param plugId
	 * @return true if there is no error when trying to force
	 * @throws PersistException
	 */
	public static boolean forcePlugToAnotherSim(List<String> simsAvailble, String plugId) throws PersistException {
		Allocations allocations = new Allocations();
		Plugs plugs = new Plugs();

		allocations.deleteByPlugId(plugId);

		if (simsAvailble.size() > 0) {
			String rv = plugs.modifyById(plugId, null, null, null, null, null, null, null, null, simsAvailble.get(0),
					null, null, null, null, null);

			return rv.contains("changed successfully") ? true : false;
		} else {
			System.err.println("there no availble RSIM to force");
			return false;
		}
	}

	/**
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneTestable(PhoneType phone) {

		ITestResult iTestResult = Reporter.getCurrentTestResult();	
		
		boolean allocated = isAllocated(phone);
		
		if (allocated == false) {
			iTestResult.setAttribute("isPhoneTestable", false);
			return false;
		}

		// if its Android
		if (phone.getOs()==PhoneType.OS.ANDROID) {
			List<String> deviceSN = new AndroidTechCodes().getDeviceSN();
			if (isAndroidDeviceConnected(deviceSN, phone)) {
				iTestResult.setAttribute("isPhoneTestable", true);
				return true;
			}
		// if its ios
		} else if (phone.getOs()==PhoneType.OS.IOS) {
			iTestResult.setAttribute("isPhoneTestable", true);
			return true;
		// null
		} else 
			iTestResult.setAttribute("isPhoneTestable", false);
			
		return false;
		
	}

	/**
	 * check if plug is allocated to SIM or USIM depending on phone.getRsim()
	 * @param phone
	 * @return
	 */
	public static boolean isAllocated(PhoneType phone) {
		if (phone.getRsim() == null)
			return false;

		switch (phone.getRsim()) {
		case SIM:
			return isPlugAllocatedToSim(phone);
		case USIM:
			return isPlugAllocatedToUsim(phone);
		default:
			return false;
		}
	}
	
	/**
	 * check if plug is allocated to SIM or USIM depending on phone.getRsim()
	 * @param phone
	 * @return
	 */
	public static boolean isAllocationChanged(PhoneType phone) {
		if (phone.getRsim() == null)
			return false;

		switch (phone.getRsim()) {
		case SIM:
			return isPlugAllocatedToUsim(phone);
		case USIM:
			return isPlugAllocatedToSim(phone);
		default:
			return false;
		}
	}

	/**
	 * Check if Android device is connected now to PC
	 * 
	 * @param deviceSN
	 *            adb utility get's all devices connected
	 * @param phone
	 *            testData/deviceSN - from test_data.json
	 * @return TestOutput
	 */
	private static boolean isAndroidDeviceConnected(List<String> deviceSN, PhoneType phone) {
		for (String s : deviceSN) {
			if (s.equalsIgnoreCase(phone.getTestData().getDeviceSN())) {
				System.out.println("ME " + phone.getTestData().getDeviceSN() + " connected");
				return true;
			}
		}
		System.err.println("ME " + phone.getTestData().getDeviceSN() + " is not connected");
		return false;
	}

}
