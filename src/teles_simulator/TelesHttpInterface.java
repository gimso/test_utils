package teles_simulator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import global.PropertiesUtil;



/**
 * @author Or
 * This class provides the API functions to add and delete Sims from the teles simulator - in json over REST to the HTTP server. 
 */
public class TelesHttpInterface  {
	
	static String teles_url = PropertiesUtil.getInstance("Resources/Dyno.properties").getProperty( "TELES_URL");


	/**
	 * example: addSimToSimUnit("5","1","425000000000008","2g3g");
	 * @param boardnumber - the board number for the simulated sim unit - any number from 1 to 10, will create a new board if not exists
	 * @param offset - the sim location on the board (e.g - 30 for board 1 will be 30:1)
	 * @param imsi  - the sim's requsted imsi
	 * @param gen - sim generation.  Possible options here:  2g3g for both, 2g or 3g as a String. 

	 */
	public Boolean addSimToSimUnit(String boardnumber, String offset, String imsi, String gen)  {
		
		
		
		// Set An HTTP request URL with the boardnumber
		String url = teles_url + boardnumber;		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(url);
		// Construct the Request
		
		StringEntity input = constructSimJson(offset, imsi, gen);
			
		try {
			put.setEntity(input);
			HttpResponse response = client.execute(put);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				if (line.contains("PUT_OK")) 
				{	System.out.println("The sim was inserted to the teles simulator");
					return true;}
			}
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}

	

	/**
	 * @param boardnumber
	 * @param offset
	 * @param imsi
	 * @param gen
	 * @return action success indication
	 */
	public Boolean deleteSimFromSimUnit(String boardnumber, String offset, String imsi, String gen) {
		
		
		// Set An HTTP request URL with the boardnumber
		String url = teles_url + boardnumber;		
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpDeleteWithBody delete = new HttpDeleteWithBody(url);
		//HttpDelete delete = new HttpDelete(url);
		// Construct the Request
		
		StringEntity input = constructSimJson(offset, imsi, gen);
		delete.setEntity(input);	
		
		try {
			HttpResponse response = client.execute(delete);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				if (line.contains("DELETE_OK")) {return true;}
			}
			
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
		
	/** Deletes the board from the simulated sim unit
	 * @param boardnumber
	 * @return  success indication

	 */
	public  Boolean deleteBoard(String boardnumber) {
		
		// Set An HTTP request URL with the boardnumber
		String url = teles_url + boardnumber;		
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete delete = new HttpDelete(url);
		// Construct the Request
		
		try {
			HttpResponse response = client.execute(delete);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				if (line.contains("DELETE_OK")) {return true;}
			}
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public void deleteAllBoards(){
		
		
		
		for (int i = 0; i<11; i++)
		{
			if (!deleteBoard(Integer.toString(i)))
				{
				  System.err.println("failed to delete board number: " + i);
				}
		}
	}
	
	/** Forces the sim unit to return a network error (ISO 9862) on the next auth request that will be received)
	 * @return Success indication
	 */
	public Boolean forceAuthError() {


	
		// Set An HTTP request URL with the boardnumber
		String url = teles_url;	
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		// Construct the Request
		
		StringEntity input = new StringEntity("{\"throw\": \"9862\"}", "UTF-8");
			
		post.setEntity(input);
		try {
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				if (line.contains("FORCE_ERROR_OK")) {return true;}
			}
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		}
		
	
	
	
	private static StringEntity constructSimJson(String offset, String imsi,
			String gen) {
		// Construct the request Json for adding a sim
		String string = "{\"object\": {\"SIM\":[{\"offset\":\"" + offset +"\", \"imsi\": \"" + imsi +"\", \"gen\": \""+ gen +"\"}]}}";
		StringEntity input = new StringEntity(string,"UTF-8" );
		input.setContentType("application/json");
		return input;
	}

	
	
	
	
	

}
