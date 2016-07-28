package testrail.refrences;

import org.json.simple.JSONObject;

/**
 * in order add multiple test runs for a particular test suite active at the same time. This
 * can make sense if you want to execute a particular test suite for multiple
 * configurations (such as different operating systems). You can then start a
 * test run for each different configuration you want to test against.
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Configuration {
	private Object group_id, name, id;
	private JSONObject jsonObject;

	public Configuration(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initMap();
	}
	
	/**
	 * Initialized Case from the json object 
	 */
	public void initMap() {
		this.group_id = jsonObject.get("group_id");
		this.name = jsonObject.get("name");
		this.id = jsonObject.get("id");
	}
	
	// Getters / Setters
	
	public Object getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Object group_id) {
		this.group_id = group_id;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
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
		return "Configuration [group_id=" + group_id + ", name=" + name + ", id=" + id + "]";
	}
}