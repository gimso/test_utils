package testrail.refrences;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;

public class Project {
	private static final String GET_SUITES = "get_suites/";
	private static final String GET_CONFIGS = "get_configs/";
	private static final String GET_PROJECT = "get_project/";
	private static final String GET_PROJECTS = "get_projects";
	private static final String GET_PLANS = "get_plans/";
	private static final String GET_RUNS = "get_runד/";

	private Object suite_mode, completed_on, show_announcement, is_completed, url, announcement;
	private String name;
	private Long id;
	private List<Suite> suites;
	private List<ConfigurationGroup> configurationGroups;
	private List<Plan> plans;
	private List<Run> runs;

	private JSONObject jsonObject;

	private TestRailUtil testRailUtil = TestRailUtil.getInstance();

	public Project(String name) {
		this.jsonObject = testRailUtil.getJsonObjAsMapByKeyValue(GET_PROJECTS, "name", name);
		initJObj();
	}

	public Project(long id) {
		this.jsonObject = testRailUtil.sendGetJObj(GET_PROJECT + id);
		initJObj();
	}

	public Project(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}

	/**
	 * Initialized the bean obj
	 * 
	 * @param map
	 */
	private void initJObj() {
		this.suite_mode = jsonObject.get("suite_mode");
		this.completed_on = jsonObject.get("completed_on");
		this.name = (String) jsonObject.get("name");
		this.id = (Long) jsonObject.get("id");
		this.show_announcement = jsonObject.get("show_announcement");
		this.is_completed = jsonObject.get("is_completed");
		this.url = jsonObject.get("url");
		this.announcement = jsonObject.get("announcement");
		testRailUtil.setProject(this);
	}

	public List<Plan> getPlans() {
		if (plans == null || plans.isEmpty()) {
			plans = new ArrayList<>();
			String sendGetCommand = GET_PLANS + this.id;
			JSONArray array = testRailUtil.sendGetJArray(sendGetCommand);
			for (Object o : array)
				if (o instanceof JSONObject)
					plans.add(new Plan((JSONObject) o));
			return plans;
		}
		return plans;
	}

	public void setPlans(List<Plan> plans) {
		this.plans = plans;
	}

	public List<Suite> getSuites() {
		if (suites == null || suites.isEmpty()) {
			suites = new ArrayList<>();
			String sendGetCommand = GET_SUITES + this.id;
			JSONArray array = testRailUtil.sendGetJArray(sendGetCommand);
			for (Object o : array)
				if (o instanceof JSONObject)
					suites.add(new Suite((JSONObject) o));
			return suites;
		}
		return suites;
	}

	public void setSuites(List<Suite> suites) {
		this.suites = suites;
	}

	public List<ConfigurationGroup> getConfigurationGroups() {
		if (configurationGroups == null || configurationGroups.isEmpty()) {
			configurationGroups = new ArrayList<>();
			String sendGetCommand = GET_CONFIGS + this.id;
			JSONArray array = testRailUtil.sendGetJArray(sendGetCommand);
			for (Object o : array)
				if (o instanceof JSONObject)
					configurationGroups.add(new ConfigurationGroup((JSONObject) o));
			return configurationGroups;
		}
		return configurationGroups;
	}

	public void setConfigurationGroups(List<ConfigurationGroup> configs) {
		this.configurationGroups = configs;
	}

	public List<Run> getRuns() {
		if (this.runs == null || this.runs.isEmpty()) {
			this.runs = new ArrayList<>();
			String sendGetCommand = GET_RUNS + this.id;
			JSONArray tempArray = this.testRailUtil.sendGetJArray(sendGetCommand);
			for (Object object : tempArray)
				if (object instanceof JSONObject)
					runs.add(new Run((JSONObject) object));
			return this.runs;
		}
		return this.runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	// Getters & Setters

	public Object getSuite_mode() {
		return suite_mode;
	}

	public void setSuite_mode(Object suite_mode) {
		this.suite_mode = suite_mode;
	}

	public Object getCompleted_on() {
		return completed_on;
	}

	public void setCompleted_on(Object completed_on) {
		this.completed_on = completed_on;
	}

	public Object getShow_announcement() {
		return show_announcement;
	}

	public void setShow_announcement(Object show_announcement) {
		this.show_announcement = show_announcement;
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

	public Object getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Object announcement) {
		this.announcement = announcement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Project [suite_mode=" + suite_mode + ", completed_on=" + completed_on + ", name=" + name + ", id=" + id
				+ ", show_announcement=" + show_announcement + ", is_completed=" + is_completed + ", url=" + url
				+ ", announcement=" + announcement + "]";
	}

}
