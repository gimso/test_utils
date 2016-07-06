package gateway_util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.jayway.restassured.http.ContentType;

public class IncomingSms {
	
	/**
	 * Sends a incoming sms request 
	 * @param contentType
	 * @param json
	 * @param url
	 */
	public void sendRequest(String json, String url){
		given().contentType(ContentType.JSON).body(json).when().post(url);	
	}
	
	/**
	 * Sends a incoming sms request and asserts that correct status code is received
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertStatusCode(String json, String url, int statusCode){
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode);
	}
	
	/**
	 * Sends a incoming sms request, asserts that correct status code is received, </br>and the correct response expected in the response body
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertCodeAndResponse(String json, String url, int statusCode, String response){
		given().contentType(ContentType.JSON).body(json).when().post(url).then().assertThat().statusCode(statusCode).and().body(containsString(response));
	}
	
	/**
	 * Sends a incoming sms request and asserts that correct status code is received, </br>and two correct responses that are expected- are received in the response body
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
