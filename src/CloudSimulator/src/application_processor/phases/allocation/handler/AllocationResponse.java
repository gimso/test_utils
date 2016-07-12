package application_processor.phases.allocation.handler;

import org.json.JSONObject;

import application_processor.phases.allocation.AllocationManager;
import CloudCommon.JsonFileManager;

public class AllocationResponse extends JsonFileManager {
	
	private static AllocationResponse mInstance;

	// file name
	public static final String AP_RESPONSES_NAME = "AP_Allocation_Responses.json";

	public static AllocationResponse getInstance(){
		if(mInstance == null){
			mInstance = new AllocationResponse();
		}
		return mInstance;
	}
	
	private AllocationResponse(){
		super(AP_RESPONSES_NAME);
	}
	
}
