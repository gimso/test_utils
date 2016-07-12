package CloudCommon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class JsonFileManager {
	
	protected JSONObject mFile;
	private String mFileName;
	private String mFilePath;
	
	protected JsonFileManager(String aFileName){
		mFileName = aFileName;
		mFilePath = String.format("%s/%s",System.getProperty("user.dir"),mFileName);
		setDataFile();
	}
	
	/**
	 * Pull JSON responses from file
	 */
	public String getJsonFromFile(String path){
		try {
			org.json.simple.JSONObject json =  ((org.json.simple.JSONObject)new JSONParser().parse(new FileReader(path)));
			return json.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void setDataFile(){
		try {
			mFile = new JSONObject(getJsonFromFile(mFilePath));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * get Json File
	 * @return
	 */
	public  JSONObject getDataFile(){
		if(mFile == null){
			setDataFile();
		}
		return mFile;
	}
	
	/**
	 *
	 * @return JSON Object contains json response
	 */
	public JSONObject getValueFromJSONFile(String key) {
		try{
			if(mFile == null)
				setDataFile();
			if(mFile.has(key))
				return mFile.getJSONObject(key);
			}catch(Exception e){
				return new JSONObject();
			}
		return new JSONObject();
	};

}
