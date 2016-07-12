package application_processor.phases.authentication;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application_processor.files.JsonFilesKeys;
import application_processor.phases.allocation.AllocationManager;
import application_processor.phases.allocation.handler.AllocationResponse;
import application_processor.phases.authentication.handler.AuthenticationResponse;
import application_processor.phases.common.PhaseManager;
import application_processor.phases.constants.StatusCode;

public class AuthenticationManager extends PhaseManager {

	private static AuthenticationManager mInstance;
	
	public static AuthenticationManager getInstance(){
		if(mInstance == null){
			mInstance = new AuthenticationManager();
		}
		return mInstance;
	}
	
	@Override
	public String getResponse() throws ParseException, JSONException {
		JSONObject response = AuthenticationResponse.getInstance().
				getValueFromJSONFile(JsonFilesKeys.KEY_AUTHENTICATION_RESPONSE);
		if(response.length() > 0){
			response.put(JsonFilesKeys.JSON_KEY_RESPONSE_STATUS, getResponseStatus());
		}
		return response.toString();
	}

}
