package testrail.refrences;

import org.json.simple.JSONObject;

/**
 * Manage CustomStepResults
 * @author Yehuda Ginsburg
 *
 */
public class CustomStepResults {
	private Object actual, status_id, expected, content;
	private JSONObject jsonObject;

	public CustomStepResults(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}
	
	/**
	 * Initialized Case from the json object 
	 */
	private void initJObj() {
		this.actual = jsonObject.get("actual");
		this.status_id = jsonObject.get("status_id");
		this.expected = jsonObject.get("expected");
		this.content = jsonObject.get("content");
	}

	// Getters / Setters
	
	public Object getActual() {
		return actual;
	}

	public void setActual(Object actual) {
		this.actual = actual;
	}

	public Object getStatus_id() {
		return status_id;
	}

	public void setStatus_id(Object status_id) {
		this.status_id = status_id;
	}

	public Object getExpected() {
		return expected;
	}

	public void setExpected(Object expected) {
		this.expected = expected;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "CustomStepResults [actual=" + actual + ", status_id=" + status_id + ", expected=" + expected
				+ ", content=" + content + ", jsonObject=" + jsonObject + "]";
	}

}
