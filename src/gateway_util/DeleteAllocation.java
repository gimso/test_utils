package gateway_util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteAllocation {
	
	/**
	 * Sends a delete request
	 * @param url
	 */
	public void sendRequest(String url){
		given().when().delete(url);
	}
	
	/**
	 * Sends a delete request and asserts that correct status code is received
	 * @param url
	 * @param statusCode
	 */
	public void assertStatusCode(String url, int statusCode){
		given().when().delete(url).then().assertThat().statusCode(statusCode);
	}
	
	/**
	 * Sends a delete request and asserts that correct status code is received, <br>and the correct response expected in the response body
	 * @param url
	 * @param statusCode
	 * @param response
	 */
	public void assertCodeAndResponse(String url, int statusCode, String response){
		given().when().delete(url).then().assertThat().statusCode(statusCode).and().body(containsString(response));
	}
	
	/**
	 * Sends a delete request and asserts that correct status code is received, <br>and two correct responses that are expected- are received in the response body
	 * @param url
	 * @param statusCode
	 * @param response1
	 * @param response2
	 */
	public void assertCodeAndDualResponse(String url, int statusCode, String response1, String response2){
		given().when().delete(url).then().assertThat().statusCode(statusCode).
		and().body(containsString(response1)).and().body(containsString(response2));
	}
	
	
}
