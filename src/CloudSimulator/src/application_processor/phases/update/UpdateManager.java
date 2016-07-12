package application_processor.phases.update;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application_processor.files.JsonFilesKeys;
import application_processor.phases.allocation.handler.AllocationResponse;
import application_processor.phases.authentication.handler.AuthenticationResponse;
import application_processor.phases.common.PhaseManager;
import application_processor.phases.constants.StatusCode;
import application_processor.phases.update.handler.UpdateResponse;

public class UpdateManager extends PhaseManager {

	private static UpdateManager mInstance;
	
	public static UpdateManager getInstance(){
		if(mInstance == null){
			mInstance = new UpdateManager();
		}
		return mInstance;
	}
	
	@Override
	public String getResponse() throws ParseException, JSONException {
		JSONObject response = UpdateResponse.getInstance().getValueFromJSONFile(JsonFilesKeys.KEY_UPDATE_RESPONSE);
		if(response.length() > 0){
			response.put(JsonFilesKeys.JSON_KEY_RESPONSE_STATUS, getResponseStatus());
		}
		return response.toString();
	}
}
