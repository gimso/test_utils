package testrail.refrences;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;

public class Run {
	private static final String ADD_RUN = "add_run/";
	private static final String GET_TESTS = "get_tests/";
	private static final String GET_RUN = "get_run/";

	private Object completed_on, milestone_id, description, custom_status3_count, is_completed, retest_count,
			custom_status5_count, project_id, id, suite_id, custom_status2_count, include_all, passed_count,
			custom_status7_count, custom_status4_count, created_by, url, config_ids, blocked_count, created_on,
			untested_count, name, assignedto_id, failed_count, custom_status1_count, custom_status6_count, config,
			plan_id;
	private List<Test> tests;

	private JSONObject jsonObject;
	private TestRailUtil testRailUtil = TestRailUtil.getInstance();

	public static Run getRun(long id) {
		return new Run(TestRailUtil.getInstance().sendGetJObj(GET_RUN + id));
	}

	public Run(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}

	@SuppressWarnings("unchecked")
	public Run(Suite suite) {
		this.jsonObject = new JSONObject();
		this.suite_id = suite.getId();
		this.jsonObject.put("suite_id", this.suite_id);
		this.project_id = testRailUtil.getProject().getId();
	}

	public void addRun() {
		this.jsonObject = testRailUtil.sendPost(ADD_RUN + this.project_id, jsonObject);

	}
	
	public void initJObj() {
		this.completed_on = jsonObject.get("completed_on");
		this.milestone_id = jsonObject.get("milestone_id");
		this.description = jsonObject.get("description");
		this.custom_status3_count = jsonObject.get("custom_status3_count");
		this.is_completed = jsonObject.get("is_completed");
		this.retest_count = jsonObject.get("retest_count");
		this.custom_status5_count = jsonObject.get("custom_status5_count");
		this.project_id = jsonObject.get("project_id");
		this.id = jsonObject.get("id");
		this.suite_id = jsonObject.get("suite_id");
		this.custom_status2_count = jsonObject.get("custom_status2_count");
		this.include_all = jsonObject.get("include_all");
		this.passed_count = jsonObject.get("passed_count");
		this.custom_status7_count = jsonObject.get("custom_status7_count");
		this.custom_status4_count = jsonObject.get("custom_status4_count");
		this.created_by = jsonObject.get("created_by");
		this.url = jsonObject.get("url");
		this.config_ids = jsonObject.get("config_ids");
		this.blocked_count = jsonObject.get("blocked_count");
		this.created_on = jsonObject.get("created_on");
		this.untested_count = jsonObject.get("untested_count");
		this.name = jsonObject.get("name");
		this.assignedto_id = jsonObject.get("assignedto_id");
		this.failed_count = jsonObject.get("failed_count");
		this.custom_status1_count = jsonObject.get("custom_status1_count");
		this.custom_status6_count = jsonObject.get("custom_status6_count");
		this.config = jsonObject.get("config");
		this.plan_id = jsonObject.get("plan_id");
	}

	public Object getCompleted_on() {
		return completed_on;
	}

	public void setCompleted_on(Object completed_on) {
		this.completed_on = completed_on;
	}

	public Object getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(Object milestone_id) {
		this.milestone_id = milestone_id;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public Object getCustom_status3_count() {
		return custom_status3_count;
	}

	public void setCustom_status3_count(Object custom_status3_count) {
		this.custom_status3_count = custom_status3_count;
	}

	public Object getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(Object is_completed) {
		this.is_completed = is_completed;
	}

	public Object getRetest_count() {
		return retest_count;
	}

	public void setRetest_count(Object retest_count) {
		this.retest_count = retest_count;
	}

	public Object getCustom_status5_count() {
		return custom_status5_count;
	}

	public void setCustom_status5_count(Object custom_status5_count) {
		this.custom_status5_count = custom_status5_count;
	}

	public Object getProject_id() {
		return project_id;
	}

	public void setProject_id(Object project_id) {
		this.project_id = project_id;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getSuite_id() {
		return suite_id;
	}

	public void setSuite_id(Object suite_id) {
		this.suite_id = suite_id;
	}

	public Object getCustom_status2_count() {
		return custom_status2_count;
	}

	public void setCustom_status2_count(Object custom_status2_count) {
		this.custom_status2_count = custom_status2_count;
	}

	public Object getInclude_all() {
		return include_all;
	}

	public void setInclude_all(Object include_all) {
		this.include_all = include_all;
	}

	public Object getPassed_count() {
		return passed_count;
	}

	public void setPassed_count(Object passed_count) {
		this.passed_count = passed_count;
	}

	public Object getCustom_status7_count() {
		return custom_status7_count;
	}

	public void setCustom_status7_count(Object custom_status7_count) {
		this.custom_status7_count = custom_status7_count;
	}

	public Object getCustom_status4_count() {
		return custom_status4_count;
	}

	public void setCustom_status4_count(Object custom_status4_count) {
		this.custom_status4_count = custom_status4_count;
	}

	public Object getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Object created_by) {
		this.created_by = created_by;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}

	public Object getConfig_ids() {
		return config_ids;
	}

	public void setConfig_ids(Object config_ids) {
		this.config_ids = config_ids;
	}

	public Object getBlocked_count() {
		return blocked_count;
	}

	public void setBlocked_count(Object blocked_count) {
		this.blocked_count = blocked_count;
	}

	public Object getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Object created_on) {
		this.created_on = created_on;
	}

	public Object getUntested_count() {
		return untested_count;
	}

	public void setUntested_count(Object untested_count) {
		this.untested_count = untested_count;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public Object getAssignedto_id() {
		return assignedto_id;
	}

	public void setAssignedto_id(Object assignedto_id) {
		this.assignedto_id = assignedto_id;
	}

	public Object getFailed_count() {
		return failed_count;
	}

	public void setFailed_count(Object failed_count) {
		this.failed_count = failed_count;
	}

	public Object getCustom_status1_count() {
		return custom_status1_count;
	}

	public void setCustom_status1_count(Object custom_status1_count) {
		this.custom_status1_count = custom_status1_count;
	}

	public Object getCustom_status6_count() {
		return custom_status6_count;
	}

	public void setCustom_status6_count(Object custom_status6_count) {
		this.custom_status6_count = custom_status6_count;
	}

	public Object getConfig() {
		return config;
	}

	public void setConfig(Object config) {
		this.config = config;
	}

	public Object getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(Object plan_id) {
		this.plan_id = plan_id;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public List<Test> getTests() {
		if (tests == null || tests.isEmpty()) {
			tests = new ArrayList<>();
			String sendGetCommand = GET_TESTS + this.id;
			JSONArray array = testRailUtil.sendGetJArray(sendGetCommand);
			for (Object o : array)
				if (o instanceof JSONObject)
					tests.add(new Test((JSONObject) o));
			return tests;
		}
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}
	
	public String getTestTitleFromMethod(String methodName) {
		for (Test test : tests) {
			String string = test.getTitle().toString();
			String temp = string.replaceAll("\\s", "");
			if (methodName.equalsIgnoreCase(temp))
				return string;
		}
		System.err.println("Method Name " + methodName + " does not exist in TestRail");
		return null;
	}

	@Override
	public String toString() {
		return "Run [completed_on=" + completed_on + ", milestone_id=" + milestone_id + ", description=" + description
				+ ", custom_status3_count=" + custom_status3_count + ", is_completed=" + is_completed
				+ ", retest_count=" + retest_count + ", custom_status5_count=" + custom_status5_count + ", project_id="
				+ project_id + ", id=" + id + ", suite_id=" + suite_id + ", custom_status2_count="
				+ custom_status2_count + ", include_all=" + include_all + ", passed_count=" + passed_count
				+ ", custom_status7_count=" + custom_status7_count + ", custom_status4_count=" + custom_status4_count
				+ ", created_by=" + created_by + ", url=" + url + ", config_ids=" + config_ids + ", blocked_count="
				+ blocked_count + ", created_on=" + created_on + ", untested_count=" + untested_count + ", name=" + name
				+ ", assignedto_id=" + assignedto_id + ", failed_count=" + failed_count + ", custom_status1_count="
				+ custom_status1_count + ", custom_status6_count=" + custom_status6_count + ", config=" + config
				+ ", plan_id=" + plan_id + "]";

	}

}
