package gateway_util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;

public class OutgoingCall {
	
	/**
	 * Sends outgoing call request 
	 * @param contentType
	 * @param json
	 * @param url
	 */
	public void sendRequest(String json, String url){
		given().contentType(ContentType.JSON).body(json).when().post(url);	
	}
	
	/**
	 * Sends outgoing call request using GET method
	 * @param url
	 * @param response
	 */
	public void sendGetRequest(String url, int statusCode){
		given().when().get(url).then().assertThat().statusCode(statusCode);
	}
	
	/**
	 * Sends outgoing call request and gets a JsonObject response
	 * @param contentType
	 * @param json
	 * @param url
	 * @return JsonObject
	 */
	public JsonObject getJsonObjectResponse(String json, String url){
		Response obj =  given().contentType(ContentType.JSON).body(json).when().post(url).andReturn();	
		return	(JsonObject) new JsonParser().parse(obj.asString());
	}
	
	/**
	 * Sends outgoing call request and gets a ValidatableResponse response, in case parsing is required
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 * @return ValidatableResponse
	 */
	public ValidatableResponse getResponse(String json, String url, int statusCode){
		return given().contentType(ContentType.JSON).body(json).when().post(url).then().statusCode(statusCode);		
	}
	
	/**
	 * Sends outgoing call request and gets a String response, in case parsing is required
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 * @return response string
	 */
	public String getResponseAsString(String json, String url, int statusCode){
		return given().contentType(ContentType.JSON).body(json).when().post(url).then().statusCode(statusCode).toString();	
	}
	
	/**
	 * Sends outgoing call request and asserts that correct status code is received
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertStatusCode(String json, String url, int statusCode){
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode);
	}
	
	/**
	 * Sends outgoing call request, asserts that correct status code is received, <br>and the correct response expected in the response body
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertCodeAndResponse(String json, String url, int statusCode, String response){
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode).and().body(containsString(response));
	}
	
	/**
	 * Sends outgoing call request and asserts that correct status code is received, <br>and two correct responses that are expected- are received in the response body
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertCodeAndDualResponse(String json, String url, int statusCode, String response1, String response2){
		given().contentType(ContentType.JSON).body(json).when().post(url).
		then().assertThat().statusCode(statusCode).and().body(containsString(response1)).and().body(containsString(response2));
	}		
}
