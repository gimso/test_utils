package global;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import beans.PhoneType;
import beans.PhoneTypeData;
import beans.TestData;

/**
 * Parse a test_data.json and properties.json JSON files into beans or Strings
 * 
 * @author Yehuda Ginsburg
 */
public class JSONReader {

	private static final String PLUG_COM_PORT_ID 				= 		"PLUG_COM_PORT_ID";
	private static final String OPERATING_SYSTEM 				= 		"OPERATING_SYSTEM";
	private static final String DEVICE_SERIAL_NUMBER 			= 		"DEVICE_SERIAL_NUMBER";
	private static final String DEVICE_MODEL 					= 		"DEVICE_MODEL";
	private static final String PLUG_ID 						= 		"PLUG_ID";
	private static final String HOME_NUMBER 					= 		"HOME_NUMBER";
	private static final String TEST_PLAN 						= 		"TEST_PLAN";
	private static final String OUTGOING_ACCESS_NUMBER 			= 		"OUTGOING_ACCESS_NUMBER";
	private static final String ACCESS_NUMBER_GROUP 			= 		"ACCESS_NUMBER_GROUP";
	private static final String USER_GROUP_ID 					= 		"USER_GROUP_ID";
	private static final String USER_NAME 						= 		"USER_NAME";
	private static final String DIALED_DIGITS 					= 		"DIALED_DIGITS";
	private static final String INCOMING_ACCESS_NUMBER 			= 		"INCOMING_ACCESS_NUMBER";
	private static final String DEVICE_NAME 					= 		"DEVICE_NAME";

	public static final String DEFAULT_JSON_PATH 				= 		"files/test_data.json";
	public static final String DEFAULT_PROPERTIES_PATH 			= 		"files/properties.json";

	
	
	// Keys are used in Simgo App Automation Tests in a separate Json File <phone_type_details.json>
	private static final String key_Device_Patform		 		= 		 "Platform";
	private static final String key_Device_Model		 		= 		 "Model";
	private static final String key_Device_Version		 		= 		 "Version";
	private static final String key_Device_UDID		 			= 		 "DeviceUDID";
	
	
	/**
	 * get phone types, test data keys and dialed digits list convert them into
	 * PhoneType beans
	 * 
	 * @param path
	 * @return List<PhoneType>
	 */
	public static List<PhoneType> phoneTypes(String path) {
		try {
			List<PhoneType> phoneTypes = new ArrayList<PhoneType>();

			String jsonString = fromJsonFileToString(path);
			JSONObject json = new JSONObject(jsonString);

			List<String> phoneTypeKeys = getJsonKeys(json);

			for (String phone : phoneTypeKeys) {
				JSONObject phoneTypeJson = (JSONObject) json.get(phone);
				List<String> dialedDigits = getDialedDigits(phoneTypeJson);
				TestData testData = getTestData(phoneTypeJson, dialedDigits);
				PhoneType type = new PhoneType(phone, testData);
				phoneTypes.add(type);
			}
			return phoneTypes;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get phone types, test data keys and dialed digits list convert them into
	 * PhoneType beans
	 * 
	 * @param path
	 * @return List<PhoneType>
	 */
	public static List<PhoneType> phoneTypes() {
		try {
			List<PhoneType> phoneTypes = new ArrayList<PhoneType>();

			String jsonString = fromJsonFileToString(DEFAULT_JSON_PATH);
			JSONObject json = new JSONObject(jsonString);

			List<String> phoneTypeKeys = getJsonKeys(json);

			for (String phone : phoneTypeKeys) {
				JSONObject phoneTypeJson = (JSONObject) json.get(phone);
				List<String> dialedDigits = getDialedDigits(phoneTypeJson);
				TestData testData = getTestData(phoneTypeJson, dialedDigits);
				PhoneType type = new PhoneType(phone, testData);
				phoneTypes.add(type);
			}
			return phoneTypes;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * get from JSONObject file all the keys include in it
	 * 
	 * @param json
	 * @return
	 */
	public static List<String> getJsonKeys(JSONObject json) {
		List<String> phoneTypeKeys = new ArrayList<String>();
		Iterator<?> keys = json.keys();
		while (keys.hasNext()) {
			phoneTypeKeys.add((String) keys.next());
		}
		return phoneTypeKeys;
	}

	/**
	 * received a phone type json object and dialed digit list and convert into
	 * TestData bean
	 * 
	 * @param phoneType
	 * @param dialedDigits
	 * @return TestData bean
	 * @throws JSONException
	 */
	private static TestData getTestData(JSONObject phoneType,
			List<String> dialedDigits) throws JSONException {
		String userName = (String) phoneType.get(USER_NAME);
		String userGroupId = (String) phoneType.get(USER_GROUP_ID);
		String accessNumberGroup = (String) phoneType.get(ACCESS_NUMBER_GROUP);
		String testPlan = (String) phoneType.get(TEST_PLAN);
		String homeNumber = (String) phoneType.get(HOME_NUMBER);
		String plugId = (String) phoneType.get(PLUG_ID);
		String outgoingAccessNumber = (String) phoneType
				.get(OUTGOING_ACCESS_NUMBER);
		String incomingAccessNumber = (String) phoneType
				.get(INCOMING_ACCESS_NUMBER);
		String operatingSystem = (String) phoneType.get(OPERATING_SYSTEM);
		String plugComPortId = (String) phoneType.get(PLUG_COM_PORT_ID);
		String deviceModel = (String) phoneType.get(DEVICE_MODEL);
		String deviceSN = (String) phoneType.get(DEVICE_SERIAL_NUMBER);
		String deviceName = (String) phoneType.get(DEVICE_NAME);
		TestData testData = new TestData(userName, userGroupId,
				accessNumberGroup, outgoingAccessNumber, testPlan, homeNumber,
				plugId, incomingAccessNumber, dialedDigits, operatingSystem,
				plugComPortId, deviceModel, deviceSN, deviceName);
		return testData;
	}

	/**
	 * received phone type json object and convert it to list of dialed digits
	 * string numbers
	 * 
	 * @param phoneType
	 * @return List<String>
	 * @throws JSONException
	 */
	private static List<String> getDialedDigits(JSONObject phoneType)
			throws JSONException {
		JSONArray dialedDigits = (JSONArray) phoneType.get(DIALED_DIGITS);
		List<String> digits = new ArrayList<String>();
		if (dialedDigits != null) {
			int len = dialedDigits.length();
			for (int i = 0; i < len; i++) {
				digits.add(dialedDigits.get(i).toString());
			}
		}
		return digits;
	}

	/**
	 * if inserting to 'test_data.json' a new value that didn't parse yet as a
	 * part of PhoneType keys , still can just insert the path of the json and
	 * the key, and get it
	 * 
	 * @param path
	 * @param name
	 * @return a value of property key
	 */
	public static PhoneType getTestDataPhoneTypeByName(String path, String key) {
		try {
			String jsonString = fromJsonFileToString(path);
			JSONObject json = new JSONObject(jsonString);
			JSONObject phoneTypeJson = (JSONObject) json.get(key);
			List<String> dialedDigits = getDialedDigits(phoneTypeJson);
			TestData testData = getTestData(phoneTypeJson, dialedDigits);
			if (key != null) {
				return new PhoneType(key, testData);
			}
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 
	 * Received a json path and convert into a Map(phone type string,
	 * Map(test_data key, test_data value))
	 * 
	 * @param path
	 * @return Map of String, and inner Map of String and Object from
	 *         test_data.json
	 */
	public static Map<String, Map<String, Object>> fromTestDataJsonToMap(
			String path) {
		// get json as String from json file
		try {
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			String jsonString = fromJsonFileToString(path);
			JSONObject jsonObject = new JSONObject(jsonString);
			Iterator<?> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String jsonObjectKey = (String) keys.next();
				JSONObject jsonObjectChild = (JSONObject) jsonObject
						.get(jsonObjectKey);
				Map<String, Object> innerMap = new HashMap<String, Object>();

				List<String> testDataKeys = getJsonKeys(jsonObjectChild);

				for (String key : testDataKeys) {
					Object value = jsonObjectChild.get(key);
					innerMap.put(key, value);
				}
				map.put(jsonObjectKey, innerMap);
				jsonObject.keys().next();
			}
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * utility to convert from Map of test data key and value to TestData bean
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TestData fromMapToTestData(Map<String, Object> map) {
		String userName = (String) map.get(USER_NAME);
		String userGroupId = (String) map.get(USER_GROUP_ID);
		String accessNumberGroup = (String) map.get(ACCESS_NUMBER_GROUP);
		String testPlan = (String) map.get(TEST_PLAN);
		String homeNumber = (String) map.get(HOME_NUMBER);
		String plugId = (String) map.get(PLUG_ID);

		String incomingAccessNumber = (String) map.get(INCOMING_ACCESS_NUMBER);
		List<String> dialedDigits = (List<String>) map.get(DIALED_DIGITS);
		String outgoingAccessNumber = (String) map.get(OUTGOING_ACCESS_NUMBER);
		String operatingSystem = (String) map.get(OPERATING_SYSTEM);
		String plugComPortId = (String) map.get(PLUG_COM_PORT_ID);
		String deviceSN = (String) map.get(DEVICE_SERIAL_NUMBER);
		String deviceModel = (String) map.get(DEVICE_MODEL);
		String deviceName = (String) map.get(DEVICE_NAME);

		TestData testDataFromJson = new TestData(userName, userGroupId,
				accessNumberGroup, outgoingAccessNumber, testPlan, homeNumber,
				plugId, incomingAccessNumber, dialedDigits, operatingSystem,
				plugComPortId, deviceModel, deviceSN, deviceName);
		return testDataFromJson;
	}

	/**
	 * Received a path of any json file and convert it to a string
	 * 
	 * @param path
	 * @return
	 */
	public static String fromJsonFileToString(String path) {

		return FileUtil.readFromFile(new File(path));
	}
	
	/**
	 * returns JSON object from JSON file
	 * @param path - File path
	 * @return JSON object
	 */
	public static JSONObject getJsonFromFile(String path){
		return new JSONObject(FileUtil.readFromFile(new File(path)));
		
	}

	/**
	 * get JSON File as a String and return it as List of TestData Objects
	 * 
	 * @param path
	 * @return
	 */
	public static List<TestData> fromJsonFileToTestData(String path) {
		List<TestData> testDatas = new ArrayList<TestData>();
		for (PhoneType pt : phoneTypes(path)) {
			testDatas.add(pt.getTestData());
		}
		return testDatas;
	}

	/**
	 * get all properties as a map
	 */
	public static Map<String, String> getProprtiesAsMap() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String jsonString = new String(Files.readAllBytes(Paths
					.get(DEFAULT_PROPERTIES_PATH)));
			JSONObject jsonObject = new JSONObject(jsonString);
			

			for (int i = 0; i < jsonObject.names().length(); i++) {
				String key = jsonObject.names().getString(i);
				String value = (String) jsonObject.get(jsonObject
						.names().getString(i));
				map.put(key, value);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * from JSON Object to Map of key as String and Object as a value
	 * 
	 * @param object
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> toMap(JSONObject object)
			throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	/**
	 * From JsonArray to List of Objects
	 * 
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}
	
	
	
	/**
	 * This Method is used for Simgo App Automation tests.  
	 *  
	 * @return
	 */
	public static List<PhoneTypeData> getPhoneDataFromJson(String jsonPath) {
		List<PhoneTypeData> phones = new ArrayList<PhoneTypeData>();
		try {
			String jsonString = JSONReader.fromJsonFileToString(jsonPath);
			JSONObject json = new JSONObject(jsonString);
			List<String> phoneDataKeys = JSONReader.getJsonKeys(json);
			for (String phoneDataKey : phoneDataKeys) {
				JSONObject phoneMetaData = (JSONObject) json.get(phoneDataKey);
				PhoneTypeData ptd = new PhoneTypeData();
				ptd.setPlatform(phoneMetaData.getString(key_Device_Patform));
				ptd.setModel(phoneMetaData.getString(key_Device_Model));
				ptd.setVersion(phoneMetaData.getString(key_Device_Version));
				ptd.setDeviceUDID(phoneMetaData.getString(key_Device_UDID));
				phones.add(ptd);
			}
		} catch (JSONException e) {
			System.out.println("DataReader " + e.getMessage());
		}
		return phones;

	}
	
	
}