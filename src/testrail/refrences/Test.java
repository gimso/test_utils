package testrail.refrences;

import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;

public class Test {

	private static final String GET_TEST = "get_test/";

	private Object run_id, custom_expected, type_id, custom_method_name, custom_steps_separated, milestone_id,
			estimate_forecast, title, custom_preconds, priority_id, status_id, refs, case_id, estimate, assignedto_id,
			template_id, id;
	
	private JSONObject jsonObject;
	
	public static Test getTest(long id) {
		JSONObject jsonObject = TestRailUtil.getInstance().sendGetJObj(GET_TEST + id);
		return new Test(jsonObject);
	}
	
	public static Test getTest(String title, Run run) {
		for (Test test : run.getTests())
			if (test.getTitle().equals(title))
				return test;
		return null;
	}

	public Test(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}

	public void initJObj() {
		this.run_id = jsonObject.get("run_id");
		this.custom_expected = jsonObject.get("custom_expected");
		this.type_id = jsonObject.get("type_id");
		this.custom_method_name = jsonObject.get("custom_method_name");
		this.custom_steps_separated = jsonObject.get("custom_steps_separated");
		this.milestone_id = jsonObject.get("milestone_id");
		this.estimate_forecast = jsonObject.get("estimate_forecast");
		this.title = jsonObject.get("title");
		this.custom_preconds = jsonObject.get("custom_preconds");
		this.priority_id = jsonObject.get("priority_id");
		this.status_id = jsonObject.get("status_id");
		this.refs = jsonObject.get("refs");
		this.case_id = jsonObject.get("case_id");
		this.estimate = jsonObject.get("estimate");
		this.assignedto_id = jsonObject.get("assignedto_id");
		this.template_id = jsonObject.get("template_id");
		this.id = jsonObject.get("id");

	}

	

	public Object getRun_id() {
		return run_id;
	}

	public void setRun_id(Object run_id) {
		this.run_id = run_id;
	}

	public Object getCustom_expected() {
		return custom_expected;
	}

	public void setCustom_expected(Object custom_expected) {
		this.custom_expected = custom_expected;
	}

	public Object getType_id() {
		return type_id;
	}

	public void setType_id(Object type_id) {
		this.type_id = type_id;
	}

	public Object getCustom_method_name() {
		return custom_method_name;
	}

	public void setCustom_method_name(Object custom_method_name) {
		this.custom_method_name = custom_method_name;
	}

	public Object getCustom_steps_separated() {
		return custom_steps_separated;
	}

	public void setCustom_steps_separated(Object custom_steps_separated) {
		this.custom_steps_separated = custom_steps_separated;
	}

	public Object getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(Object milestone_id) {
		this.milestone_id = milestone_id;
	}

	public Object getEstimate_forecast() {
		return estimate_forecast;
	}

	public void setEstimate_forecast(Object estimate_forecast) {
		this.estimate_forecast = estimate_forecast;
	}

	public Object getTitle() {
		return title;
	}

	public void setTitle(Object title) {
		this.title = title;
	}

	public Object getCustom_preconds() {
		return custom_preconds;
	}

	public void setCustom_preconds(Object custom_preconds) {
		this.custom_preconds = custom_preconds;
	}

	public Object getPriority_id() {
		return priority_id;
	}

	public void setPriority_id(Object priority_id) {
		this.priority_id = priority_id;
	}

	public Object getStatus_id() {
		return status_id;
	}

	public void setStatus_id(Object status_id) {
		this.status_id = status_id;
	}

	public Object getRefs() {
		return refs;
	}

	public void setRefs(Object refs) {
		this.refs = refs;
	}

	public Object getCase_id() {
		return case_id;
	}

	public void setCase_id(Object case_id) {
		this.case_id = case_id;
	}

	public Object getEstimate() {
		return estimate;
	}

	public void setEstimate(Object estimate) {
		this.estimate = estimate;
	}

	public Object getAssignedto_id() {
		return assignedto_id;
	}

	public void setAssignedto_id(Object assignedto_id) {
		this.assignedto_id = assignedto_id;
	}

	public Object getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Object template_id) {
		this.template_id = template_id;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "Test [run_id=" + run_id + ", custom_expected=" + custom_expected + ", type_id=" + type_id
				+ ", custom_method_name=" + custom_method_name + ", custom_steps_separated=" + custom_steps_separated
				+ ", milestone_id=" + milestone_id + ", estimate_forecast=" + estimate_forecast + ", title=" + title
				+ ", custom_preconds=" + custom_preconds + ", priority_id=" + priority_id + ", status_id=" + status_id
				+ ", refs=" + refs + ", case_id=" + case_id + ", estimate=" + estimate + ", assignedto_id="
				+ assignedto_id + ", template_id=" + template_id + ", id=" + id + ", jsonObject="
				+ jsonObject + "]";
	}
	
}
