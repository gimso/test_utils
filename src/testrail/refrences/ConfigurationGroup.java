package testrail.refrences;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import global.JsonUtil;

public class ConfigurationGroup {

	private Object id, name, project_id;
	private List<Configuration> configs;

	private JSONObject jsonObject;

	public static ConfigurationGroup getConfigurationGroup(String name, Project project) {
		for (ConfigurationGroup configurationGroup : project.getConfigurationGroups())
			if (configurationGroup.getName().equals(name))
				return configurationGroup;
		return null;
	}

	public static ConfigurationGroup getConfigurationGroup(long id, Project project) {
		for (ConfigurationGroup configurationGroup : project.getConfigurationGroups())
			if (configurationGroup.getId().equals(id))
				return configurationGroup;
		return null;
	}

	public ConfigurationGroup(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		initJObj();
	}

	@SuppressWarnings("unchecked")
	public ConfigurationGroup(Long id, String name, Long project_id, List<Configuration> configs) {
		this.id = id;
		this.name = name;
		this.project_id = project_id;
		this.configs = configs;

		this.jsonObject = new JSONObject();
		this.jsonObject.put("id", id);
		this.jsonObject.put("name", name);
		this.jsonObject.put("project_id", project_id);
		this.jsonObject.put("configs", JsonUtil.listToJsonArray(configs));
	}

	public static ConfigurationGroup getConfigurationGroup(Project project, Long configId) {
		for (ConfigurationGroup configurationGroup : project.getConfigurationGroups())
			if (configurationGroup.getId().equals(configId))
				return configurationGroup;
		return null;
	}

	private void initJObj() {
		this.id = jsonObject.get("id");
		this.name = jsonObject.get("name");
		this.project_id = jsonObject.get("project_id");
		this.configs = new ArrayList<>();
		for (Object o : (JSONArray) jsonObject.get("configs"))
			this.configs.add(new Configuration((JSONObject) o));
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public Object getProject_id() {
		return project_id;
	}

	public void setProject_id(Object project_id) {
		this.project_id = project_id;
	}

	public List<Configuration> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Configuration> configs) {
		this.configs = configs;
	}

	public Configuration getConfiguration(String name) {
		for (Configuration configuration : getConfigs())
			if (configuration.getName().equals(name))
				return configuration;
		return null;
	}

	public Configuration getConfiguration(long id) {
		for (Configuration configuration : getConfigs())
			if (configuration.getId().equals(id))
				return configuration;
		return null;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "ConfigurationGroup [id=" + id + ", name=" + name + ", project_id=" + project_id + ", configs=" + configs
				+ "]";
	}

}
