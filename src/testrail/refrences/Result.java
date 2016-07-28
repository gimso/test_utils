package testrail.refrences;

import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;
import testrail.enums.Status;
import testrail.enums.User;

public class Result {
	private Object assignedto_id, comment, created_by, created_on, defects, elapsed, id, status_id, test_id, version,
			custom_log_file, custom_note, custom_step_results;

	private JSONObject jsonObject;
	private TestRailUtil testRailUtil = TestRailUtil.getInstance();

	public Result(JSONObject jsonObject) {
		initJObj();
	}

	@SuppressWarnings("unchecked")
	public Result(Long test_id, Status status, User user, String comment) {
		this.test_id = test_id;
		this.status_id = status.getValue();
		this.created_by = user.getValue();
		this.comment = comment;
		
		this.jsonObject = new JSONObject();
		this.jsonObject.put("status_id", status_id);
		this.jsonObject.put("created_by", created_by);
		this.jsonObject.put("comment", comment);
		addResult();
	}

	public void addResult() {
		this.jsonObject = testRailUtil.sendPost("add_result/" + test_id, jsonObject);
		initJObj();
	}

	public void initJObj() {
		this.assignedto_id = jsonObject.get("assignedto_id");
		this.comment = jsonObject.get("comment");
		this.created_by = jsonObject.get("created_by");
		this.created_on = jsonObject.get("created_on");
		this.defects = jsonObject.get("defects");
		this.elapsed = jsonObject.get("elapsed");
		this.id = jsonObject.get("id");
		this.status_id = jsonObject.get("status_id");
		this.test_id = jsonObject.get("test_id");
		this.version = jsonObject.get("version");
		this.custom_log_file = jsonObject.get("custom_log_file");
		this.custom_note = jsonObject.get("custom_note");
		this.custom_step_results = jsonObject.get("custom_step_results");
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "Result [assignedto_id=" + assignedto_id + ", comment=" + comment + ", created_by=" + created_by
				+ ", created_on=" + created_on + ", defects=" + defects + ", elapsed=" + elapsed + ", id=" + id
				+ ", status_id=" + status_id + ", test_id=" + test_id + ", version=" + version + ", custom_log_file="
				+ custom_log_file + ", custom_note=" + custom_note + ", custom_step_results=" + custom_step_results
				+ "]";
	}

}
