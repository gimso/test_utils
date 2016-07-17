package gateway_util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class PersistApi {
	/**
	 * Sends GET request
	 * 
	 * @param url
	 * @param response
	 */
	public void sendGetRequest(String url) {
		given().when().get(url);
	}

	/**
	 * Sends GET request and assert status code
	 * 
	 * @param url
	 * @param response
	 */
	public void assertStatusCode(String url, int statusCode) {
		given().when().get(url).then().assertThat().statusCode(statusCode);
	}

	/**
	 * Sends GET request and assert status code and response
	 * 
	 * @param url
	 * @param response
	 */
	public void assertStatusCodeAndResponse(String url, int statusCode, String response) {
		given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response));
	}

	/**
	 * Sends GET request and assert status code and 2 responses
	 * 
	 * @param url
	 * @param response
	 */
	public void assertCodeAndDualResponse(String url, int statusCode, String response1, String response2) {
		given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1)).and()
				.body(containsString(response2));
		;
	}

	/**
	 * Sends GET request and assert status code and all responses
	 * 
	 * @param url
	 * @param response
	 */
	public void assertCodeAndMultipleResponse(String url, int statusCode, String response1, String response2,
			String response3, String response4) {
		given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1)).and()
				.body(containsString(response2)).and().body(containsString(response3)).and()
				.body(containsString(response4));
	}
}

