package testrail.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

import beans.Config;
import global.PropertiesUtil;
import testing_utils.TestOutput;

/**
 * this class parse the testRail result api. </br> using org.json.simple and the
 * sendGet/sendPost of testrail APIClient. </br> the user only should put the
 * test result inside @Test </br>and this class will update all test in testRail
 * . </br> </br> Be Aware the method in the test <b>Must</b> be with the same
 * name of the testrail test case (no need for spaces but should be equals
 * (ignore cases)), otherwise testrail will update
 * 
 * @author Yehuda Ginsburg
 */

public class TestRailAPI {

	private static final String CLOUD = "CLOUD";
	private static final String PLUG = "PLUG";
	private APIClient client;
	private JSONObject plan;
	private JSONObject run;
	public boolean isPlan;
	private boolean isRun;
	
	private List<String> titles;
	private List<Config> configs;
	private Map<String, Long> testMap;
	private Map<String, TestOutput> resultMap;

	private Long assignedto_id;
	private Long suiteId;
	private Long runId;
	private boolean isAndroid;
	private boolean isIos;

	// Authorization
	private static String password;
	private static String user;

	// Test Status
	public static final Long PASSED = 1l;
	public static final Long BLOCKED = 2l;
	public static final Long UNTESTED = 3l;
	public static final Long RETEST = 4l;
	public static final Long FAILED = 5l;
	public static final Long SKIPPED = 6l;
	
	// Users
	public static final Long OR_SHACHAR = 1l;
	public static final Long YEHUDA_GINSBURG = 4l;
	public static final Long GIMSO_REPORTS = 5l;
	public static final Long DEFAULT_ASSIGNED_TO_USER = GIMSO_REPORTS;

	// Keys
	private static final Long DEFAULT_PROJECT_ID = 6l;
	private static final String SUITE_ID_KEY = "suite_id";
	private static final String INCLUDE_ALL_KEY = "include_all";
	private static final String CASE_IDS_KEY = "case_ids";
	private static final String CONFIG_IDS_KEY = "config_ids";
	private static final String RUNS_KEY = "runs";
	
	//Application suite properties.json keys
	private static final String TEST_RAIL_ANDROID_SUITE = "TEST_RAIL_ANDROID_SUITE";
	private static final String TEST_RAIL_IOS_SUITE = "TEST_RAIL_IOS_SUITE";
	
	//Configuration id's
	private static final long IOS_APP_GROUP_ID = 5l;
	private static final long ANDROID_APP_GROUP_ID = 6l;
	private static final long RSIM_GROUP_ID = 3l;
	private static final long PHONES_GROUP_ID = 1l;
	
	/**
	 * private CTOR to make it Singleton, initialized in the @BeforeSuite and
	 * use the same instance for all the test
	 * 
	 * @param user
	 * @param password
	 */
	private TestRailAPI(String user, String password) {
	
		this.resultMap = new HashMap<String, TestOutput>();
		this.testMap = new HashMap<String, Long>();
		this.titles = new ArrayList<String>();
		this.client = new APIClient(user, password);
		this.configs = initialConfigs();
		
	}


	private static class LazyHolder {
		private static final TestRailAPI INSTANCE = new TestRailAPI(user, password);
	}

	/**
	 * First initialization must insert user name and password for each user
	 * name and password one instance, be aware in one test
	 * 
	 * @param user
	 * @param password
	 * @return TestRailResult instance
	 */
	public static TestRailAPI getInstance(String user, String password) {
		TestRailAPI.user = user;
		TestRailAPI.password = password;
		return LazyHolder.INSTANCE;
	}

	/**
	 * after first initialization get the instance
	 * 
	 * @return TestRailResult instance
	 */
	public static TestRailAPI getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * get run id by name, needs get_runs() method
	 * 
	 * @param name
	 * @return
	 */
	private Long getRunIdByName(String name) {
		JSONArray runsArray = get_runs();
		for (int i = 0; i < runsArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) runsArray.get(i);
			if (jsonObject.get("name").toString().equalsIgnoreCase(name)) {
				return Long.valueOf(jsonObject.get("id").toString());
			}
		}
		return null;
	}
	
	/**
	 * get all runs with default project -(6) needs get_runs(int project_id)
	 * method
	 * 
	 * @return
	 */
	public JSONArray get_runs() {
		return get_runs(DEFAULT_PROJECT_ID);
	}

	/**
	 * get all runs by project id
	 * 
	 * @param project_id
	 * @return
	 */
	private JSONArray get_runs(Long project_id) {
		try {
			JSONArray array = (JSONArray) client.sendGet("get_runs/" + project_id);
			return array;
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get json array of tests return a list of test_titles initializing also
	 * the testMap with title as key and id as value
	 * 
	 * @param runID
	 * @return
	 */
	private List<String> getAllTestTitle(Long runID) {
		JSONArray jsonArray = get_tests(runID);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String method_name = String.valueOf(jsonObject.get("custom_method_name"));
			String title = (method_name != null && !method_name.isEmpty() && !method_name.equals("null")) 
					? method_name
					: jsonObject.get("title").toString();
			Long id = Long.valueOf(jsonObject.get("id").toString());
			titles.add(title);
			testMap.put(title, id);
		}
		return titles;
	}
	
	/**
	 * get specific test json by test_id
	 * 
	 * @param test_id
	 * @return
	 */
	public JSONObject get_test(Long test_id) {
		try {
			return (JSONObject) client.sendGet("get_test/" + test_id);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * get all test from test rail by run_id and return them as json array
	 * 
	 * @param runId
	 * @return
	 */
	private JSONArray get_tests(Long runId) {
		try {
			return (JSONArray) client.sendGet("get_tests/" + runId);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * update from the test the result the result will be stored in the.</br>
	 * resultMap and in the and of the trip will update TestRail with this
	 * map.</br></br>
	 * 
	 * @param output
	 */
	public void updateTestResultAssertTrue(TestOutput output) {
		String title = getFromMethodNameTheTestTitle(getMethodName());
		if (title != null) {
			resultMap.put(title, output);
		}
	}

	/**
	 * when assert is AsserFalse , if result is true the test failed...</br> so
	 * in testRail must be update the result according to that.</br> e.g. if
	 * assert false and the result false - </br> in test rail the result must be
	 * true because the test is pass.</br>
	 * 
	 * @param output
	 */
	public void updateTestResultAssertFalse(TestOutput output) {
		System.out.println("Original output " + output);
		boolean result = !(output.getResult());
		output.setResult(result);
		System.out.println("After Changing output " + output);
		String title = getFromMethodNameTheTestTitle(getMethodName());
		
		if (title!=null) {
			System.out.println("AssertFalse\ttitle = " + title + " output="
					+ output);
			resultMap.put(title, output);
		}
	}

	/**
	 * @return the method name of a @Test
	 */
	public String getMethodName() {
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		ITestNGMethod iTestNGMethod = iTestResult.getMethod();		
		return iTestNGMethod.getMethodName();
	}
	/**
	 * @return the method name of a @Test from iTestResult
	 */
	public String getMethodName(ITestResult iTestResult) {
		ITestNGMethod iTestNGMethod = iTestResult.getMethod();		
		return iTestNGMethod.getMethodName();
	}

	/**
	 * @param methodName
	 *            from the @Test
	 * @return the matching test_title
	 */
	public String getFromMethodNameTheTestTitle(String methodName) {

		for (String title : titles) {
			String temp = title.replaceAll("\\s", "");
			if (methodName.equalsIgnoreCase(temp)) {
				return title;
			}
		}
		System.err.println("Method Name " + methodName + " does not exist in TestRail");
		return null;
	}

	/**
	 * @param methodName
	 *            from the @Test
	 * @return the matching test_title
	 */
	public String getFromMethodNameTheTestTitle(String methodName, Long runId) {
		this.titles = getAllTestTitle(runId);
		for (String title : titles) {
			if (title.equals(methodName))
				return title;
			String temp = title.replaceAll("\\s", "");
			if (methodName.equalsIgnoreCase(temp))
				return title;

		}
		System.err.println("Method Name " + methodName + " does not exist in TestRail");
		return null;
	}


	/**
	 * getting from the result map (that updated during the tests) the result
	 * and update test rail
	 */
	public void updateTestRailByRun() {
		for (Map.Entry<String, TestOutput> entry : resultMap.entrySet()) {
			if (entry.getKey() != null) {

				String testTitle = entry.getKey();
				Long status = getStatus(entry.getValue().getResult());
				String outputAsComment = entry.getValue().getOutput();

				System.out.println(testTitle + " is: \n"
						+ entry.getValue().getResult() + " \n\toutput is : "
						+ outputAsComment + "\n\t\tstatus is: " + status);

				JSONObject jsonObject = add_result_ForTest(testTitle, status,
						outputAsComment, assignedto_id);
				System.out.println(jsonObject);
			}
		}
	}
	
	/**
	 * getting from the result map (that updated during the tests) the result
	 * and update test rail
	 */
	public void updateTestRailByPlan() {
		for (Map.Entry<String, TestOutput> entry : resultMap.entrySet()) {
			if (entry.getKey() != null) {

				String testTitle = entry.getKey();
				Long status = getStatus(entry.getValue().getResult());
				String outputAsComment = entry.getValue().getOutput();

				JSONObject jsonObject = add_result_ForTest(testTitle, status, outputAsComment, assignedto_id);
				System.out.println(jsonObject);
			}
		}
	}

	/**
	 * status_id int The ID of the test status. </br> The built-in system
	 * statuses have the following IDs: </br> 1 Passed </br> 2 Blocked </br> 3
	 * Untested (not allowed when adding a result) </br> 4 Retest </br> 5 Failed
	 * </br>
	 *
	 * @param runName
	 * @param testTitle
	 * @param status_id
	 * @param comment
	 * @param assignedto_id
	 * @return
	 */
	private JSONObject add_result_ForTest(
			String testTitle, Long status_id,
			String comment, Long assignedto_id) {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("status_id", status_id);
			data.put("assignedto_id", assignedto_id);
			data.put("comment", comment);
			JSONObject jsonResult = (JSONObject) client.sendPost("add_result/"
					+ testMap.get(testTitle), data);
			return jsonResult;
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * @param result
	 *            of TestOutput
	 * @return the test rail status
	 */
	private Long getStatus(Boolean result) {
		if (result == false) {
			return TestRailAPI.FAILED;
		} else
			return TestRailAPI.PASSED;
	}
	
	/**
	 * update test rail by testng ITestResult 
	 * @param result
	 * @return
	 */
	private Long getStatus(ITestResult result) {
		
		switch (result.getStatus()) {
		
		case ITestResult.FAILURE:
			return TestRailAPI.FAILED;
		
		case ITestResult.SKIP:
			return TestRailAPI.SKIPPED;
		
		case ITestResult.SUCCESS:
			return TestRailAPI.PASSED;
		}
		return RETEST;

	}

	/**
	 * @param assignedTo
	 * @param runName
	 * @param suite_id
	 */
	public void addNewRun(Long assignedTo, String runName, Long suite_id) {
		Map<String, Object> map = this.addRunMapData(suite_id, runName,
				null, null, assignedTo, true, null);
		this.add_run(map);
		this.runId = getRunIdByName(runName);
		this.titles = getAllTestTitle(this.runId);
		this.assignedto_id = assignedTo;
	}

	
	//-------------------//
	//--------RUN--------//
	//-------------------//
	
	/**
	 * Extract the ID of specific Run from Plan by Run's name 
	 * @param plansArray
	 * @param name
	 * @return The Id as String
	 */
	private  JSONObject getRunByPlansArrayAndName(JSONArray plansArray, String name) {
		for (int j = 0; j < plansArray.size(); j++) {
			JSONObject temp = (JSONObject) plansArray.get(j);
			if (temp.get("name").toString().equalsIgnoreCase(name)) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param mapData
	 * @return Run's JSONObject
	 */
	private JSONObject add_run(Map<String, Object> mapData) {
		try {
			JSONObject add_runResponse_content = (JSONObject) client.sendPost("add_run/" + DEFAULT_PROJECT_ID, mapData);
			return add_runResponse_content;
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <b>suite_id int</b> The ID of the test suite for the test run,<br/>
	 * (optional if the project is operating in single suite mode, required
	 * otherwise)<br/>
	 * <b> name string </b>The name of the test run description string The
	 * description of the test run.</br>
	 * <b> milestone_id int </b>The ID of the milestone to link to the test
	 * run.</br>
	 * <b> assignedto_id int</b> The ID of the user the test run should be
	 * assigned to.</br>
	 * <b> include_all boolean</b> True for including all test cases of the test
	 * suite and false for a custom case selection (default: true).<br/>
	 * <b> case_ids array</b> An array of case IDs for the custom case
	 * selection.
	 * 
	 * @param suite_id
	 * @param name
	 * @param description
	 * @param milestone_id
	 * @param assignedto_id
	 * @param include_all
	 * @param case_ids
	 * @return a map <Key-String, Value-Object>
	 */
	public Map<String, Object> addRunMapData(Long suite_id, String name, String description, Long milestone_id,
			Long assignedto_id, Boolean include_all, Long[] case_ids) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (suite_id != null) {
			map.put("suite_id", suite_id);
		}
		if (milestone_id != null) {
			map.put("milestone_id", milestone_id);
		}

		if (assignedto_id != null) {
			map.put("assignedto_id", assignedto_id);
		}
		if (case_ids != null) {
			map.put("case_ids", case_ids);
		}
		map.put("name", name);
		map.put("description", description);
		map.put("include_all", include_all);
		return map;
	}

	/**
	 * check if there is a run that has the phone and the RSIM configuration
	 * from parameters
	 * 
	 * @param planName
	 *            to get JSONArray of Runs inside this Plan
	 * @param phoneConfig
	 * @param rsimConfig
	 * @return JSONObject of run if find any
	 */
	public JSONObject getRunByConfigName(String planName, String phoneConfig, String rsimConfig) {
		JSONObject runObj = null;
		JSONArray runs = getRunsByPlanName(planName);
		for (int i = 0; i < runs.size(); i++) {
			JSONObject temp = (JSONObject) runs.get(i);
			boolean containsRsim = false;
			boolean containsPhone = false;
			String config[] = temp.get("config").toString().split(", ");
			for (String s : config) {
				if (s.equalsIgnoreCase(rsimConfig)) {
					containsRsim = true;
				}
				if (s.equalsIgnoreCase(phoneConfig)) {
					containsPhone = true;
				}
			}
			if (containsPhone && containsRsim)
				runObj = temp;
		}
		return runObj;
	}

	public JSONObject getRunByConfigName(String phoneConfig, String rsimConfig) {
		JSONObject runObj = null;
		JSONArray runs = getRunsByPlan();
		for (int i = 0; i < runs.size(); i++) {
			JSONObject run = (JSONObject) runs.get(i);
			boolean containsRsim = false;
			boolean containsPhone = false;
			String config[] = run.get("config").toString().split(", ");
			for (String s : config) {
				if (s.equalsIgnoreCase(rsimConfig)) {
					containsRsim = true;
				}
				if (s.equalsIgnoreCase(phoneConfig)) {
					containsPhone = true;
				}
			}
			if (containsPhone && containsRsim)
				runObj = run;
		}
		return runObj;
	}
	
	
	public JSONObject getRunForAppByPhoneType(String phoneType) {
		JSONObject rv = null;
		JSONArray runs = getRunsByPlan();
		outer: 		
		for (int i = 0; i < runs.size(); i++) {
			JSONObject temp = (JSONObject) runs.get(i);
			if (String.valueOf(temp.get("config")).equals(phoneType)) {
				rv = temp;
				break outer;
			}
		}
		return rv;
	}
	
	
	/**
	 * get all runs and extract them from json array, if name equals the name
	 * requested return the whole run of this name.
	 * 
	 * @param name
	 * @return Run's JSONObject
	 */
	public JSONObject getRunByName(String name) {
		JSONArray runsArray = get_runs();
		for (int i = 0; i < runsArray.size(); i++) {
			JSONObject temp = (JSONObject) runsArray.get(i);
			if (temp.get("name").toString().equalsIgnoreCase(name)) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param planName
	 * @param phoneConfig
	 * @param rsimConfig
	 * @return The run ID as Long
	 */
	public Long getRunIdByPlanAndConfig(String planName, String phoneConfig, String rsimConfig) {
		return (Long) getRunByConfigName(planName, phoneConfig, rsimConfig).get("id");
	}

	/**
	 * 
	 * @param planName
	 * @return JSONArray of all Run's in Plan
	 */
	public JSONArray getRunsByPlanName(String planName) {
		JSONObject planObj = getPlanByName(planName);
		JSONArray entries = getEntriesFromPlan(planObj);
		JSONArray runs = null;
		for (int i = 0; i < entries.size(); i++) {
			JSONObject temp = (JSONObject) entries.get(i);
			if (temp.containsKey("runs"))
				runs = (JSONArray) temp.get("runs");
		}
		return runs;
	}

	/**
	 * get run by plan object that already initialized
	 * 
	 * @return JSONArray
	 */
	public JSONArray getRunsByPlan() {
		JSONArray entries = getEntriesFromPlan(this.plan);
		JSONArray runs = null;
		for (int i = 0; i < entries.size(); i++) {
			JSONObject temp = (JSONObject) entries.get(i);
			if (temp.containsKey("runs"))
				runs = (JSONArray) temp.get("runs");
		}
		return runs;
	}

	// --------------------//
	// --------PLAN--------//
	// --------------------//

	/**
	 * <b>POST index.php?/api/v2/add_plan/:project_id</br>
	 * :project_id The ID of the project the test plan should be added to </b>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col style="width: 100px"
	 * > <col> </colgroup> <tbody>
	 * <tr class="row0">
	 * <th class="col0 leftalign">Name</th>
	 * <th class="col1 leftalign">Type</th>
	 * <th class="col2 leftalign">Description</th>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">name</td>
	 * <td class="col1 leftalign">string</td>
	 * <td class="col2">The name of the test plan (required)</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">description</td>
	 * <td class="col1 leftalign">string</td>
	 * <td class="col2">The description of the test plan</td>
	 * </tr>
	 * <tr class="row3">
	 * <td class="col0 leftalign">milestone_id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the milestone to link to the test plan</td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign" style="vertical-align: top">entries</td>
	 * <td class="col1 leftalign" style="vertical-align: top">array</td>
	 * <td class="col2">An array of objects describing the test runs of the
	 * plan, see the example below and
	 * <a href="#add_plan_entry">add_plan_entry</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <pre class="code">
	 * {
	 * 	"name": "System test",
	 * 	"entries": [
	 * 		{
	 * 			"suite_id": 1,
	 * 			"include_all": true,
	 * 			"config_ids": [1, 2, 4, 5, 6],
	 * 			"runs": [
	 * 				{
	 * 					"include_all": false,
	 * 					"case_ids": [1, 2, 3],
	 * 					"assignedto_id": 1,
	 * 					"config_ids": [2, 5]
	 * 				},
	 * 				{
	 * 					"include_all": false,
	 * 					"case_ids": [1, 2, 3, 5, 8],
	 * 					"assignedto_id": 2,
	 * 					"config_ids": [2, 6]
	 * 				}
	 * 
	 * 				..
	 * 			]
	 * 		},
	 * 
	 * 		..
	 * 	]
	 * }
	 * </pre>
	 * 
	 * @param projectId
	 * @param name
	 * @param entries
	 */

	public JSONObject addPlan(String name, Long suiteId) {
		return addPlan(name, suiteId, null);
	}

	public JSONObject addPlan(String name, Long suiteId, String configName) {
		Map<String, Object> planMap = new HashMap<String, Object>();

		JSONArray jsonArrayEntries = new JSONArray();
		JSONArray jsonArrayRuns = new JSONArray();

		List<JSONObject> jsonObjectsEntries = new ArrayList<JSONObject>();
		List<JSONObject> jsonObjectsRuns = new ArrayList<>();
		
		if (configName.equals(CLOUD))
			jsonObjectsRuns = null;
		else if (configName.equals(PLUG))
			jsonObjectsRuns = initialRunsForPlugPlan();
		else
			jsonObjectsRuns = initialRunsForAppPlan(configName);

		JSONObject jsonObject_Entry_1 = new JSONObject();

		if (jsonObjectsRuns!=null) 
			jsonArrayRuns.addAll(jsonObjectsRuns);

		jsonObject_Entry_1.put("suite_id", suiteId);
		jsonObject_Entry_1.put("runs", jsonArrayRuns);
		jsonObject_Entry_1.put("include_all", true);

		JSONArray allConfigIdsByGroupIds = null;

		if (configName.equals(CLOUD)) 
			{/* do nothing add no configuration */} 
		else if (configName.equals(PLUG))
			allConfigIdsByGroupIds = getAllConfigIdsByGroupIds(PHONES_GROUP_ID, RSIM_GROUP_ID);
		else if (isAndroid)
			allConfigIdsByGroupIds = getAllConfigIdsByGroupIds(ANDROID_APP_GROUP_ID);
		else if (isIos)
			allConfigIdsByGroupIds = getAllConfigIdsByGroupIds(IOS_APP_GROUP_ID);

		jsonObject_Entry_1.put("config_ids", allConfigIdsByGroupIds);

		jsonObjectsEntries.add(jsonObject_Entry_1);
		jsonArrayEntries.addAll(jsonObjectsEntries);

		planMap.put("name", name);
		planMap.put("entries", jsonArrayEntries);

		try {
			return (JSONObject) client.sendPost("add_plan/" + DEFAULT_PROJECT_ID, planMap);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Create JSONArray of configs by one or more group ids
	 * @param groupIds
	 * @return JSONArray
	 */
	private JSONArray getAllConfigIdsByGroupIds(Long... groupIds) {
		List<Long> ids = new ArrayList<Long>();
		for (Long groupId : groupIds) {
		for (Config config : this.configs) {
				if (groupId == config.getGroupId())
					ids.add(config.getId());
			}
		}
		JSONArray rv = new JSONArray();
		rv.addAll(ids);
		return rv;
	}
	
	/**
	 * Initial Runs For Application Plans by OS (ANDROID/IOS)
	 * 
	 * @param os
	 * @return List<JSONObject>
	 */
	private List<JSONObject> initialRunsForAppPlan(String os) {

		List<JSONObject> jsonObjectsRuns = new ArrayList<JSONObject>();
		List<Long> configList = new ArrayList<Long>();
		
		if (os.toUpperCase().contains("ANDROID")) {
			isAndroid = true;
			isIos = false;
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_ANDROID_SUITE));
		} else if (os.toUpperCase().contains("IOS")) {
			isAndroid = false;
			isIos = true;
			suiteId = Long.valueOf(PropertiesUtil.getInstance().getProperty(TEST_RAIL_IOS_SUITE));
		} else
			return null;

		if (isAndroid)
			for (int i = 0; i < getAllConfigIdsByGroupIds(ANDROID_APP_GROUP_ID).size(); i++)
				configList.add((Long) getAllConfigIdsByGroupIds(ANDROID_APP_GROUP_ID).get(i));
		else if (isIos)
			for (int i = 0; i < getAllConfigIdsByGroupIds(IOS_APP_GROUP_ID).size(); i++)
				configList.add((Long) getAllConfigIdsByGroupIds(IOS_APP_GROUP_ID).get(i));
		else
			return null;
			
		for (Long config : configList) {
			JSONArray configs = new JSONArray();
			configs.add(config);
			JSONObject run = new JSONObject();
			run.put("config_ids", configs);
			jsonObjectsRuns.add(run);
		}
		
		return jsonObjectsRuns;
	}
	
	/**
	 * The hierarchy of configuration is like this Header with array of Children
	 * </br> (e.g. RSIM->SIM or PHONE-> IPHONE5s) </br> 
	 * A. Get Configurations as
	 * JSONArray.</br> 
	 * B. Iterate over it and extract JSONObjects of PHONE and
	 * RSIM configurations headers.</br> 
	 * C. Extract the JSONArray of PHONE/RSIM
	 * configurations from JSONObjects.</br> 
	 * D. Get the Id's of each
	 * configuration add it to List (Long)</br> 
	 * E. Insert for each RSIM the all PHONE's 
	 * 
	 * @return List of all created Runs  as JSONObjects
	 */
	
private List<JSONObject> initialRunsForPlugPlan() {
				
		List<JSONObject> runs = new ArrayList<JSONObject>();
		List<Config> rsimConfigList = new ArrayList<>();
		List<Config> phonesConfigList = new ArrayList<>();
		for(Config config : this.configs){
			if(config.getGroupName().equals("Phones")){
				phonesConfigList.add(config);
			}else if (config.getGroupName().equals("RSIM")){
				rsimConfigList.add(config);
			}
		}

		for (Config rsim : rsimConfigList) {
			Long rsimId = rsim.getId();
			for (Config phone : phonesConfigList) {
				Long phoneId = phone.getId();
				JSONArray runConfigs = new JSONArray();
				JSONObject run = new JSONObject();
				runConfigs.add(rsimId);
				runConfigs.add(phoneId);
				run.put("config_ids", runConfigs);
				runs.add(run);
			}
		}
		return runs;
	}

	

	/**
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col style="width: 100px"
	 * > <col> </colgroup> <tbody>
	 * <tr class="row0">
	 * <th class="col0 leftalign">Name</th>
	 * <th class="col1 leftalign">Type</th>
	 * <th class="col2 leftalign">Description</th>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">suite_id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the test suite for the test run(s) (required)
	 * </td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">name</td>
	 * <td class="col1 leftalign">string</td>
	 * <td class="col2">The name of the test run (s)</td>
	 * </tr>
	 * <tr class="row3">
	 * <td class="col0 leftalign">assignedto_id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the user the test run(s) should be assigned to
	 * </td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign">include_all</td>
	 * <td class="col1 leftalign">bool</td>
	 * <td class="col2">True for including all test cases of the test suite and
	 * false for a custom case selection (default: true)</td>
	 * </tr>
	 * <tr class="row5">
	 * <td class="col0 leftalign">case_ids</td>
	 * <td class="col1 leftalign">array</td>
	 * <td class="col2">An array of case IDs for the custom case selection</td>
	 * </tr>
	 * <tr class="row6">
	 * <td class="col0 leftalign" style="vertical-align: top">config_ids</td>
	 * <td class="col1 leftalign" style="vertical-align: top">array</td>
	 * <td class="col2">An array of configuration IDs used for the test runs of
	 * the test plan entry (requires <strong>TestRail 3.1</strong> or later)</td>
	 * </tr>
	 * <tr class="row7">
	 * <td class="col0 leftalign" style="vertical-align: top">runs</td>
	 * <td class="col1 leftalign" style="vertical-align: top">array</td>
	 * <td class="col2">An array of test runs with configurations, please see
	 * the example below for details (requires <strong>TestRail 3.1</strong> or
	 * later)</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * Also see the following example which shows how to create a new test plan
	 * entry with multiple test runs and configurations:
	 * 
	 * 
	 * <pre class="code">
	 * {
	 * 	"suite_id": 1,
	 * 	"assignedto_id": 1,           // Default assignee
	 * 	"include_all": true,          // Default selection
	 * 	"config_ids": [1, 2, 4, 5, 6],
	 * 	"runs": [
	 * 		{
	 * 			"include_all": false, // Override selection
	 * 			"case_ids": [1, 2, 3],
	 * 			"config_ids": [2, 5]
	 * 		},
	 * 		{
	 * 			"include_all": false, // Override selection
	 * 			"case_ids": [1, 2, 3, 5, 8],
	 * 			"assignedto_id": 2,   // Override assignee
	 * 			"config_ids": [2, 6]
	 * 		}
	 * 
	 * 		..
	 * 	]
	 * }
	 * </pre>
	 * 
	 * @param planId
	 * @param suiteId
	 * @param name
	 * @param configIds
	 * @param runs
	 */
	public JSONObject addPlanEntry(String planId, String suiteId, String name,
			JSONArray caseIds, JSONArray configIds, JSONArray runs) {

		boolean includeAll = true;
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(SUITE_ID_KEY, suiteId);// 80
		// map.put(NAME_KEY, name);//
		map.put(INCLUDE_ALL_KEY, includeAll);
		map.put(CASE_IDS_KEY, caseIds);
		map.put(CONFIG_IDS_KEY, configIds);
		map.put(RUNS_KEY, runs);
		try {
			return (JSONObject) client
					.sendPost("add_plan_entry/" + planId, map);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Search in plans array (getPlans()) of specific suite, if plan with this
	 * name already exist return the JSONObject of this plan if not exist add
	 * new plan with the planName parameter
	 * 
	 * @param planName
	 * @param suiteId
	 * @return Plan JSONObject
	 */
	public JSONObject addPlanIfNotExist(String planName, Long suiteId, String configName) {
		planName = getPlanOrRunName(planName);
		JSONArray plans = get_plans();
		boolean isExist = false;
		JSONObject plan = null;
		
		outer: 
		for (int j = 0; j < plans.size(); j++) {
			JSONObject tempPlan = (JSONObject) plans.get(j);
			if (tempPlan.get("name").toString().equalsIgnoreCase(planName)) {
				plan = tempPlan;
				isExist = true;
				System.out.println("Plan with '" + planName + "' name, already exists");	
				break outer;
			}
		}
		
		if (isExist == false) {
			plan = addPlan(planName, suiteId, configName);
			System.out.println("New Plan '" + planName + "' was created");
		}
		
		if(plan!=null)
			setPlan(plan);
		
		return plan;
	}

	public JSONObject addRunIfNotExist(String runName, Long suiteId) {
		runName = getPlanOrRunName(runName);
		JSONArray runsArray = get_runs();
		boolean isExist = false;
		for (int j = 0; j < runsArray.size(); j++) {
			JSONObject run = (JSONObject) runsArray.get(j);
			if (run.get("name").toString().equalsIgnoreCase(runName)) {
				isExist = true;
				System.out.println("Run with '" + runName
						+ "' name, already exists");
				return run;
			}
		}
		if (isExist == false) {
			JSONObject addRun = addRun (runName, suiteId);
			System.out.println("New Run '"+runName+"' was created");
			return addRun;
		}
		return null;
	}
	private JSONObject addRun(String runName, Long suite_id) {
		Map<String, Object> map = addRunMapData(
				suite_id, runName, null, null, DEFAULT_ASSIGNED_TO_USER, true, null);
		return add_run(map);		
	}
	private JSONArray getEntriesFromPlan(JSONObject planObj) {
		try {
			return (JSONArray) planObj.get("entries");
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("cannot get entries from plan");
		}
	}

	/**
	 * <div class="level4" style="display: block;">
	 * 
	 * <p>
	 * Please see the following example for a typical response:
	 * </p>
	 * 
	 * <pre class="code">
	 * {
	 * 	"assignedto_id": null,
	 * 	"blocked_count": 2,
	 * 	"completed_on": null,
	 * 	"created_by": 1,
	 * 	"created_on": 1393845644,
	 * 	"custom_status1_count": 0,
	 * 	"custom_status2_count": 0,
	 * 	"custom_status3_count": 0,
	 * 	"custom_status4_count": 0,
	 * 	"custom_status5_count": 0,
	 * 	"custom_status6_count": 0,
	 * 	"custom_status7_count": 0,
	 * 	"description": null,
	 * 	"entries": [
	 * 	{
	 * 		"id": "3933d74b-4282-4c1f-be62-a641ab427063",
	 * 		"name": "File Formats",
	 * 		"runs": [
	 * 		{
	 * 			"assignedto_id": 6,
	 * 			"blocked_count": 0,
	 * 			"completed_on": null,
	 * 			"config": "Firefox, Ubuntu 12",
	 * 			"config_ids": [
	 * 				2,
	 * 				6
	 * 			],
	 * 			"custom_status1_count": 0,
	 * 			"custom_status2_count": 0,
	 * 			"custom_status3_count": 0,
	 * 			"custom_status4_count": 0,
	 * 			"custom_status5_count": 0,
	 * 			"custom_status6_count": 0,
	 * 			"custom_status7_count": 0,
	 * 			"description": null,
	 * 			"entry_id": "3933d74b-4282-4c1f-be62-a641ab427063",
	 * 			"entry_index": 1,
	 * 			"failed_count": 2,
	 * 			"id": 81,
	 * 			"include_all": false,
	 * 			"is_completed": false,
	 * 			"milestone_id": 7,
	 * 			"name": "File Formats",
	 * 			"passed_count": 2,
	 * 			"plan_id": 80,
	 * 			"project_id": 1,
	 * 			"retest_count": 1,
	 * 			"suite_id": 4,
	 * 			"untested_count": 3,
	 * 			"url": "http://&lt;server&gt;/testrail/index.php?/runs/view/81"
	 * 		},
	 * 		{
	 * 			..
	 * 		}
	 * 		],
	 * 		"suite_id": 4
	 * 	}
	 * 	],
	 * 	"failed_count": 2,
	 * 	"id": 80,
	 * 	"is_completed": false,
	 * 	"milestone_id": 7,
	 * 	"name": "System test",
	 * 	"passed_count": 5,
	 * 	"project_id": 1,
	 * 	"retest_count": 1,
	 * 	"untested_count": 6,
	 * 	"url": "http://&lt;server&gt;/testrail/index.php?/plans/view/80"
	 * }
	 * </pre>
	 * 
	 * <p>
	 * The following fields are included in the response:
	 * </p>
	 * 
	 * <p>
	 * 
	 * </p>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col style="width: 100px"> <col>
	 * </colgroup> <tbody>
	 * <tr class="row0">
	 * <th class="col0 leftalign">Name</th>
	 * <th class="col1 leftalign">Type</th>
	 * <th class="col2 leftalign">Description</th>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">assignedto_id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the user the entire test plan is assigned to</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">blocked_count</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The amount of tests in the test plan marked as blocked</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">completed_on</td>
	 * <td class="col1 leftalign">timestamp</td>
	 * <td class="col2">The date/time when the test plan was closed (as UNIX
	 * timestamp)</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">created_by</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the user who created the test plan</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">created_on</td>
	 * <td class="col1 leftalign">timestamp</td>
	 * <td class="col2">The date/time when the test plan was created (as UNIX
	 * timestamp)</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">custom_status?_count</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The amount of tests in the test plan with the respective
	 * custom status</td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign">description</td>
	 * <td class="col1 leftalign">string</td>
	 * <td class="col2">The description of the test plan</td>
	 * </tr>
	 * <tr class="row5">
	 * <td class="col0 leftalign">entries</td>
	 * <td class="col1 leftalign">array</td>
	 * <td class="col2">An array of 'entries', i.e. group of test runs</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">failed_count</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The amount of tests in the test plan marked as failed</td>
	 * </tr>
	 * <tr class="row7">
	 * <td class="col0 leftalign">id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The unique ID of the test plan</td>
	 * </tr>
	 * <tr class="row8">
	 * <td class="col0 leftalign">is_completed</td>
	 * <td class="col1 leftalign">bool</td>
	 * <td class="col2">True if the test plan was closed and false otherwise</td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign">milestone_id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the milestone this test plan beStrings to</td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign">name</td>
	 * <td class="col1 leftalign">string</td>
	 * <td class="col2">The name of the test plan</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">passed_count</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The amount of tests in the test plan marked as passed</td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign">project_id</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The ID of the project this test plan beStrings to</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">retest_count</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The amount of tests in the test plan marked as retest</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">untested_count</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">The amount of tests in the test plan marked as untested</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">url</td>
	 * <td class="col1 leftalign">string</td>
	 * <td class="col2">The address/URL of the test plan in the user interface</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * </p>
	 * 
	 * <p>
	 * The <code>entries</code> field includes an array of test plan
	 * <em>entries</em>. A test plan entry is a group of test runs that beString
	 * to the same test suite (just like in the user interface). Each group can
	 * have a variable amount of test runs and also supports configurations.
	 * Please also see <a href="#add_plan"
	 * title="testrail-api2:reference-plans ↵" class="wikilink1">add_plan</a>
	 * and <a href="#add_plan_entry" title="testrail-api2:reference-plans ↵"
	 * class="wikilink1">add_plan_entry</a>.
	 * </p>
	 * 
	 * </div>
	 * 
	 * @param id
	 * @return
	 */
	public JSONObject get_plan(Long id) {
		try {
			return (JSONObject) client.sendGet("get_plan/" + id);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**get plan by name
	 * @param plans
	 */
	public JSONObject getPlanByName(String planName) {
		JSONArray plansArray = get_plans();
		for (int i = 0; i < plansArray.size(); i++) {
			JSONObject temp = (JSONObject) plansArray.get(i);
			if (temp.get("name").toString().equalsIgnoreCase(planName))
				return get_plan((Long)temp.get("id"));
		}
		return null;
	}

	/**
	 * GET index.php?/api/v2/get_plans/:project_id
	 * 
	 * @return
	 */
	public JSONArray get_plans() {
		try {
			return (JSONArray) client.sendGet("get_plans/" + DEFAULT_PROJECT_ID);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <h2 class="sectionedit3" id="get_plans">get_plans</h2> <div
	 * class="level2">
	 * 
	 * <p>
	 * Returns a list of test plans for a project.
	 * </p>
	 * 
	 * <pre class="code">
	 * GET index.php?/api/v2/get_plans/:project_id
	 * </pre>
	 * 
	 * <p>
	 * 
	 * </p>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col> </colgroup> <tbody>
	 * <tr class="row0">
	 * <td class="col0 leftalign">:project_id</td>
	 * <td class="col1">The ID of the project</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * </p>
	 * 
	 * </div>
	 * 
	 * <h4 id="request_filters"><img class="expand"
	 * src="/_media/testrail-api2/expand.png" style="display: none;"><img
	 * class="collapse" src="/_media/testrail-api2/collapse.png" style="">
	 * Request filters</h4> <div class="level4" style="display: block;">
	 * 
	 * <p>
	 * The following filters can be applied:
	 * <em>(available since TestRail 4.0)</em>
	 * </p>
	 * 
	 * <p>
	 * 
	 * </p>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col style="width: 100px"> <col>
	 * </colgroup> <tbody>
	 * <tr class="row0">
	 * <th class="col0 leftalign">Name</th>
	 * <th class="col1 leftalign">Type</th>
	 * <th class="col2 leftalign">Description</th>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">:created_after</td>
	 * <td class="col1 leftalign">timestamp</td>
	 * <td class="col2">Only return test plans created after this date (as UNIX
	 * timestamp).</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">:created_before</td>
	 * <td class="col1 leftalign">timestamp</td>
	 * <td class="col2">Only return test plans created before this date (as UNIX
	 * timestamp).</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">:created_by</td>
	 * <td class="col1 leftalign">int (list)</td>
	 * <td class="col2">A comma-separated list of creators (user IDs) to filter
	 * by.</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">:is_completed</td>
	 * <td class="col1 leftalign">bool</td>
	 * <td class="col2">1 to return completed test plans only. 0 to return
	 * active test plans only.</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">:limit/:offset</td>
	 * <td class="col1 leftalign">int</td>
	 * <td class="col2">Limit the result to :limit test plans. Use :offset to
	 * skip records.</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">:milestone_id</td>
	 * <td class="col1 leftalign">int (list)</td>
	 * <td class="col2">A comma-separated list of milestone IDs to filter by.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * </p>
	 * 
	 * <pre class="code">
	 * # All active test plans for project with ID 1 and milestone 2 or 3
	 * GET index.php?/api/v2/get_plans/1&amp;is_completed=0&amp;milestone_id=2,3
	 * </pre>
	 * 
	 * </div>
	 * 
	 * 
	 * @return
	 */
	public JSONArray get_plans_CreatedAfterDate(Date createdAfter) {
		try {
			String unixTimeFromDate = createdAfter.getTime() / 1000 + "";
			System.out.println(unixTimeFromDate);
			JSONArray objectArray = (JSONArray) client
					.sendGet("get_plans/" + DEFAULT_PROJECT_ID
							+ "&created_after=" + unixTimeFromDate);
			return objectArray;
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}


	//----------------------//
	//--------RESULT--------//
	//----------------------//

	
	public JSONObject addBlockedResult(Long testId, TestOutput testOutput) {
		JSONObject resultObject = new JSONObject();
		resultObject.put("status_id", TestRailAPI.BLOCKED);
		resultObject.put("comment", testOutput.getOutput());
		try {
			return (JSONObject) client.sendPost("add_result/" + testId,
					resultObject);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject addBlockedResultForTestByPlan(String planName,
			String rsimConfigName, String phoneConfigName,
			TestOutput testOutput, String testName) {
		String combinedConfig = phoneConfigName + ", " + rsimConfigName;

		JSONObject planObject = getPlanByName(planName);
		JSONArray entriesFromPlan = getEntriesByPlan(planObject);
		JSONArray runsFromEntries = null;
		for (int i = 0; i < entriesFromPlan.size(); i++) {
			JSONObject temp = (JSONObject) entriesFromPlan.get(i);
			if (temp.containsKey("runs"))
				runsFromEntries = (JSONArray) temp.get("runs");
		}
		JSONObject runToUpdate = null;
		for (int i = 0; i < runsFromEntries.size(); i++) {
			JSONObject temp = (JSONObject) runsFromEntries.get(i);
			if (temp.get("config").toString().contains(combinedConfig)) {
				runToUpdate = temp;
			}
		}
		JSONObject testObject = get_test_ByRunIdAndTitle((Long) runToUpdate.get("id"),
				testName);
		Long testId = (Long) testObject.get("id");
		JSONObject resultObject = addBlockedResult(testId, testOutput);

		return resultObject;
	}

	/**
	 * add result with status (true/false), and comment (from TestOutput)
	 * @param testId
	 * @param testOutput
	 * @return
	 */
	public JSONObject add_result(Long testId, TestOutput testOutput) {
		JSONObject resultObject = new JSONObject();
		resultObject.put("status_id", getStatus(testOutput.getResult()));
		resultObject.put("comment", testOutput.getOutput());
		try {
			return (JSONObject) client.sendPost("add_result/" + testId,
					resultObject);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * add result with status (true/false), and comment from iTestResult and output)
	 * @param testId
	 * @param testOutput
	 * @return
	 */
	public JSONObject add_result(Long testId, ITestResult iTestResult, String output) {
		JSONObject resultObject = new JSONObject();
		resultObject.put("status_id", getStatus(iTestResult));
		// try to get the error message if exist
		String message = null;
		// wrap it with try and catch (ignore nullPointerException if not exist
		try {
			message = iTestResult.getThrowable().getMessage();
		} catch (Exception e1) {}
		//if there is a failure use the error message
		if (message!=null) {
			resultObject.put("comment", message+"\n"+output);
		}else{//use the default output from the screen
			resultObject.put("comment", output);
		}
		try {
			return (JSONObject) client.sendPost("add_result/" + testId,
					resultObject);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * status_id int The ID of the test status. </br> The built-in system
	 * statuses have the following IDs: </br> 1 Passed </br> 2 Blocked </br> 3
	 * Untested (not allowed when adding a result) </br> 4 Retest </br> 5 Failed
	 * </br>
	 * 
	 * @param runName
	 * @param testTitle
	 * @param status_id
	 * @param comment
	 * @param assignedto_id
	 * @return
	 */
	public JSONObject add_result_Informative(String runName, String testTitle,
			Long status_id, String comment, Long assignedto_id, JSONArray array_result) {
		try {
			Map<String, Object> data = new HashMap<String, Object>();

			data.put("status_id", status_id);
			data.put("assignedto_id", assignedto_id);
			data.put("comment", comment);
			data.put("custom_steps_separated", array_result);
			
			JSONObject json = get_test_ByRunIdAndTitle(runId, testTitle);
			JSONObject jsonResult = (JSONObject) client.sendPost("add_result/"
					+ json.get("id"), data);
			return jsonResult;
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * Add result for plug plan by plan,rsimCofig,phoneConfig,testName and TestOuput
	 * @param planName
	 * @param rsimConfigName
	 * @param phoneConfigName
	 * @param testOutput
	 * @param testName
	 * @return
	 */
	public JSONObject add_result_ByPlugPlan(String planName,
			String rsimConfigName, String phoneConfigName,
			TestOutput testOutput, String testName) {

		String combinedConfig = phoneConfigName + ", " + rsimConfigName;

		JSONObject planObject = getPlanByName(planName);
		JSONArray entriesFromPlan = getEntriesByPlan(planObject);
		JSONArray runsFromEntries = null;
		for (int i = 0; i < entriesFromPlan.size(); i++) {
			JSONObject temp = (JSONObject) entriesFromPlan.get(i);
			if (temp.containsKey("runs"))
				runsFromEntries = (JSONArray) temp.get("runs");
		}
		JSONObject runToUpdate = null;
		for (int i = 0; i < runsFromEntries.size(); i++) {
			JSONObject temp = (JSONObject) runsFromEntries.get(i);
			if (temp.get("config").toString().contains(combinedConfig)) {
				runToUpdate = temp;
			}
		}
		JSONObject testObject = get_test_ByRunIdAndTitle((Long) runToUpdate.get("id"),
				testName);
		Long testId = (Long) testObject.get("id");
		JSONObject resultObject = add_result(testId, testOutput);
	
		return resultObject;
	}
	
	/**
	 * new implementation of add result for plug plan
	 * @param output
	 * @param iTestResult
	 * @return result as JSONObject
	 */
	public JSONObject add_result_ByPlugPlan(String output,  ITestResult iTestResult) {
		
		String testName = getFromMethodNameTheTestTitle(getMethodName(), runId);
		
		JSONObject resultObject = null;
		
		if (testName!=null) {
			JSONObject testObject = get_test_ByRunIdAndTitle(runId, testName);
			resultObject = add_result((Long) testObject.get("id"), iTestResult, output);
		}

		return resultObject;
	}
	
	/**
	 * Add result for test by run TestOutput and testName
	 * @param runId
	 * @param testOutput
	 * @param testName
	 * @return
	 */
	public JSONObject addResultForTestByRun
			(Long runId, TestOutput testOutput,String testName) {	
		JSONObject testObject = get_test_ByRunIdAndTitle(runId, testName);
		return add_result((Long) testObject.get("id"), testOutput);
	}
	

	/**
	 * Add result for test by run-id iTestResult, output and testName
	 * @param runId
	 * @param testOutput
	 * @param testName
	 * @return the result JSONObject 
	 */
	public JSONObject addResultForTestByRun
			(Long runId,  ITestResult iTestResult, String output, String testName) {	
		JSONObject testObject = get_test_ByRunIdAndTitle(runId, testName);		
		try {
			return add_result((Long) testObject.get("id"),iTestResult, output);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	
	// getPlans/getEntries/getRun - update by phone type and config
	/**
	 * add multiple results
	 * @param runId
	 * @param mapResults
	 * @return JSONObject of the results
	 */
	public JSONObject add_results(Long runId, Map<String, Object> mapResults) {
		try {
			return (JSONObject) client.sendPost("add_results/" + runId,
					mapResults);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}

	// -------CONFIGURATION
	
	private JSONArray getAllConfigIds() {

		List<JSONObject> jsonConfigObjects = get_configs_AsListOfJSONObject();
		List<Long> configIdList = new ArrayList<Long>();
		JSONArray configArrayIds = new JSONArray();

		for (JSONObject l : jsonConfigObjects) {
		
			configIdList.add((Long) l.get("id"));
		}
		configArrayIds.addAll(configIdList);
		return configArrayIds;

	}

	public List<JSONObject> get_configs_AsListOfJSONObject() {
		List<JSONObject> jsonConfigObjectsList = new ArrayList<JSONObject>();

		JSONArray jsonConfigObjectsArray = get_configs();

		for (int j = 0; j < jsonConfigObjectsArray.size(); j++) {
			JSONObject configs = (JSONObject) jsonConfigObjectsArray.get(j);
			JSONArray array = (JSONArray) configs.get("configs");
			for (int i = 0; i < array.size(); i++) {
				jsonConfigObjectsList.add((JSONObject) array.get(i));
			}
		}
		return jsonConfigObjectsList;
	}

	/**
	 * <div id="left-content">
	 * 
	 * <div class="dokuwiki">
	 * 
	 * <p>
	 * 
	 * <style type="text/css"> div.dokuwiki h2 { border: none; padding: 4px 6px
	 * !important; background-color: #575852 !important; color: #e6db74;
	 * font-family: monospace; font-weight: normal; font-size: 14px; }
	 * 
	 * div.dokuwiki h4 { cursor: pointer; } </style>
	 * 
	 * </p>
	 * 
	 * <h1 class="sectionedit1" id="apiconfigurations">API: Configurations</h1>
	 * <div class="level1">
	 * 
	 * <p>
	 * Use the following <abbr
	 * title="Application Programming Interface">API</abbr> methods to request
	 * details about configurations.
	 * </p>
	 * 
	 * <p>
	 * Requires <strong>TestRail 3.1</strong> or later.
	 * </p>
	 * 
	 * </div>
	 * 
	 * <h2 class="sectionedit2" id="get_configs">get_configs</h2> <div
	 * class="level2">
	 * 
	 * <p>
	 * Returns a list of available configurations, grouped by configuration
	 * groups.
	 * </p>
	 * 
	 * <pre class="code">
	 * GET index.php?/api/v2/get_configs/:project_id
	 * </pre>
	 * 
	 * <p>
	 * 
	 * </p>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col> </colgroup> <tbody>
	 * <tr class="row0">
	 * <td class="col0 leftalign">:project_id</td>
	 * <td class="col1">The ID of the project</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * </p>
	 * 
	 * </div>
	 * 
	 * <h4 id="response_content"><img class="expand"
	 * src="/_media/testrail-api2/expand.png" style="display: none;"><img
	 * class="collapse" src="/_media/testrail-api2/collapse.png" style="">
	 * Response content</h4> <div class="level4" style="display: block;">
	 * 
	 * <p>
	 * The response includes an array of configuration groups, each with a list
	 * of configurations. Please see below for a typical example:
	 * </p>
	 * 
	 * <pre class="code">
	 * [
	 * 	{
	 * 		"configs": [
	 * 			{
	 * 				"group_id": 1,
	 * 				"id": 1,
	 * 				"name": "Chrome"
	 * 			},
	 * 			{
	 * 				"group_id": 1,
	 * 				"id": 2,
	 * 				"name": "Firefox"
	 * 			},
	 * 			{
	 * 				"group_id": 1,
	 * 				"id": 3,
	 * 				"name": "Internet Explorer"
	 * 			}
	 * 		],
	 * 		"id": 1,
	 * 		"name": "Browsers",
	 * 		"project_id": 1
	 * 	},
	 * 	{
	 * 		"configs": [
	 * 			{
	 * 				"group_id": 2,
	 * 				"id": 6,
	 * 				"name": "Ubuntu 12"
	 * 			},
	 * 			{
	 * 				"group_id": 2,
	 * 				"id": 4,
	 * 				"name": "Windows 7"
	 * 			},
	 * 			{
	 * 				"group_id": 2,
	 * 				"id": 5,
	 * 				"name": "Windows 8"
	 * 			}
	 * 		],
	 * 		"id": 2,
	 * 		"name": "Operating Systems",
	 * 		"project_id": 1
	 * 	}
	 * ]
	 * </pre>
	 * 
	 * <p>
	 * The example response includes two configuration groups (
	 * <code>Browsers</code> and <code>Operating Systems</code>), each with
	 * three example configurations:
	 * </p>
	 * 
	 * <p>
	 * 
	 * </p>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 75px"> <col style="width: 200px"> <col>
	 * </colgroup> <tbody>
	 * <tr class="row0">
	 * <th class="col0 leftalign">ID</th>
	 * <th class="col1 leftalign">Group</th>
	 * <th class="col2 leftalign">Configuration</th>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">1</td>
	 * <td class="col1 leftalign">Browsers</td>
	 * <td class="col2">Chrome</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">2</td>
	 * <td class="col1 leftalign">Browsers</td>
	 * <td class="col2">Firefox</td>
	 * </tr>
	 * <tr class="row3">
	 * <td class="col0 leftalign">3</td>
	 * <td class="col1 leftalign">Browsers</td>
	 * <td class="col2">Internet Explorer</td>
	 * </tr>
	 * <tr class="row4">
	 * <td class="col0 leftalign">4</td>
	 * <td class="col1 leftalign">Operating Systems</td>
	 * <td class="col2">Windows 7</td>
	 * </tr>
	 * <tr class="row5">
	 * <td class="col0 leftalign">5</td>
	 * <td class="col1 leftalign">Operating Systems</td>
	 * <td class="col2">Windows 8</td>
	 * </tr>
	 * <tr class="row6">
	 * <td class="col0 leftalign">6</td>
	 * <td class="col1 leftalign">Operating Systems</td>
	 * <td class="col2">Ubuntu 12</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * </p>
	 * 
	 * <p>
	 * Please also see <a href="/testrail-api2/reference-plans#add_plan_entry"
	 * class="wikilink1"
	 * title="testrail-api2:reference-plans">add_plan_entry</a> and <a
	 * href="/testrail-api2/reference-plans#add_plan" class="wikilink1"
	 * title="testrail-api2:reference-plans">add_plan</a> for an example on how
	 * to use configurations.
	 * </p>
	 * 
	 * </div>
	 * 
	 * <h4 id="response_codes"><img class="expand"
	 * src="/_media/testrail-api2/expand.png"><img class="collapse"
	 * src="/_media/testrail-api2/collapse.png" style="display: none"> Response
	 * codes</h4> <div class="level4" style="display: none;">
	 * 
	 * <p>
	 * 
	 * </p>
	 * <table class="inline" style="width: 100%">
	 * <colgroup> <col style="width: 150px"> <col> </colgroup> <tbody>
	 * <tr class="row0">
	 * <td class="col0 leftalign" style="vertical-align: top">200</td>
	 * <td class="col1">Success, the configurations are returned as part of the
	 * response</td>
	 * </tr>
	 * <tr class="row1">
	 * <td class="col0 leftalign">400</td>
	 * <td class="col1">Invalid or unknown project</td>
	 * </tr>
	 * <tr class="row2">
	 * <td class="col0 leftalign">403</td>
	 * <td class="col1">No access to the project</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * </p>
	 * 
	 * <p>
	 * 
	 * <script type="text/javascript"> jQuery(document).ready( function() {
	 * jQuery('div.level4').hide(); jQuery('h4').prepend('<img class="expand"
	 * src="/_media/testrail-api2/expand.png" /><img class="collapse"
	 * src="/_media/testrail-api2/collapse.png" style="display: none" /> ');
	 * jQuery('h4').click( function() { var h = jQuery(this); if
	 * (h.next('div').is(':visible')) { h.next('div').hide();
	 * h.find('img.expand').show(); h.find('img.collapse').hide(); } else {
	 * h.find('img.expand').hide(); h.find('img.collapse').show();
	 * h.next('div').show(); } } ); }); </script>
	 * 
	 * </p>
	 * 
	 * </div>
	 * 
	 * 
	 * <div class="meta"> <div class="user"> </div> </div> </div> </div>
	 * 
	 * @param projectId
	 * @return
	 */
	public JSONArray get_configs() {
		try {
			return (JSONArray) client.sendGet("get_configs/"
					+ DEFAULT_PROJECT_ID);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}


	//--------------------//
	//--------CASE--------//
	//--------------------//
	
	/**
	 * get all cases by project and suite id's
	 * 
	 * @param project_id
	 * @param suite_id
	 * @return
	 */
	public JSONArray get_cases(Long project_id, Long suite_id) {
		try {
			JSONArray get_casesResponse_content = (JSONArray) client
					.sendGet("get_cases/" + project_id + "/&suite_id="
							+ suite_id);
			return get_casesResponse_content;
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject get_cases_Persist() {
		try {
			return (JSONObject) client.sendGet("get_cases/"
					+ DEFAULT_PROJECT_ID + "&suite_id="
					+ PropertiesUtil.getInstance().getProperty("TEST_RAIL_PERSIST_SUITE"));
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public JSONObject get_cases_Plug() {
		try {
			return (JSONObject) client.sendGet("get_cases/"
					+ DEFAULT_PROJECT_ID + "&suite_id="
					+ PropertiesUtil.getInstance().getProperty("TEST_RAIL_PLUG_SUITE"));
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public JSONArray get_cases_PlugIdsAsJSONArray() {
		List<Long> list = new ArrayList<Long>();
		JSONArray objectArray;
		try {
			objectArray = (JSONArray) client.sendGet("get_cases/"
					+ DEFAULT_PROJECT_ID + "&suite_id="
					+ PropertiesUtil.getInstance().getProperty("TEST_RAIL_PLUG_SUITE"));
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}
		for (int j = 0; j < objectArray.size(); j++) {
			JSONObject jot = (JSONObject) objectArray.get(j);
			String id = "" + jot.get("id");
			list.add(Long.valueOf(id));
		}
		JSONArray objectArrayIds = new JSONArray();
		objectArrayIds.addAll(list);
		return objectArrayIds;
	}

	// ------------TESTS
	/**
	 * get run name and title, run over getRunByName() to get the run, from it
	 * extract the id of this run, then get all tests and of this specific run ,
	 * from it if any match by name it return this json object
	 * 
	 * @param runId
	 * @param title
	 * @return
	 */
	public JSONObject get_test_ByRunIdAndTitle(Long runId, String title) {
		JSONArray testsArray = get_tests(runId);
		for (int i = 0; i < testsArray.size(); i++) {
			JSONObject jot = (JSONObject) testsArray.get(i);
			String custom_method_name = String.valueOf(jot.get("custom_method_name"));
			if (custom_method_name != null 
			 && !custom_method_name.isEmpty()
			 && custom_method_name.equalsIgnoreCase(title))
				return jot;
			if (jot.get("title").toString().equalsIgnoreCase(title))
				return jot;
		}
		return null;
	}
	
	/**
	 * get all test names of plug specific run by plan name phone config and rsim config
	 * @param planName
	 * @param phoneConfig
	 * @param rsimConfig
	 * @return List<String> of test names
	 */
	public List<String> getAllTestNames(String planName, String phoneConfig,String rsimConfig) {
		List<String> testNames = new ArrayList<String>();
		JSONObject runByConfigName = getRunByConfigName(planName, phoneConfig, rsimConfig);		
		JSONArray testsArray = get_tests((Long)runByConfigName.get("id"));
		
		for (int j = 0; j < testsArray.size(); j++) {
			JSONObject jot = (JSONObject) testsArray.get(j);
			testNames.add(jot.get("title").toString());
		}
		return !testNames.isEmpty() ? testNames : null;
	}
	
	/**
	 * 
	 * @param planName
	 * @param configName
	 * @param testOutput
	 * @param testName
	 * @return
	 */
	public JSONObject addResultForTestByPlanGeneric(String planName,String configName,
			TestOutput testOutput, String testName) {

		JSONObject planObject = getPlanByName(planName);
		JSONArray entriesFromPlan = getEntriesByPlan(planObject);
		JSONArray runsFromEntries = null;
		for (int i = 0; i < entriesFromPlan.size(); i++) {
			JSONObject temp = (JSONObject) entriesFromPlan.get(i);
			if (temp.containsKey("runs"))
				runsFromEntries = (JSONArray) temp.get("runs");
		}
		JSONObject runToUpdate = null;
		for (int i = 0; i < runsFromEntries.size(); i++) {
			JSONObject temp = (JSONObject) runsFromEntries.get(i);
			if (temp.get("config").toString().contains(configName)) {
				runToUpdate = temp;
			}
		}
		JSONObject testObject = get_test_ByRunIdAndTitle((Long) runToUpdate.get("id"),
				testName);
		Long testId = (Long) testObject.get("id");
		JSONObject resultObject = add_result(testId, testOutput);
	
		return resultObject;
	}
	
	public boolean isThisARun(String name) {
		this.isRun = getRunByName(name) != null ? true : false;
		return isRun;
	}

	public boolean isThisAPlan(String name) {
		this.isPlan = getPlanByName(name) != null ? true : false;
		return isPlan;
	}


	/**
	 * @param api
	 * @param plans
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws APIException
	 */
	public JSONObject getRunBySuiteId(Long suiteId) {
		if (this.run!=null) 
			return this.run;
		
		JSONArray entries = getEntriesByPlan(this.plan);
		
		List<JSONArray> runs = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			JSONObject temp = (JSONObject) entries.get(i);
			if (temp.containsKey("runs")) {
				runs.add((JSONArray) temp.get("runs"));
			}
		}
		
		Long runId = null;
		outer: 
		for (JSONArray run : runs) {
			for (int i = 0; i < run.size(); i++) {
				JSONObject temp = (JSONObject) run.get(i);
				String tempSuiteId = temp.get("suite_id").toString();
				if (tempSuiteId.equals(suiteId + "")) {
					runId = (Long) temp.get("id");
					break outer;
				}
			}
		}
		
		if(runId==null)
			System.err.println("Run Id not found");
		
		try {
			return get_run(runId);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param api
	 * @param runId
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws APIException
	 */
	private JSONObject get_run(Long runId) throws MalformedURLException,
			IOException, APIException {
		return (JSONObject) client.sendGet("get_run/" + runId);
	}

	/**
	 * @param runs
	 * @param suiteId
	 * @return
	 */
	private Long getRunIdBySuiteIdAndRuns(JSONArray runs, Long suiteId) {
		for (int i = 0; i < runs.size(); i++) {
			JSONObject temp = (JSONObject) runs.get(i);
			String tempSuiteId = temp.get("suite_id").toString();
			if (tempSuiteId.equals(suiteId+"")) {
				return (Long) temp.get("id");
			}
		}
		return null;
	}

	/**
	 * @param entries
	 * @return
	 */
	private JSONArray getRunsByEntries(JSONArray entries) {
		JSONArray runs = null;
		for (int i = 0; i < entries.size(); i++) {
			JSONObject temp = (JSONObject) entries.get(i);
			if (temp.containsKey("runs")) {
				runs = (JSONArray) temp.get("runs");
				break;
			}
		}
		return runs;
	}

	/**
	 * @param plan
	 * @return
	 */
	private JSONArray getEntriesByPlan(JSONObject plan) {
		JSONArray entries = (JSONArray) plan.get("entries");
		return entries;
	}
	
	

	private JSONArray get_plans(Long projectId) throws MalformedURLException,
			IOException, APIException {
		// Long projectId = 6l;
		return (JSONArray) client.sendGet("get_plans/" + projectId);
	}

	
	
	/**
	 * Insert the result of the test as TestOuput.java <br>
	 * Add the TestRailApi instance <br>
	 * Insert the Run name <br>
	 * the method update the testrail just if the method name is exactly (ignore cases and white spaces) as the tests names in testrail<br>
	 * @param output
	 * @param testRailApi
	 * @param runName
	 */
	public  boolean updateTestRailByRunId(TestOutput output,Long runId) {
		String methodName = getMethodName();
		String testName = getFromMethodNameTheTestTitle(methodName, runId);
		
		if (testName != null) {
			addResultForTestByRun(runId, output, testName);
			return true;
		}
		return false;
	}
	
	/**
	 * Insert the result of the test as TestOuput.java <br>
	 * Add the TestRailApi instance <br>
	 * Insert the Run name <br>
	 * the method update the testrail just if the method name is exactly (ignore cases and white spaces) as the tests names in testrail<br>
	 * 
	 * @param testName
	 * @param iTestResult
	 * @param output
	 * @param runId
	 * @return 
	 */
	public  boolean updateTestRailByRunId(String testName, ITestResult iTestResult, String output, Long runId) {
		if (testName != null) {
			addResultForTestByRun(runId, iTestResult, output, testName);
			return true;
		}
		return false;
	}
	
	/**
	 * get from the the suiteId attribute the runObject insert the result and update by that
	 * @param output
	 * @return true if update success
	 */
	public  boolean updateTestRail(TestOutput output ){
		JSONObject runObject = null;// getRunBySuiteId(getSuiteId());
		try {
			if (isRun)
				runObject = getRun();
			else if (isPlan) {
				runObject = getRunBySuiteId(suiteId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean updateTestRailByRunId = false;
		if (runObject!=null) {
			updateTestRailByRunId = updateTestRailByRunId(output,(Long) runObject.get("id"));
		}
		return updateTestRailByRunId;
	}
	
	/**
	 * get from the the suiteId attribute the runObject insert the result and update by that
	 * 
	 * @param String testName if null it get the test name from method name
	 * @param ITestResult iTestResult
	 * @param String output
	 * @return result JSONObject 
	 */
	public  JSONObject updateTestRail(String testName, ITestResult iTestResult, String output){
		JSONObject runObject = null;
		JSONObject rv = null;

		try {
			if (isRun)
				runObject = getRun();
			else if (isPlan) {
				runObject = getRunBySuiteId(suiteId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.runId = (Long) runObject.get("id");
		if(testName==null){
			testName = getFromMethodNameTheTestTitle(getMethodName(iTestResult), runId);
		}
		if (runObject!=null) {
			rv = addResultForTestByRun(runId, iTestResult, output, testName);
		}
		return rv;
	}
	
	
	//Getters/Setters
	public JSONObject getPlan() {
		return plan;
	}

	public void setPlan(JSONObject plan) {
		this.plan = get_plan((Long)plan.get("id"));
		this.isPlan = true;
	}
	
	public JSONObject getRun() {
		return run;
	}

	public void setRun(JSONObject run) {
		this.run = run;
		this.runId = (Long) run.get("id");
		this.isRun = true;
	}

	public Long getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(Long suiteId) {
		this.suiteId = suiteId;
	}

	// add this method as of compare in other way
	/**
	 * @param methodName
	 *            from the @Test
	 * @return the matching test_title
	 */
	public String getFromXSLMethodNameTheTestTitle(String methodName, Long runId) {
		this.titles = getAllTestTitle(runId);
		for (String title : titles) 
			if (methodName.equalsIgnoreCase(title)) 
				return title;
		System.err.println("Method Name " + methodName + " does  not exist in TestRail");
		return null;
	}
	/**
	 * get the run name from environment variables from jenkins or use the default name from properties
	 * @return String - runName
	 */
	private String getPlanOrRunName(String defaultName) {
		String build_tag = System.getenv("BUILD_TAG");
		String git_branch = System.getenv("GIT_BRANCH");

		if (build_tag != null && git_branch != null)
			return build_tag + "_" + git_branch;
		else
			return defaultName;
	}
	
	/**
	 * Add plan into TestRail from JSONObject plan
	 * @param plan
	 * @return JSONObject
	 */
	public JSONObject add_plan(JSONObject plan){
		try {
			return (JSONObject) client.sendPost("add_plan/" + DEFAULT_PROJECT_ID, plan);
		} catch (IOException | APIException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * Create JSONObject plan
	 * @param name
	 * @param entries
	 * @return JSONObject
	 */
	public JSONObject addPlan(String name, JSONArray entries){
		JSONObject plan = new JSONObject();
		plan.put("name", name);
		plan.put("entries", entries);
		return plan;	
	}
	
	/**
	 * Create JSONArray of entries from one or more entry JSONObjects 
	 * @param entries 
	 * @return JSONArray
	 */
	public JSONArray addEntries(JSONObject... entries){
		JSONArray rv = new JSONArray();
		for (int i = 0; i < entries.length; i++) {
			rv.add(entries[i]);
		}
		return rv;
	}
	
	/**
	 * Create entry JSONObject 
	 * @param name - can be empty and extends the suite name
	 * @param suiteId - can only built on one suite id
	 * @param runs - JSONArray of runs
	 * @param configGroupId - one or more configuration group id's
	 * @return JSONObject
	 */
	public JSONObject addEntry(String name, Long suiteId, JSONArray runs, Long configGroupId){
		JSONObject entry = new JSONObject();
		 
		JSONArray configsByGroupId = getAllConfigIdsByGroupIds(configGroupId);
		entry.put("config_ids", configsByGroupId);
		entry.put("include_all", true);

		entry.put("name", name == null ? "" : name);
		entry.put("suite_id", suiteId);
		checkIfRunsConfigIncludeInEntry(configsByGroupId,runs);
		entry.put("runs", runs);
		return entry;
	}
	
	/**
	 * Create JSONArray of runs from one or more run JSONObjects
	 * @param runs
	 * @return JSONArray
	 */
	public JSONArray addRuns(JSONObject... runs){
		JSONArray rv = new JSONArray();
		for (int i = 0; i < runs.length; i++) {
			rv.add(runs[i]);
		}
		return rv;
	}
	
	/**
	 * Create run JSONObject for plan (include only config_ids values)
	 * @param config_ids
	 * @return JSONObject
	 */
	public JSONObject addRun(Long... config_ids){
		//Checks if there is not more then one configuration from each group for run
		Map<Long, Long> oneGroupForOneConfig = new HashMap<>();
		List<Long> configidsAsList = Arrays.asList(config_ids);
		for (Config c : this.configs) {
			if (configidsAsList.contains(c.getId())) {
				//check if config from this group is already included
				if (oneGroupForOneConfig.get(c.getGroupId()) == null) {
					oneGroupForOneConfig.put(c.getGroupId(), c.getId());
				} else {
					//not throwing an exceptions because its not blocked the tests
					System.err.println("Run with " + c.getGroupId() + " Group id Already Exist, " + c.getId() + " ("+c.getName()+") Cannot be added");
				}
			}
		}
		JSONArray configs = new JSONArray();
		for (Long key : oneGroupForOneConfig.keySet()) {
			configs.add(oneGroupForOneConfig.get(key));
		}
		JSONObject run = new JSONObject();
		run.put("config_ids", configs);
		return run;
	}
	
	/**
	 * Checks if config ids from 'runs' includes in 'entry' config ids </br>
	 * 
	 * @param entryConfigs
	 * @param runsConfigs
	 * @throws RunTimeException in case of failure
	 */
	private void checkIfRunsConfigIncludeInEntry(JSONArray entryConfigs, JSONArray runsConfigs) {
		List<Long> runsConfigIds = new ArrayList<>();
		List<Long> entryConfigIds = new ArrayList<>();
		//init runs Config Ids
		for (int i = 0; i < runsConfigs.size(); i++) {
			JSONObject run = (JSONObject) runsConfigs.get(i);
			JSONArray configs = (JSONArray) run.get("config_ids");
			for (int j = 0; j < configs.size(); j++) {
				runsConfigIds.add((Long) configs.get(j));
			}
		}
		//init entry Config Ids
		for (int i = 0; i < entryConfigs.size(); i++) {
			entryConfigIds.add((Long) entryConfigs.get(i));
		}
		// for all run configs if entry configs not contains this id throwing exception
		for (Long configId : runsConfigIds) {
			if (!entryConfigIds.contains(configId)) {
				throw new RuntimeException("Run id "+configId+" is not included in the Entry configs");
			}
		}
	
	}
	
	/**
	 * Initialized all TestRail configurations as each config as
	 * bean.Config.</br>
	 * The hierarchy is like that: [{a:[{a,b,c},{e,f,g}],b,c,d}].</br>
	 * JSONArray of JSONObject config groups.</br>
	 * Inner config group JSONObject with (JSONArray configs, long project_id,
	 * String name, long id(group-id)).</br>
	 * Inner this JSONArray of JSONObject configs .</br>
	 * Inner each config JSONObject (long group_id, String name, long id).</br>
	 * 
	 * @return List<Config>
	 */
	private List<Config> initialConfigs() {
		this.configs = new ArrayList<>();
		JSONArray configs = get_configs();
		for (int i = 0; i < configs.size(); i++) {
			JSONObject tempConfigGroup = (JSONObject) configs.get(i);
			//get config group id, name and configs
			Long groupId = (Long) tempConfigGroup.get("id");
			String groupName = (String) tempConfigGroup.get("name");
			JSONArray tempConfigsArray = (JSONArray) tempConfigGroup.get("configs");
			for (int j = 0; j < tempConfigsArray.size(); j++) {
				JSONObject tempConfig = (JSONObject) tempConfigsArray.get(j);
				//get config id and name
				Long id = (Long) tempConfig.get("id");
				String name = (String) tempConfig.get("name");				
				this.configs.add(new Config(groupName, groupId, name, id));
			}
		}
		return this.configs;
	}
	
}
