package testrail.api;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import global.PropertiesUtil;
import testrail.refrences.Plan;
import testrail.refrences.Project;
/**
 * Utility for initialized testrail client instances and send post/get toward testrail server
 * @author Yehuda Ginsburg
 *
 */
public class TestRailUtil {
	
	private APIClient client;
	private Project project;
	private Plan plan;

	// using Singleton
	private static TestRailUtil instance = null;
	
	public static TestRailUtil getInstance() {
		if (instance == null) {
			instance = new TestRailUtil();
		}
		return instance;
	}
	
	// Initialized the APIClient
	private TestRailUtil() {
		String user = PropertiesUtil.getInstance().getProperty("TEST_RAIL_USER_EMAIL");
		String password =  PropertiesUtil.getInstance().getProperty("TEST_RAIL_USER_PASSOWRD");
		this.client = new APIClient(user, password);
	}
	
	/**
	 * Using sendGetCommand key and value extracting jspn object
	 * @param sendGetCommand
	 * @param key
	 * @param value
	 * @return Map<String, Object>
	 */
	public  JSONObject getJsonObjAsMapByKeyValue(String sendGetCommand, String key, String value) {
		JSONArray jsonArray = sendGetJArray(sendGetCommand);
		return getJsonObjFromJsonArrayByKeyValue(jsonArray, key, value);
	}
	
	/**
	 * Send Get Issues a GET request 
	 * use this method when expecting JSONObject in return
	 *  
	 * @param sendGetCommand
	 * @return JSONObject
	 */
	public JSONObject sendGetJObj(String sendGetCommand) {
		try {
			return (JSONObject) client.sendGet(sendGetCommand);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Send POST Issues a POST request 
	 * use this method when expecting JSONObject in return
	 *  
	 * @param sendGetCommand
	 * @return JSONObject
	 */
	public JSONObject sendPost(String sendGetCommand, JSONObject jsonObject) {
		try {
			return (JSONObject) client.sendPost(sendGetCommand, jsonObject);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Send Get Issues a GET request 
	 * use this method when expecting JSONArray in return
	 *  
	 * @param sendGetCommand
	 * @return JSONArray
	 */
	public JSONArray sendGetJArray(String sendGetCommand) {
		try {
			return (JSONArray) client.sendGet(sendGetCommand);
		} catch (IOException | APIException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get the JSONObject from JSONArray by key and value
	 * @param jsonArray
	 * @param key
	 * @param value
	 * @return JSONObject
	 */
	private JSONObject getJsonObjFromJsonArrayByKeyValue(JSONArray jsonArray, String key, String value) {
		for (Object obj : jsonArray) {
			JSONObject temp = (JSONObject) obj;
			if (temp.containsKey(key) && temp.get(key).equals(value))
				return temp;
		}
		System.err.println("Cannot find " + key + " with the value " + value + " in " + jsonArray);
		return null;
	}
	
	/**
	 * Get project from default if not already created
	 * @return Project
	 */
	public Project getProject() {
		if(project==null)
			this.project = new Project(Long.valueOf(PropertiesUtil.getInstance().getProperty("TEST_RAIL_PROJECT_ID")));
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


	public APIClient getClient() {
		return client;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

}
