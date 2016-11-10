package gateway_v2_util;

import java.io.File;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import global.FileUtil;
/**
 * 
 * @author Dana
 *
 */
public class JsonFileReader {
	
	private static String ALLOCATION_JSON = System.getProperty("user.dir")+"\\..\\GatewayV2\\files\\GwFiles\\allocation.json";
	private static String OPERATIONS_JSON = System.getProperty("user.dir")+"\\..\\GatewayV2\\files\\GwFiles\\operations.json";
	private static String DATA_USAGE_JSON = System.getProperty("user.dir")+"\\..\\GatewayV2\\files\\GwFiles\\datausage.json";
	static JsonParser JSON_PARSER = new JsonParser();
	private static String data;	
	private static JsonObject fullJson;
	
	
	/**
	 * Get Create Allocation json file 
	 */
	public static String allocationFile() {
		File file = new File(ALLOCATION_JSON);
		data = FileUtil.readFromFile(file);
		return data;
	}
	
	/**
	 * Get Create Allocation file, change it to contain only mandatory values:
	 * Remove all other values 
	 */
	public static String allocationMandatoryValuesOnly() {
		data = FileUtil.readFromFile(new File(ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		//JsonObject obj = fullJson.getAsJsonObject();
		fullJson.remove("device_status");
		fullJson.remove("m2m");
		JsonObject objNetwork = fullJson.getAsJsonObject("simgo_network_registration");
		objNetwork.remove("method");
		objNetwork.remove("imsi");
		objNetwork.remove("iccid");
		objNetwork.remove("mnc");
		objNetwork.remove("rssi");
		objNetwork.remove("lac");
		objNetwork.remove("cid");
		return fullJson.toString();
	}
	
	/**
	 * Get Create Allocation file, change it to contain only optional values:
	 * Remove all mandatory (required) values 
	 */
	public static String allocationWithoutMandatoryValues() {
		data = FileUtil.readFromFile(new File(ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		JsonObject obj = fullJson.getAsJsonObject("simgo_network_registration");
		fullJson.remove("device_params");
		fullJson.remove("package_version");
		obj.remove("mcc");
		return fullJson.toString();
	}
	
	/**
	 * Get Create Allocation file, change "mcc" and "device_id" values to be invalid 
	 */
	public static String allocationInvalidValues(){
		data = FileUtil.readFromFile(new File(ALLOCATION_JSON));
		fullJson = (JsonObject) JSON_PARSER.parse(data);
		JsonObject objDevice = fullJson.getAsJsonObject("device_params");
		objDevice.addProperty("device_id", "10001");
		JsonObject objNetwork = fullJson.getAsJsonObject("simgo_network_registration");
		objNetwork.addProperty("mcc", 1);
		return fullJson.toString();
	}
	
	/**
	 * Get Create Allocation file with empty package_version value
	 */
	public static String allocationWithEmptyPackageVersion() {
		data = FileUtil.readFromFile(new File(ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "");
		return fullJson.toString();
	}
	
	/**
	 * Get create allocation file with a different package_version (newer or older)
	 */
	public static String allocationWithNewPackageVersion() {
		data = FileUtil.readFromFile(new File(ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "version 3.0.0");
		return fullJson.toString();
	}
	
	/**
	 * Get create allocation file with a package_version that doesn't exist in google bucket
	 */
	public static String allocationNonExistingPackageVersion() {
		data = FileUtil.readFromFile(new File(ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "v0");
		return fullJson.toString();
	}
	

	
	/**
	 * Get operation state json file 
	 */
	public static String operationsFile() {
		File file = new File(OPERATIONS_JSON);
		data = FileUtil.readFromFile(file);
		return data;
	}
	
	/**
	 * Get operation State json file with package_version only
	 * Send json with mandatory values only
	 */
	public static String operationsMandatoryOnlyFile() {
		File file = new File(OPERATIONS_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.remove("component_id");
		fullJson.remove("component_version");
		fullJson.remove("reason");
		fullJson.remove("state");
		return fullJson.toString();
	}
	
	/**
	 * Get operation State json file without package_version, without mandatory values
	 */
	public static String operationsWithoutMandatoryFile() {
		File file = new File(OPERATIONS_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.remove("package_version");
		return fullJson.toString();
	}
	
	/**
	 * Get operation State json file with invalid package_id (doesn't exist in db)
	 */
	public static String operationsInvalidPackageFile() {
		File file = new File(OPERATIONS_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "2.0.0");
		return fullJson.toString();
	}
	
	/**
	 * Get operation State json file with invalid component_id for the specific package
	 */
	public static String fotaStateInvalidComponentIdFile() {
		File file = new File(OPERATIONS_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("component_id", "10");
		return fullJson.toString();
	}
	
	/**
	 * Get operation State json file with invalid component_version for the specific package
	 */
	public static String fotaStateInvalidComponentVersionFile() {
		File file = new File(OPERATIONS_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("component_version", "3.0.0");
		return fullJson.toString();
	}
	
	/**
	 * Get datausage.json file 
	 */
	public static String dataUsageFile() {
		File file = new File(DATA_USAGE_JSON);
		data = FileUtil.readFromFile(file);
		return data;
	}
	
	/**
	 * Get datausage.json file with current timestamp
	 */
	public static String dataUsageTimestampFile() {
		long date = System.currentTimeMillis() / 1000L;
		data = FileUtil.readFromFile(new File(DATA_USAGE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);		
		fullJson.addProperty("timestamp", date);
		return fullJson.toString();
	}
	
	/**
	 * Get datausage.json file with current timestamp
	 */
	public static String dataUsageMandatoryValuesFile() {
		long date = System.currentTimeMillis() / 1000L;
		data = FileUtil.readFromFile(new File(DATA_USAGE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);		
		fullJson.addProperty("timestamp", date);
		fullJson.remove("usages");
		fullJson.add("usages", new JsonArray());
		return fullJson.toString();
	}
	
	/**
	 * Get datausage.json file without the timestamp
	 */
	public static String dataUsageWithoutTimestampFile() {
		data = FileUtil.readFromFile(new File(DATA_USAGE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);		
		fullJson.remove("timestamp");
		return fullJson.toString();
	}
	
	/**
	 * Get datausage.json file with current timestamp, and without "usages"
	 */
	public static String dataUsageWithoutUsagesFile() {
		long date = System.currentTimeMillis() / 1000L;
		data = FileUtil.readFromFile(new File(DATA_USAGE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);	
		fullJson.addProperty("timestamp", date);
		fullJson.remove("usages");
		return fullJson.toString();
	}
	
	/**
	 * Get datausage.json file with current timestamp, and without "imsi"
	 */
	public static String dataUsageWithoutImsiFile() {
		long date = System.currentTimeMillis() / 1000L;
		data = FileUtil.readFromFile(new File(DATA_USAGE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);	
		fullJson.addProperty("timestamp", date);
		JsonArray usages = fullJson.getAsJsonArray("usages");
		JsonObject usageObj = usages.get(0).getAsJsonObject();
		usageObj.remove("imsi");
		return fullJson.toString();
	}
	
	/**
	 * Get datausage.json file with current timestamp, without "mcc" inside "m2m_network"
	 */
	public static String dataUsageWithoutMccFile() {
		long date = System.currentTimeMillis() / 1000L;
		data = FileUtil.readFromFile(new File(DATA_USAGE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);	
		fullJson.addProperty("timestamp", date);
		JsonArray usages = fullJson.getAsJsonArray("usages");			
		JsonObject usageObj = usages.get(0).getAsJsonObject();
		JsonObject m2mNetwork = usageObj.getAsJsonObject("m2m_network");
		m2mNetwork.remove("mcc");
		return fullJson.toString();
	}
}
