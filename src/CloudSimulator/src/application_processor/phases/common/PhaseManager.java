package application_processor.phases.common;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import application_processor.files.JsonFilesKeys;
import application_processor.phases.constants.StatusCode;
import application_processor.phases.constants.ServerResponseCodes;

public abstract class PhaseManager {
	
	protected static PhaseManager mInstance;
	
	protected int mServerResponseCode = ServerResponseCodes.DEFAULT;
	protected int mStatusCode = StatusCode.DEFAULT;
	
	public static PhaseManager getInstance() throws Exception{
		if(mInstance == null){
			throw new Exception("phase manager cannot be instantiated");
		}
		return mInstance;
	}
	
	public int getEServerResponseCodes(){
		return mServerResponseCode;
	}
	
	private void setServerResponseCode(int aServerResponseCode){
		mServerResponseCode = aServerResponseCode;
	}
	
	public int getEStatusCode(){
		return mStatusCode;
	}
	
	public void setEstatusCode(int aStatusCode){
		if(aStatusCode >= 0){
			mStatusCode = aStatusCode;
		}else{
			mStatusCode = StatusCode.HTTP_ERROR;
		}
	}
	
	/**
	 * set response codes
	 * @param aServerResponseCode server response code (200, 201, 400)
	 * @param aStatusCode in case of server status 200/201 we may still receive an error in cloud, status code will indicate
	 * the source of the problem
	 */
	public void setResponseCode(int aServerResponseCode, int aStatusCode){
		setServerResponseCode(aServerResponseCode);
		setEstatusCode(aStatusCode);
	}
	
	
	/**
	 * get corresponded response
	 * @param key the key the reponse is saved in Json file
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	public abstract String getResponse() throws ParseException, JSONException;
	
	
	/**
	 * return corresponded response code 
	 * @return JSON including status code and info 
	 * @throws JSONException 
	 */
	public JSONObject getResponseStatus() throws JSONException{
		JSONObject response_code = new JSONObject();
		response_code.put(JsonFilesKeys.JSON_KEY_CODE, getEStatusCode());
		response_code.put(JsonFilesKeys.JSON_KEY_INFO, StatusCode.getInfoByCode(getEStatusCode()));
		return response_code;
	}
	
}
