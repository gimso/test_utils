package global;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
	
	private Map<String, Object> jsonMap;
	
	/**
	 * Wrapper for getJsonElementValue()
	 * Gets a json object, and operates getJsonElementValue()
	 * @param json object
	 * @return Map
	 */
	public Map<String, Object> getJsonMap(JsonObject json){
		jsonMap = new HashMap<>();
		getJsonElementValue(json);
		return jsonMap;
	}
	
	/**
	 * Extracts inner json element from json object
	 * @param json
	 */
	private void getJsonElementValue(JsonObject json) {
		for(Entry<String, JsonElement> element :json.entrySet()){			
			String key = element.getKey();
			JsonElement value = element.getValue();
			//check if value is primitive
			if (value.isJsonPrimitive()) {	
					//check if key doesn't already exist- and put key and value in jsonMap
					if (!(jsonMap.containsKey(key))) {
						jsonMap.put(key, value);
						//if exists print error
					}else {
						System.err.println("key " + key + " already exists");
					}					
			//else check if it's json object and extract next element
			} else if (value.isJsonObject()) {
				getJsonElementValue(value.getAsJsonObject());
				
			//else check if it's json array and extract element
			}else if (value.isJsonArray()) {
				for (JsonElement innerArray : value.getAsJsonArray()){
					if (innerArray.isJsonObject()) {
						getJsonElementValue(innerArray.getAsJsonObject());
					}
				}
			} 			
		}
	}
}
