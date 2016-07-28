package testrail.refrences;

import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;

/**
 * A test case in TestRail consists of a description of the test's
 * prerequisites, a list of test steps and the expected result. A test case can
 * ideally be verified by a single tester in a short period of time and confirms
 * a specific functionality, documents a task or verifies a project artifact.
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Case {

	private static final String GET_CASE = "get_case/";

	private Object created_by, created_on, estimate, estimate_forecast, id, 
			milestone_id, priority_id, refs, section_id,
			suite_id, template_id, title, type_id, updated_by, updated_on;

	private JSONObject jsonObject;
	
	/**
	 * get case by case id
	 * 
	 * @param id
	 * @return Case
	 */
	public static Case getCase(long id) {
		return new Case(TestRailUtil.getInstance().sendGetJObj(GET_CASE + id));
	}
	
	public Case(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}

	/**
	 * Initialized Case from the json object 
	 */
	private void initJObj() {
		this.created_by = jsonObject.get("created_by");
		this.created_on = jsonObject.get("created_on");
		this.estimate = jsonObject.get("estimate");
		this.estimate_forecast = jsonObject.get("estimate_forecast");
		this.id = jsonObject.get("id");
		this.milestone_id = jsonObject.get("milestone_id");
		this.priority_id = jsonObject.get("priority_id");
		this.refs = jsonObject.get("refs");
		this.section_id = jsonObject.get("section_id");
		this.suite_id = jsonObject.get("suite_id");
		this.template_id = jsonObject.get("template_id");
		this.title = jsonObject.get("title");
		this.type_id = jsonObject.get("type_id");
		this.updated_by = jsonObject.get("updated_by");
		this.updated_on = jsonObject.get("updated_on");
	}
	
	// Getters / Setters
	
	public Object getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Object created_by) {
		this.created_by = created_by;
	}

	public Object getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Object created_on) {
		this.created_on = created_on;
	}

	public Object getEstimate() {
		return estimate;
	}

	public void setEstimate(Object estimate) {
		this.estimate = estimate;
	}

	public Object getEstimate_forecast() {
		return estimate_forecast;
	}

	public void setEstimate_forecast(Object estimate_forecast) {
		this.estimate_forecast = estimate_forecast;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(Object milestone_id) {
		this.milestone_id = milestone_id;
	}

	public Object getPriority_id() {
		return priority_id;
	}

	public void setPriority_id(Object priority_id) {
		this.priority_id = priority_id;
	}

	public Object getRefs() {
		return refs;
	}

	public void setRefs(Object refs) {
		this.refs = refs;
	}

	public Object getSection_id() {
		return section_id;
	}

	public void setSection_id(Object section_id) {
		this.section_id = section_id;
	}

	public Object getSuite_id() {
		return suite_id;
	}

	public void setSuite_id(Object suite_id) {
		this.suite_id = suite_id;
	}

	public Object getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Object template_id) {
		this.template_id = template_id;
	}

	public Object getTitle() {
		return title;
	}

	public void setTitle(Object title) {
		this.title = title;
	}

	public Object getType_id() {
		return type_id;
	}

	public void setType_id(Object type_id) {
		this.type_id = type_id;
	}

	public Object getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Object updated_by) {
		this.updated_by = updated_by;
	}

	public Object getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Object updated_on) {
		this.updated_on = updated_on;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "Case [created_by=" + created_by + ", created_on=" + created_on + ", estimate=" + estimate
				+ ", estimate_forecast=" + estimate_forecast + ", id=" + id + ", milestone_id=" + milestone_id
				+ ", priority_id=" + priority_id + ", refs=" + refs + ", section_id=" + section_id + ", suite_id="
				+ suite_id + ", template_id=" + template_id + ", title=" + title + ", type_id=" + type_id
				+ ", updated_by=" + updated_by + ", updated_on=" + updated_on + ", jsonObject="
				+ jsonObject + "]";
	}

}
