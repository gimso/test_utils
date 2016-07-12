package application_processor.phases.authentication.handler;

import CloudCommon.JsonFileManager;

public class AuthenticationResponse extends JsonFileManager {

	private static AuthenticationResponse mInstance;
	// file name
	public static final String AP_RESPONSES_NAME = "AP_Authentication_Responses.json";

	public static AuthenticationResponse getInstance(){
		if(mInstance == null){
			mInstance = new AuthenticationResponse();
		}
		return mInstance;
	}
	
	private AuthenticationResponse(){
		super(AP_RESPONSES_NAME);
	}
	
}
