package testrail.refrences;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;

public class Suite {
	private static final String GET_SUITE = "get_suite/";

	private Object is_master, project_id, is_baseline, completed_on, name, description, id, is_completed, url;
	private List<Case> cases;

	private JSONObject jsonObject;
	private TestRailUtil testRailUtil = TestRailUtil.getInstance();

	public Suite(long id) {
		this.jsonObject = testRailUtil.sendGetJObj(GET_SUITE + id);
		initJObj();
	}

	public Suite(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}

	public void initJObj() {
		this.is_master = jsonObject.get("is_master");
		this.project_id = jsonObject.get("project_id");
		this.is_baseline = jsonObject.get("is_baseline");
		this.completed_on = jsonObject.get("completed_on");
		this.name = jsonObject.get("name");
		this.description = jsonObject.get("description");
		this.id = jsonObject.get("id");
		this.is_completed = jsonObject.get("is_completed");
		this.url = jsonObject.get("url");
	}

	public Object getIs_master() {
		return is_master;
	}

	public void setIs_master(Object is_master) {
		this.is_master = is_master;
	}

	public Object getProject_id() {
		return project_id;
	}

	public void setProject_id(Object project_id) {
		this.project_id = project_id;
	}

	public Object getIs_baseline() {
		return is_baseline;
	}

	public void setIs_baseline(Object is_baseline) {
		this.is_baseline = is_baseline;
	}

	public Object getCompleted_on() {
		return completed_on;
	}

	public void setCompleted_on(Object completed_on) {
		this.completed_on = completed_on;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(Object is_completed) {
		this.is_completed = is_completed;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}

	public List<Case> getCases() {
		if (cases == null || cases.isEmpty()) {
			cases = new ArrayList<>();
			String sendGetCommand = "get_cases/" + this.project_id + "/&suite_id=" + this.id;
			JSONArray array = testRailUtil.sendGetJArray(sendGetCommand);
			for (Object o : array)
				if (o instanceof JSONObject)
					cases.add(new Case((JSONObject) o));
			return cases;
		}
		return cases;
	}

	public void setCases(List<Case> cases) {
		this.cases = cases;
	}

	public Case getCase(long id) {
		for (Case caseObj : getCases())
			if (caseObj.getId().equals(id))
				return caseObj;
		return null;
	}

	public Case getCase(String title) {
		for (Case caseObj : getCases())
			if (caseObj.getTitle().equals(title))
				return caseObj;
		return null;
	}

	@Override
	public String toString() {
		return "Suite [is_master=" + is_master + ", project_id=" + project_id + ", is_baseline=" + is_baseline
				+ ", completed_on=" + completed_on + ", name=" + name + ", description=" + description + ", id=" + id
				+ ", is_completed=" + is_completed + ", url=" + url + ", map=" + jsonObject + "]";
	}
	
}
