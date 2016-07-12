package application_processor.phases.update.handler;

import CloudCommon.JsonFileManager;

public class UpdateResponse extends JsonFileManager {

	private static UpdateResponse mInstance;
	
	// file name
	public static final String AP_RESPONSES_NAME = "AP_Update_Responses.json";

	public static UpdateResponse getInstance(){
		if(mInstance == null){
			mInstance = new UpdateResponse();
		}
		return mInstance;
	}
	
	private UpdateResponse(){
		super(AP_RESPONSES_NAME);
	}
	
}
