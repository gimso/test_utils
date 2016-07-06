package gateway_util;

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
	
	private static String CREATE_ALLOCATION_JSON = System.getProperty("user.dir")+"\\..\\Tests\\files\\GwFiles\\createAllocation.json";
	private static String AUTHENTICATION_JSON = System.getProperty("user.dir")+"\\..\\Tests\\files\\GwFiles\\authentication.json";
	private static String UPDATE_JSON = System.getProperty("user.dir")+"\\..\\Tests\\files\\GwFiles\\updateallocation.json";
	private static String OUTGOING_CALL_JSON = System.getProperty("user.dir")+"\\..\\Tests\\files\\GwFiles\\outgoingcall.json";
	private static String INCOMING_SMS_JSON = System.getProperty("user.dir")+"\\..\\Tests\\files\\GwFiles\\incomingsms.json";
	private static String FOTA_STATE_JSON = System.getProperty("user.dir")+"\\..\\Tests\\files\\GwFiles\\fotastate.json";
	private static String data;	
	private static JsonObject fullJson;
	static JsonParser JSON_PARSER = new JsonParser();
	
	/**
	 * Get Create Allocation json file 
	 */
	public static String createAllocationFile() {
		File file = new File(CREATE_ALLOCATION_JSON);
		data = FileUtil.readFromFile(file);
		return data;
	}
	
	/**
	 * Get Create Allocation file, change it to contain only mandatory values:
	 * Remove all other values 
	 */
	public static String allocationMandatoryValuesOnly() {
		data = FileUtil.readFromFile(new File(CREATE_ALLOCATION_JSON));
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
		data = FileUtil.readFromFile(new File(CREATE_ALLOCATION_JSON));
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
		data = FileUtil.readFromFile(new File(CREATE_ALLOCATION_JSON));
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
		data = FileUtil.readFromFile(new File(CREATE_ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "");
		return fullJson.toString();
	}
	
	/**
	 * Get create allocation file with a different package_version (newer or older)
	 */
	public static String allocationWithNewPackageVersion() {
		data = FileUtil.readFromFile(new File(CREATE_ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "version 3.0.0");
		return fullJson.toString();
	}
	
	/**
	 * Get create allocation file with a package_version that doesn't exist in google bucket
	 */
	public static String allocationNonExistingPackageVersion() {
		data = FileUtil.readFromFile(new File(CREATE_ALLOCATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "v0");
		return fullJson.toString();
	}
	
	/**
	 * Get Authentication json file 
	 */
	public static String authenticationFile() {
		data = FileUtil.readFromFile(new File(AUTHENTICATION_JSON));
		return data;
	}
	
	/**
	 * Get Authentication json file with authentication empty value
	 */
	public static String authenticationEmptyValue() {
		data = FileUtil.readFromFile(new File(AUTHENTICATION_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("auth_request", "");
		return fullJson.toString();
	}

	/**
	 * Get Outgoing Call json file 
	 */
	public static String outgoingCallFile() {
		File file = new File(OUTGOING_CALL_JSON);
		data = FileUtil.readFromFile(file);
		return data;
	}
	
	/**
	 * Get Outgoing Call json file with outgoing_request empty value
	 */
	public static String outgoingCallEmptyValue() {
		data = FileUtil.readFromFile(new File(OUTGOING_CALL_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("outgoing_request", "");
		return fullJson.toString();
	}
	
	/**
	 * Get Outgoing Call json file with outgoing_request invalid value
	 */
	public static String outgoingCallInvalidValue() {
		data = FileUtil.readFromFile(new File(OUTGOING_CALL_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("outgoing_request", "+9725443");
		return fullJson.toString();
	}
	
	/**
	 * Get the Outgoing Call number from the json file
	 */
	public static String outgoingCallGetNumberValue() {
		data = FileUtil.readFromFile(new File(OUTGOING_CALL_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		return fullJson.get("outgoing_request").toString();
	}
		
	/**
	 * Get Update json file 
	 */
	public static String updateAllocationFile() {
	 data = FileUtil.readFromFile(new File(UPDATE_JSON));
	 return data;
	}

	/**
	 * Get Update json file without mandatory values
	 */
	 public static String updateAllocationWithoutMandatoryValues() {
	 data = FileUtil.readFromFile(new File(UPDATE_JSON));
	 fullJson =  (JsonObject) JSON_PARSER.parse(data);
	 fullJson.remove("device_params");
	 return fullJson.toString();
	 }
	 
	 /**
	 * Get Update json file without package_version
	 */
	 public static String updateAllocationWithoutPackage() {
	 data = FileUtil.readFromFile(new File(UPDATE_JSON));
	 fullJson =  (JsonObject) JSON_PARSER.parse(data);
	 fullJson.remove("package_version");
	 return fullJson.toString();
	 }
	 
	 /**
	 * Get update allocation file with a different package_version (newer or older)
	 */
	public static String updateWithNewPackageVersion() {
	data = FileUtil.readFromFile(new File(UPDATE_JSON));
	fullJson =  (JsonObject) JSON_PARSER.parse(data);
	fullJson.addProperty("package_version", "1.0.7");
	return fullJson.toString();
	}
	
	 /**
	 * Get update allocation file with a non-existing package_version (in google cloud)
	 */
	public static String updateWithInvalidPackageVersion() {
	data = FileUtil.readFromFile(new File(UPDATE_JSON));
	fullJson =  (JsonObject) JSON_PARSER.parse(data);
	fullJson.addProperty("package_version", "Invalid Version");
	return fullJson.toString();
	}
	 
	 /**
	 * Get Update json file without mcc (send simgo_network_registration without mcc)
	 */
	 public static String updateAllocationWithoutMcc() {
	 data = FileUtil.readFromFile(new File(UPDATE_JSON));
	 fullJson =  (JsonObject) JSON_PARSER.parse(data);
	 JsonObject obj = fullJson.getAsJsonObject("simgo_network_registration");
	 obj.remove("mcc");
	 return fullJson.toString();
	 }
	 
	 /**
	 * Get Update json file without simgo_network_registration
	 */
	 public static String updateAllocationWithoutSimgoNetwork() {
	 data = FileUtil.readFromFile(new File(UPDATE_JSON));
	 fullJson =  (JsonObject) JSON_PARSER.parse(data);
	 fullJson.remove("simgo_network_registration");
	 return fullJson.toString();
	 }
	 
	 /**
	 * Get updateallocation file, change it to contain only mandatory values.
	 * Remove all other values 
	 */
	 public static String updateAllocationMandatoryValuesOnly() {
		data = FileUtil.readFromFile(new File(UPDATE_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.remove("device_status");
		fullJson.remove("sim_status");
		fullJson.remove("timestamp");
		fullJson.remove("last_event");
		fullJson.remove("sim_allocation_request");
		fullJson.remove("package_version");
		JsonObject network = fullJson.getAsJsonObject("simgo_network_registration");
		network.remove("method");
		network.remove("imsi");
		network.remove("iccid");
		network.remove("mnc");
		network.remove("rssi");
		network.remove("lac");
		network.remove("cid");
		fullJson.remove("m2m");
		return fullJson.toString();
	}
	 
	 /**
	 * Get Update json file with changed mcc
	 */
	 public static String updateAllocationMcc() {
	 data = FileUtil.readFromFile(new File(UPDATE_JSON));
	 fullJson =  (JsonObject) JSON_PARSER.parse(data);
	 JsonObject obj = fullJson.getAsJsonObject("simgo_network_registration");
	 obj.addProperty("mcc", 310);
	 return fullJson.toString();
	 }
	 
	 /**
	 * Get Incoming SMS json file with current timestamp
	 */
	public static String incomingSmsFile() {
		long date = System.currentTimeMillis();
		data = FileUtil.readFromFile(new File(INCOMING_SMS_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		JsonArray smsArray = fullJson.getAsJsonArray("sms");
		JsonObject obj = smsArray.get(0).getAsJsonObject();
		obj.addProperty("send_time", date);
		obj.addProperty("received_time", date);
		return fullJson.toString();
	}
	
	 /**
	 * Get Incoming SMS json file with current timestamp, and without device id
	 */
	public static String incomingSmsWithoutDeviceFile() {
		long date = System.currentTimeMillis();
		data = FileUtil.readFromFile(new File(INCOMING_SMS_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		JsonArray smsArray = fullJson.getAsJsonArray("sms");
		JsonObject smsObj = smsArray.get(0).getAsJsonObject();
		smsObj.addProperty("send_time", date);
		smsObj.addProperty("received_time", date);
		JsonObject obj = fullJson.getAsJsonObject();
		obj.remove("device_id");
		return fullJson.toString();
	}
	
	/**
	 * Get Incoming SMS json file with current timestamp, and with invalid device id
	 */
	public static String incomingSmsInvalidDeviceIdFile() {
		long date = System.currentTimeMillis();
		data = FileUtil.readFromFile(new File(INCOMING_SMS_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		JsonArray smsArray = fullJson.getAsJsonArray("sms");
		JsonObject smsObj = smsArray.get(0).getAsJsonObject();
		smsObj.addProperty("send_time", date);
		smsObj.addProperty("received_time", date);
		JsonObject obj = fullJson.getAsJsonObject();
		obj.addProperty("device_id", "0000100");
		return fullJson.toString();
	}
	
	/**
	 * Get Incoming SMS json file with current timestamp, and with invalid hsim_number
	 */
	public static String incomingSmsInvalidHsimNumFile() {
		long date = System.currentTimeMillis();
		data = FileUtil.readFromFile(new File(INCOMING_SMS_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		JsonArray smsArray = fullJson.getAsJsonArray("sms");
		JsonObject smsObj = smsArray.get(0).getAsJsonObject();
		smsObj.addProperty("send_time", date);
		smsObj.addProperty("received_time", date);
		JsonObject obj = fullJson.getAsJsonObject();
		obj.addProperty("hsim_number", "+9725512");
		return fullJson.toString();
	}
	
	
	/**
	 * Get Incoming SMS json file with duplicate sms object arrays (send 2 messages within the json)
	 */
	public static String incomingSmsMultipleMessages() {
		long date = System.currentTimeMillis();
		data = FileUtil.readFromFile(new File(INCOMING_SMS_JSON));
		fullJson =  (JsonObject) JSON_PARSER.parse(data);	
		//get the array and add sms object to it
		JsonArray smsArray = fullJson.getAsJsonArray("sms");
		smsArray.addAll(smsArray);
		//change values
		JsonObject smsObj = smsArray.get(0).getAsJsonObject();		
		smsObj.addProperty("send_time", date);
		smsObj.addProperty("received_time", date);
		smsObj.addProperty("content", "first");
		return fullJson.toString();
	}
	
	/**
	 * Get Fota state json file 
	 */
	public static String fotaStateFile() {
		File file = new File(FOTA_STATE_JSON);
		data = FileUtil.readFromFile(file);
		return data;
	}
	
	/**
	 * Get Fota State json file with package_version only
	 * Send json with mandatory values only
	 */
	public static String fotaStateMandatoryOnlyFile() {
		File file = new File(FOTA_STATE_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.remove("component_id");
		fullJson.remove("component_version");
		fullJson.remove("reason");
		fullJson.remove("state");
		return fullJson.toString();
	}
	
	/**
	 * Get Fota State json file without package_version, without mandatory values
	 */
	public static String fotaStateWithoutMandatoryFile() {
		File file = new File(FOTA_STATE_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.remove("package_version");
		return fullJson.toString();
	}
	
	/**
	 * Get Fota State json file with invalid package_id (doesn't exist in db)
	 */
	public static String fotaStateInvalidPackageFile() {
		File file = new File(FOTA_STATE_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("package_version", "2.0.0");
		return fullJson.toString();
	}
	
	/**
	 * Get Fota State json file with invalid component_id for the specific package
	 */
	public static String fotaStateInvalidComponentIdFile() {
		File file = new File(FOTA_STATE_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("component_id", "10");
		return fullJson.toString();
	}
	
	/**
	 * Get Fota State json file with invalid component_version for the specific package
	 */
	public static String fotaStateInvalidComponentVersionFile() {
		File file = new File(FOTA_STATE_JSON);
		data = FileUtil.readFromFile(file);
		fullJson =  (JsonObject) JSON_PARSER.parse(data);
		fullJson.addProperty("component_version", "3.0.0");
		return fullJson.toString();
	}
}
