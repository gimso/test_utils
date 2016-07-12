package application_processor.phases.allocation;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application_processor.files.JsonFilesKeys;
import application_processor.phases.allocation.handler.AllocationResponse;
import application_processor.phases.common.PhaseManager;
import application_processor.phases.constants.StatusCode;

public class AllocationManager extends PhaseManager {

	private static AllocationManager mInstance;

	public static AllocationManager getInstance() {
		if (mInstance == null) {
			mInstance = new AllocationManager();
		}
		return mInstance;
	}

	@Override
	public String getResponse() throws ParseException, JSONException {
		JSONObject response = AllocationResponse.getInstance()
				.getValueFromJSONFile(JsonFilesKeys.KEY_ALLOCATION_RESPONSE);
		if (response.length() > 0) {
			response.put(JsonFilesKeys.JSON_KEY_RESPONSE_STATUS,
					getResponseStatus());
		}
		return response.toString();
	}

}
