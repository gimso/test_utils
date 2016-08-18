package global;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

import logging.AnalyzePlugLogs;
import logging.Analyze_cloud_logs;
import testing_utils.TestOutput;
import testrail.api.TestRailAPI;
import beans.SimgoLogger;
import beans.PhoneType;

/**
 * This is a Utility for Plug and Persist/Cloud Test. Update testsRail ,
 * checking the os type opening logs etc.
 * 
 * @author Yehuda Ginsburg
 * 
 */
public class TestUtil {

	private static final String TEST_RAIL_RESTAPI_SUITE = "TEST_RAIL_RESTAPI_SUITE";
	private static final String TEST_RAIL_IOS_SUITE = "TEST_RAIL_IOS_SUITE";
	private static final String TEST_RAIL_ANDROID_SUITE = "TEST_RAIL_ANDROID_SUITE";
	private static final String TEST_RAIL_PERSIST_SUITE = "TEST_RAIL_PERSIST_SUITE";
	private static final String TEST_RAIL_PLUG_SUITE = "TEST_RAIL_PLUG_SUITE";
	private static final String TEST_RAIL_SVCP_SUITE = "TEST_RAIL_SVCP_SUITE";
	private static final String TEST_RAIL_GATEWAY_SUITE = "TEST_RAIL_GATEWAY_SUITE";
	private static final String TEST_RAIL_USER_PASSOWRD = "TEST_RAIL_USER_PASSOWRD";
	private static final String TEST_RAIL_USER_EMAIL = "TEST_RAIL_USER_EMAIL";
	
	private static final String NUMBER_OF_ECHOS = "NUMBER_OF_ECHOS";
	private static final String EC2_PERSIST_URL_INVENTORY = "EC2_PERSIST_URL_INVENTORY";

	private static final String MAC = "Mac";
	private static final String WINDOWS = "Windows";

	private static final String IOS = "IOS";
	private static final String ANDROID = "ANDROID";
	private static final String SVCP = "SVCP";
	private static final String PLUG = "PLUG";
	private static final String CLOUD = "CLOUD";
	private static final String GATEWAY = "GATEWAY";
	
	public static final String USIM = "USIM";
	public static final String SIM = "SIM";


	/**
	 * Check the Operation System of the phone- [declared on test_data.json] and
	 * the OS of the running computer
	 * 
	 * @param phone
	 * @return if its IOS and MAC return true
	 */
	public static boolean isIosAndMac(PhoneType phone) {
		return phone.getTestData().getOperatingSystem().contains(IOS) && System.getProperty("os.name").contains(MAC);
	}

	/**
	 * Check the Operation System of the phone- [declared on test_data.json] and
	 * the OS of the running computer
	 * 
	 * @param phone
	 * @return if its ANDROID and WINDOWS return true
	 */
	public static boolean isAndroidAndWindowsOs(PhoneType phone) {
		return phone.getTestData().getOperatingSystem().contains(ANDROID)
				&& System.getProperty("os.name").contains(WINDOWS);
	}

	/**
	 * Check Internet connection availability by entrance to TestRail web-site
	 * 
	 * @return boolean
	 */
	public static boolean isInternetReachable() {
		String ec2_url = PropertiesUtil.getInstance().getProperty(EC2_PERSIST_URL_INVENTORY);
		try {

			// make a URL to a known source
			URL ec2Persist = new URL(ec2_url);

			// open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) ec2Persist.openConnection();

			// trying to retrieve data from the source. If there
			// is no connection, this line will fail
			urlConnect.getContent();

		} catch (IOException e) {
			System.err.println(ec2_url + " is not availble - skipping the tests");
			return false;
		}
		try {
			URL testrail = new URL("https://gimso.testrail.com");
			HttpURLConnection urlConnect = (HttpURLConnection) testrail.openConnection();
			urlConnect.getContent();
		} catch (IOException e) {
			System.err.println("https://gimso.testrail.com is not availble - skipping the tests");
			return false;
		}
		return true;
	}

	/**
	 * Initialized testRailResult class - get the Plan Name and check if its
	 * cloud or plug and add plan if not exist according to the suite (plug or
	 * cloud)
	 * 
	 * @param planName
	 * @param string
	 * @return
	 */
	public static TestRailAPI getTestRailAPIInstance(String planName, String configName) {
		if (configName == null)
			throw new RuntimeException("config name is null - cannot initialized TestARil");
		
		boolean containsPlug = configName.toUpperCase().contains(PLUG);
		boolean containsCloud = configName.toUpperCase().contains(CLOUD);
		boolean containsAndroid = configName.toUpperCase().contains(ANDROID);
		boolean containsIos = configName.toUpperCase().contains(IOS);
		boolean containsSVCP = configName.toUpperCase().contains(SVCP);
		boolean containsGW = configName.toUpperCase().contains(GATEWAY);;

	
		Long suiteId = null;
		if (containsPlug)
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_PLUG_SUITE));
		else if (containsCloud)
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_PERSIST_SUITE));
		else if (containsAndroid)
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_ANDROID_SUITE));
		else if (containsIos)
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_IOS_SUITE));
		else if (containsSVCP)
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_SVCP_SUITE));
		else if (containsGW)
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_GATEWAY_SUITE));
		else
			throw new RuntimeException("config name " + configName + " is not valid - cannot initialized TestARil");

		String user = PropertiesUtil.getInstance().getProperty(TEST_RAIL_USER_EMAIL);
		String password = PropertiesUtil.getInstance().getProperty(TEST_RAIL_USER_PASSOWRD);
		TestRailAPI testRailApi = TestRailAPI.getInstance(user, password);

		testRailApi.setSuiteId(suiteId);
		testRailApi.setPlan(testRailApi.addPlanIfNotExist(planName, suiteId, configName));
		
		return testRailApi;
	}
	
	
	/**
	 * Initialized testRailResult class for RestAPI (app-cloud/telemetry) tests
	 * get the Plan Name and plan if doesn't exist according to the suite 
	 * @param planName
	 * @return
	 */
	public static TestRailAPI getTestRailAPIInstanceRestApi(String planName) {	
		String testRailUserPassowrd =	PropertiesUtil.getInstance().getProperty(TEST_RAIL_USER_PASSOWRD);
		String testRailUserEmail =		PropertiesUtil.getInstance().getProperty(TEST_RAIL_USER_EMAIL);
		Long restApiSuiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_RESTAPI_SUITE));

		TestRailAPI testRailApi = TestRailAPI.getInstance(testRailUserEmail, testRailUserPassowrd);
		testRailApi.setSuiteId(restApiSuiteId);
		testRailApi.setPlan(testRailApi.addPlanIfNotExist(planName, restApiSuiteId, CLOUD));
		return testRailApi;
	}

	/**
	 * used for simgo application tests.
	 * 
	 * @param planName
	 * @param suiteId
	 */
	public static TestRailAPI testRailInitApplication(String planName, Long suiteId) {
		String configName;
		if (suiteId == Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_ANDROID_SUITE)))
			configName = ANDROID;
		else if (suiteId == Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_IOS_SUITE)))
			configName = IOS;
		else
			return null;
		return getTestRailAPIInstance(planName, configName);
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
	public static TestOutput isDeviceConnected(List<String> deviceSN, PhoneType phone) {
		for (String s : deviceSN) {
			if (s.equalsIgnoreCase(phone.getTestData().getDeviceSN())) {
				return new TestOutput(true, "");
			}
		}
		return new TestOutput(false, "Device " + phone.getTestData().getDeviceSN() + " not found");
	}

	/**
	 * Shortcut to open logs of cloud and plug from Logger class
	 * 
	 * @param logger
	 */
	public static void openLogs(SimgoLogger logger) {
		logger.getCloudLogger().readCloudLog();
		logger.getPlugLogger().readPlugLog();
	}

	/**
	 * Shortcut to close logs of cloud and plug from Logger class
	 * 
	 * @param initTechCodeTest
	 */
	public static void closeLogs(SimgoLogger initTechCodeTest) {
		initTechCodeTest.getPlugLogger().stopPlugLog();
		initTechCodeTest.getCloudLogger().stopCloudLog();
	}

	/**
	 * Get all logs - extract the number of echos performs in each entity
	 * (plug/cloud),
	 *
	 * Check the number of echo's defined in test_data.json Check the size of
	 * echo's in persist and plug logs. Check that there isn't big different
	 * between the time the plug get the Echo to perform, to the time the
	 * message has received by the persist in cloud log, then check the
	 * plug-cloud times. Assert - false if not but not connecting to the other
	 * assert (trying with try and catch)
	 * 
	 * @param logger
	 * @param phone
	 * @return TestOutput
	 */
	public static TestOutput isEchosPerforms(SimgoLogger logger, PhoneType phone) {

		List<Date> allEchosDatesInPlug = AnalyzePlugLogs.getAllEchosDatesInPlug(logger.getPlugLogger().getLogFile());

		List<Date> allEchosDatesInCloud = Analyze_cloud_logs
				.getAllEchosDatesInCloud(logger.getCloudLogger().getLogFile(), phone);

		boolean isNumberEquals = allEchosDatesInPlug.size() == Long
				.valueOf(PropertiesUtil.getInstance().getProperty(NUMBER_OF_ECHOS));

		if (isNumberEquals && allEchosDatesInPlug.size() <= allEchosDatesInCloud.size()) {
			for (int i = 0; i < allEchosDatesInPlug.size(); i++) {
				long diff = TimeAndDateConvertor.getDiffBetweenDates(allEchosDatesInPlug.get(i),
						allEchosDatesInCloud.get(i), TimeUnit.SECONDS);
				if (diff > 10) {
					String errorMessage = "The echo was by the phone device at " + allEchosDatesInPlug.get(i)
							+ " , but was received on the cloud only on " + allEchosDatesInCloud.get(i)
							+ " . The difference is more than 10 seconds";
					System.err.println(errorMessage);
					return new TestOutput(false, errorMessage);
				}
			}
		} else {
			String errorMessgae = "There is less Echo Recored in cloud then what actualy performd in plug";
			return new TestOutput(false, errorMessgae);
		}

		for (Date d : allEchosDatesInPlug) {
			System.out.println("echoDate = " + d.toString());
		}
		for (Date d : allEchosDatesInCloud) {
			System.out.println("echoCloud = " + d.toString());
		}
		return new TestOutput(true, "echo test successid");
	}

	/**
	 * Add result for TestRail parse all needed [plan name, rsim config name,
	 * device name, test output and test name] and update in testrail site
	 * 
	 * @param testOutput
	 * @param phone
	 * @param testRailApi
	 * @param planName
	 * @param rsimConfigName
	 */
	public static void addResultForPlugTest(TestOutput testOutput, PhoneType phone, TestRailAPI testRailApi,
			String planName, String rsimConfigName) {
		TestOutput output = testOutput;

		if (output != null) {
			String deviceName = phone.getTestData().getDeviceName();
			Long runId = testRailApi.getRunIdByPlanAndConfig(planName, deviceName, rsimConfigName);
			String methodName = testRailApi.getMethodName();
			String testName = testRailApi.getFromMethodNameTheTestTitle(methodName, runId);

			if (testName != null) {
				testRailApi.add_result_ByPlugPlan(planName, rsimConfigName, deviceName, output, testName);
			}
		}
	}

	/**
	 * Add in given TestOutput the output of the path to Cloud and Plug logs
	 * 
	 * @param logger
	 * @return
	 */
	public static TestOutput addFileLocationToOutput(SimgoLogger simgoLogger, TestOutput testOutput) {
		testOutput.setOutput(testOutput.getOutput() + "\n Cloud Log file located in: \n"
				+ simgoLogger.getCloudLogger().getLogFile().getAbsolutePath() + "\n Plug Log file located in: \n"
				+ simgoLogger.getPlugLogger().getLogFile().getAbsolutePath());
		return testOutput;
	}

	/**
	 * Blocked all tests for this phone USIM+SIM adding comment explanation in
	 * testOutout - This usually required when the test starts with SIM rather
	 * than USIM
	 * 
	 * @param phone
	 * @param testRailResult
	 * @param planName
	 */
	public static void blockedAll(PhoneType phone, TestRailAPI testRailResult, String planName) {

		String rsimConfig = USIM;
		List<String> tests = testRailResult.getAllTestNames(planName, phone.getTestData().getDeviceName(), rsimConfig);

		String deviceName = phone.getTestData().getDeviceName();
		TestOutput output = new TestOutput(false, "couldn't perform the test, the phone is not using USIM");

		for (String test : tests) {
			String testName = test;
			testRailResult.addBlockedResultForTestByPlan(planName, rsimConfig, deviceName, output, testName);
		}

		rsimConfig = SIM;
		tests = testRailResult.getAllTestNames(planName, phone.getTestData().getDeviceName(), rsimConfig);

		for (String test : tests) {
			String testName = test;
			testRailResult.addBlockedResultForTestByPlan(planName, rsimConfig, deviceName, output, testName);
		}
	}

	/**
	 * Blocked all SIM (not USIM) tests by phone and plan name
	 * 
	 * @param phone
	 * @param testRailApi
	 * @param planName
	 */
	public static void blockedSIMs(PhoneType phone, TestRailAPI testRailApi, String planName) {
		String rsimConfig = SIM;
		List<String> tests = testRailApi.getAllTestNames(planName, phone.getTestData().getDeviceName(), rsimConfig);
		for (String test : tests) {
			TestOutput output = new TestOutput(false, "couldn't perform the test, the phone is not using USIM");
			String deviceName = phone.getTestData().getDeviceName();
			String testName = test;

			testRailApi.addBlockedResultForTestByPlan(planName, rsimConfig, deviceName, output, testName);
		}
	}

	/**
	 * Get any object (usual from excel object) check if its numeric,</br>
	 * if it is return the whole number value as string </br>
	 * (if its a double value - it will convert from double to string)
	 * 
	 * @param Object
	 * @return String value of int from object if its numeric else just return
	 *         the string value
	 */
	public static String getIntegerAsString(Object object) {
		String returnValue = String.valueOf(object);
		if (NumberUtils.isNumber(returnValue))
			returnValue = String.valueOf(Double.valueOf(returnValue).intValue());
		return returnValue == "null" || returnValue == null ? null : returnValue;
	}

	/**
	 * 
	 * Get any object (usual from excel object) check if its numeric,</br>
	 * if it is return the double number value as string </br>
	 * (if its an integer value - it will convert it to double and then to
	 * string )
	 * 
	 * @param Object
	 * @return String value of int from object if its numeric else just return
	 *         the string value
	 */
	public static String getDoubleAsString(Object object) {
		String returnValue = String.valueOf(object);
		if (NumberUtils.isNumber(returnValue))
			returnValue = String.valueOf(Double.valueOf(returnValue));
		return returnValue == "null" || returnValue == null ? null : returnValue;
	}

	/**
	 * get from excel object the ExcelType
	 * 
	 * @param object
	 * @return ExcelType value
	 */
	public static ExcelType getExcelType(Object object) {
		// Type
		try {
			return ExcelType.valueOf(String.valueOf(object));
		} catch (ClassCastException e) {
			throw new RuntimeException("the type is empty or not from excel types");
		}
	}

	/**
	 * get from any object (usual from excel object) any object and return it as
	 * string or null
	 */
	public static String getString(Object object) {
		return String.valueOf(object) == "null" || String.valueOf(object) == null ? null : String.valueOf(object);
	}

	/**
	 * get from any object (usual from excel object ) any object and return it
	 * as boolean if its not equals true
	 */
	public static Boolean getBoolean(Object object) {
		return Boolean.valueOf(String.valueOf(object));
	}

	public static String getTestMethodName() {
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		ITestNGMethod iTestNGMethod = iTestResult.getMethod();
		return iTestNGMethod.getMethodName();
	}

	/**
	 * update test rail with test name from the xml file
	 * 
	 * @param testName
	 * @param output
	 * @return true is success
	 */
	public static boolean updateTestRailFromTestName(String testName, TestOutput output) {
		TestRailAPI testRailApi = TestRailAPI.getInstance();

		JSONObject runObject = testRailApi.getRunBySuiteId(testRailApi.getSuiteId());

		Long runId = Long.valueOf(runObject.get("id").toString());
		String testRailTestName = testRailApi.getFromXSLMethodNameTheTestTitle(testName, runId);

		if (testRailTestName != null) {
			testRailApi.addResultForTestByRun(runId, output, testRailTestName);
			return true;
		}
		return false;
	}

	/**
	 * generic update for tests plan and run
	 * 
	 * @param testOutput
	 */
	public static void updateTestRail(String testName, ITestResult iTestResult, String output) {
		TestRailAPI testRailAPI = TestRailAPI.getInstance();
		testRailAPI.updateTestRail(testName, iTestResult, output);
	}

	/**
	 * generic update for tests plan and run
	 * 
	 * @param testOutput
	 */
	public static void updateTestRail(TestOutput testOutput) {
		TestRailAPI testRailAPI = TestRailAPI.getInstance();
		testRailAPI.updateTestRail(testOutput);
	}

	public static void updateTestRailForApp(String phoneType, ITestResult iTestResult, String output) {
		TestRailAPI testRailApi = TestRailAPI.getInstance();

		testRailApi.setRun(testRailApi.getRunForAppByPhoneType(phoneType));

		testRailApi.add_result_ByPlugPlan(output, iTestResult);

	}

	/**
	 * new implementation of updating testrail plug/vSim tests, add result to
	 * specific phone and RSIM combination of configuration
	 * 
	 * @param iTestResult
	 *            including the throwable if exist, and the actual assertion
	 *            result.
	 * @param output
	 *            will insert as comment in the result
	 * @param phone
	 * @param rsimConfigName
	 */
	public static void updateTestRailForPlugTest(ITestResult iTestResult, String output, PhoneType phone,
			String rsimConfigName) {
		TestRailAPI testRailApi = TestRailAPI.getInstance();

		testRailApi.setRun(testRailApi.getRunByConfigName(phone.getTestData().getDeviceName(), rsimConfigName));

		testRailApi.add_result_ByPlugPlan(output, iTestResult);

	}
	
	/**
	 * Using reflection getting the output value (use for injecting comment to TestRail)
	 * @param iTestResult
	 * @return String output or null
	 */
	public static String getOutputFromClass(ITestResult iTestResult) {
		// get current test object
		Object testObject = iTestResult.getInstance();
		// get the necessary fields by reflection
		// public String output, public PhoneType phone, public String rsim;
		String output = null;

		try {
			output = (String) testObject.getClass().getDeclaredField("output").get(testObject);
		} catch (Exception e) {
			System.err.println("Cannot get output from Class " 
					+ iTestResult.getMethod().getTestClass().getName()
					+ ":\n" + e.getMessage());
		}
		return output;
	}
	
	/**
	 * Wrapping the Thread.sleep method to avoid try/catch and to simplify it to
	 * seconds
	 * 
	 * @param seconds
	 */
	public static void sleep(int seconds) {
		sleep(seconds, 0);
	}

	/**
	 * Wrapping the Thread.sleep method to avoid try/catch and to simplify it to
	 * seconds and milliseconds
	 * 
	 * @param seconds
	 * @param millis
	 */
	public static void sleep(int seconds, int millis) {
		try {
			Thread.sleep((seconds * 1000) + millis);
		} catch (InterruptedException e) {
		}
	}
}