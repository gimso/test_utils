package gateway_util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class Fota {

	/**
	 * Sends fota request using GET method
	 * 
	 * @param url
	 */
	public void sendGetRequest(String url) {
		given().when().get(url);
	}

	/**
	 * Sends fota request using GET method and asserts status code
	 * 
	 * @param url
	 * @param statusCode
	 */
	public void assertGetStatus(String url, int statusCode) {
		given().when().get(url).then().assertThat().statusCode(statusCode);
	}

	/**
	 * Sends fota request using GET method and asserts status code and response
	 * 
	 * @param url
	 * @param statusCode
	 * @param response
	 */
	public void assertGetStatusAndResponse(String url, int statusCode, String response) {
		given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response));
	}

	/**
	 * Sends fota request using GET method and asserts status code and 2
	 * responses
	 * 
	 * @param url
	 * @param statusCode
	 * @param response1
	 * @param response2
	 */
	public void assertGetStatusAndDualResponse(String url, int statusCode, String response1, String response2) {
		given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1)).and()
				.body(containsString(response2));
	}

	/**
	 * Sends fota request using GET method and asserts status code, contains
	 * response, and doesn't contain second response
	 * 
	 * @param url
	 * @param statusCode
	 * @param response1
	 * @param response2
	 */
	public void assertGetStatusAndResponseNotContain(String url, int statusCode, String response1, String response2) {
		given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1)).and()
				.body(not(contains(response2)));
	}

	/**
	 * Sends a fota request using PUT
	 * 
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertPutStatus(String json, String url, int statusCode) {
		given().contentType(ContentType.JSON).body(json).when().put(url).then().assertThat().statusCode(statusCode);
	}

	/**
	 * Sends a Fota state request (POST)
	 * 
	 * @param json
	 * @param url
	 */
	public void sendPostRequest(String json, String url) {
		given().contentType(ContentType.JSON).body(json).when().post(url);
	}

	/**
	 * Sends a Fota state request (POST) and asserts status code
	 * 
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertPostStatus(String json, String url, int statusCode) {
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode);
	}

	/**
	 * Sends a Fota state request (POST), asserts status code and response
	 * 
	 * @param json
	 * @param url
	 * @param statusCode
	 * @param response
	 */
	public void assertPostStatusAndResponse(String json, String url, int statusCode, String response) {
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode)
				.and().body(contains(response));
	}

	/**
	 * Sends a Fota state request (POST), asserts status code and 2 responses
	 * 
	 * @param json
	 * @param url
	 * @param statusCode
	 * @param response
	 */
	public void assertPostStatusAndDualResponse(String json, String url, int statusCode, String response1,
			String response2) {
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode)
				.and().body(containsString(response1)).and().body(containsString(response2));
	}
	
	/**
	 * Sends a Fota state request and gets a JsonObject response
	 * @param json
	 * @param url
	 * @return json object
	 */
	public JsonObject getJsonObjectResponse(String json, String url){
		Response obj =  given().contentType(ContentType.JSON).body(json).when().post(url).andReturn();	
		return	(JsonObject) new JsonParser().parse(obj.asString());
	}
}
