package testrail.refrences;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import global.JsonUtil;

/**
 * Manage Entry for Plan
 * @author Yehuda Ginsburg
 *
 */
public class Entry {
	
	private Object name, assignedto_id, id, suite_id, include_all;
	private List<Run> runs;

	private JSONObject jsonObject;

	public Entry(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initMap();
	}
	
	/**
	 * CTOR 
	 * @param suiteId
	 * @param name
	 * @param assignedtoId
	 * @param includeAll
	 * @param runs
	 */
	@SuppressWarnings("unchecked")
	public Entry(Long suiteId, String name, Long assignedtoId, Boolean includeAll, List<Run> runs) {
		this.jsonObject = new JSONObject();
		if (name != null)
			jsonObject.put("name", name);
		if (suiteId != null)
			jsonObject.put("suite_id", suiteId);
		if (assignedtoId != null)
			jsonObject.put("assignedto_id", assignedtoId);
		if (includeAll != null)
			jsonObject.put("include_all", includeAll);
		if (runs != null) {
			JSONArray array = new JSONArray();
			for (Run r : runs)
				array.add(r.getJsonObject());
			jsonObject.put("runs", array);
		}
		this.jsonObject = JsonUtil.mapToJsonObj(jsonObject);
	}
	
	/**
	 * 
	 * @param suite_id
	 * @param runs
	 * @param include_all
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Entry (Long suite_id, List<Run> runs, boolean include_all) {
		this.jsonObject = new JSONObject();		
		jsonObject.put("suite_id", suite_id);
		JSONArray runsRv = new JSONArray();
		for (Run run : runs)
			runsRv.add(run.getJsonObject());
		jsonObject.put("runs", runsRv);
		jsonObject.put("include_all", true);
		
		this.jsonObject = JsonUtil.mapToJsonObj(jsonObject);
	}
	
	/**
	 * CTOR from suite id, include all true
	 * @param suite
	 */
	@SuppressWarnings("unchecked")
	public Entry(Suite suite) {
		this.suite_id = (Long) suite.getId();
		this.jsonObject = new JSONObject();
		this.jsonObject.put("suite_id", this.suite_id);
		this.jsonObject.put("include_all", true);
		this.jsonObject = JsonUtil.mapToJsonObj(jsonObject);
	}
	
	/**
	 * CTOR from suite and configGroup
	 * @param suite
	 * @param configurationGroup
	 */
	@SuppressWarnings("unchecked")
	public Entry(Suite suite, ConfigurationGroup configurationGroup) {
		this.suite_id = (Long) suite.getId();
		this.jsonObject = new JSONObject();
		this.jsonObject.put("suite_id", this.suite_id);
		this.jsonObject.put("include_all", true);
		JSONArray configs = new JSONArray();
		for (Configuration configuration : configurationGroup.getConfigs())
			configs.add(configuration.getId());
		this.jsonObject.put("config_ids", configs);
		this.jsonObject.put("runs", addRunsByConfig(configurationGroup));
		this.jsonObject = JsonUtil.mapToJsonObj(jsonObject);
	}
	
	

	/**
	 * Each Entry contains Runs, each Runs contains Run, each Run contains
	 * ConfigId's array each configId contain one config Id
	 * 
	 * This method run over the ConfigurationGroups each of them contains one or
	 * more Configuration every config should include all the other group id's
	 * beside what we already have.
	 * 
	 * @param configurationGroup
	 * @return JSONArray "runs"
	 */
	@SuppressWarnings("unchecked")
	private JSONArray addRunsByConfig(ConfigurationGroup configurationGroup) {
		JSONArray runs = new JSONArray();
		for (Configuration config : configurationGroup.getConfigs()) {
			JSONObject run = new JSONObject();
			JSONArray config_ids = new JSONArray();
			config_ids.add(config.getId());
			run.put("config_ids", config_ids);
			runs.add(run);
		}
		return runs.isEmpty() ? null : runs;
	}

	
	/**
	 * each Entry contains Runs, each Runs contains Run, each Run contains
	 * ConfigId's array each configId contain 2 config Id's
	 * 
	 * This method run over the ConfigurationGroups each of them contains one or
	 * more Configuration every config should include all the other group id's
	 * beside what we already have.
	 * 
	 * @param configurationGroup1
	 * @param configurationGroup2
	 * @return JSONArray "runs"
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private JSONArray addRunsByConfig(ConfigurationGroup configurationGroup1, ConfigurationGroup configurationGroup2) {
		JSONArray runs = new JSONArray();
		for (Configuration config : configurationGroup1.getConfigs()) {
			for (Configuration innerConfig : configurationGroup2.getConfigs()) {
				JSONObject run = new JSONObject();
				JSONArray config_ids = new JSONArray();
				config_ids.add(config.getId());
				config_ids.add(innerConfig.getId());
				run.put("config_ids", config_ids);
				runs.add(run);
			}
		}
		return runs;
	}

	public void initMap() {
		this.name = jsonObject.get("name");
		this.id = jsonObject.get("id");
		this.setAssignedto_id((Long) jsonObject.get("assignedto_id"));
		this.setInclude_all((Boolean) jsonObject.get("include_all"));
		this.suite_id = (Long) jsonObject.get("suite_id");
		if (jsonObject.get("configs") != null) {
			this.runs = new ArrayList<>();
			for (Object o : (JSONArray) jsonObject.get("configs")) {
				this.runs.add(new Run((JSONObject) o));
			}
		}
	}
	
	/**
	 * get list of run  from entry
	 * @return List Runs
	 */
	public List<Run> getRuns() {
		if (this.runs == null) {
			this.runs = new ArrayList<>();
			JSONArray jsonArray = (JSONArray) this.jsonObject.get("runs");
			for (Object jsonObject : jsonArray)
				if (jsonObject instanceof JSONObject)
					this.runs.add(new Run((JSONObject) jsonObject));
		}
		return runs;
	}
	
	// Getters / Setters

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Object getAssignedto_id() {
		return assignedto_id;
	}

	public void setAssignedto_id(Object assignedto_id) {
		this.assignedto_id = assignedto_id;
	}

	public Object getInclude_all() {
		return include_all;
	}

	public void setInclude_all(Object include_all) {
		this.include_all = include_all;
	}

	@Override
	public String toString() {
		return "Entry [name=" + name + ", id=" + id + ", suite_id=" + suite_id + ", runs=" + runs
				+ ", jsonObject=" + jsonObject + "]";
	}

}
