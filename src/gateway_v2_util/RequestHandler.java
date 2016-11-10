package gateway_v2_util;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.http.ContentType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RequestHandler implements Runnable {
	private String fullJson;
	private String url;
	private Action action;
	private int statusCode;
	private String response1;
	private String response2;
	private String response3;
	private String response4;
	private Response obj;

	/**
	 * CTOR
	 * @param fullJson
	 * @param url
	 * @param action
	 */
	public RequestHandler(String fullJson, String url, Action action) {
		this.fullJson = fullJson;
		this.url = url;
		this.action = action;
	}

	/**
	 * CTOR
	 * @param fullJson
	 * @param url
	 * @param action
	 * @param statusCode
	 */
	public RequestHandler(String fullJson, String url, Action action, int statusCode) {
		this.fullJson = fullJson;
		this.url = url;
		this.action = action;
		this.statusCode = statusCode;
	}

	/**
	 * CTOR
	 * @param fullJson
	 * @param url
	 * @param action
	 * @param statusCode
	 * @param response1
	 */
	public RequestHandler(String fullJson, String url, Action action, int statusCode, String response1) {
		this.fullJson = fullJson;
		this.url = url;
		this.action = action;
		this.statusCode = statusCode;
		this.response1 = response1;
	}

	/**
	 * CTOR
	 * @param fullJson
	 * @param url
	 * @param action
	 * @param statusCode
	 * @param response1
	 * @param response2
	 */
	public RequestHandler(String fullJson, String url, Action action, int statusCode, String response1,
			String response2) {
		this.fullJson = fullJson;
		this.url = url;
		this.action = action;
		this.statusCode = statusCode;
		this.response1 = response1;
		this.response2 = response2;
	}

	/**
	 * CTOR
	 * @param fullJson
	 * @param url
	 * @param action
	 * @param statusCode
	 * @param response1
	 * @param response2
	 * @param response3
	 * @param response4
	 */
	public RequestHandler(String fullJson, String url, Action action, int statusCode, String response1,
			String response2, String response3, String response4) {
		this.fullJson = fullJson;
		this.url = url;
		this.action = action;
		this.statusCode = statusCode;
		this.response1 = response1;
		this.response2 = response2;
		this.response3 = response3;
		this.response4 = response4;
	}

	/**
	 * Sends a request
	 * @param contentType
	 * @param json
	 * @param url
	 */
	public void sendRequest() {
		switch (action) {
		case POST:
			given().contentType(ContentType.JSON).body(fullJson).when().post(url);
			break;
		case PUT:
			given().contentType(ContentType.JSON).body(fullJson).when().put(url);
			break;
		case GET:
			given().when().get(url);
			break;
		case DELETE:
			given().when().delete(url);
			break;
		default:
			break;
		}
	}

	/**
	 * Sends request and asserts that correct status code is received
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertStatusCode() {
		switch (action) {
		case POST:
			given().contentType(ContentType.JSON).body(fullJson).when().post(url).then().assertThat()
					.statusCode(statusCode);
			break;
		case PUT:
			given().contentType(ContentType.JSON).body(fullJson).when().put(url).then().statusCode(statusCode);
			break;
		case GET:
			given().when().get(url).then().assertThat().statusCode(statusCode);
			break;
		case DELETE:
			given().when().delete(url).then().assertThat().statusCode(statusCode);
			break;
		default:
			break;
		}
	}

	/**
	 * Sends request, asserts that correct status code is received, <br>
	 * and the correct response expected in the response body
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertCodeAndResponse() {
		switch (action) {
		case POST:
			given().contentType(ContentType.JSON).body(fullJson).when().post(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1));
			break;
		case PUT:
			given().contentType(ContentType.JSON).body(fullJson).when().put(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1));
			break;
		case GET:
			given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1));
			break;
		case DELETE:
			given().when().delete(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1));
			break;
		default:
			break;
		}
	}

	/**
	 * Sends request and asserts that correct status code is received, <br>
	 * and two correct responses that are expected- are received in the response
	 * body
	 * @param contentType
	 * @param json
	 * @param url
	 * @param statusCode
	 */
	public void assertCodeAndDualResponse() {
		switch (action) {
		case POST:
			given().contentType(ContentType.JSON).body(fullJson).when().post(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1)).and().body(containsString(response2));
			break;
		case PUT:
			given().contentType(ContentType.JSON).body(fullJson).when().put(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1)).and().body(containsString(response2));
			break;
		case GET:
			given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1))
					.and().body(containsString(response2));
			break;
		case DELETE:
			given().when().delete(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1))
					.and().body(containsString(response2));
			break;
		default:
			break;
		}
	}

	/**
	 * Sends request and assert status code and 4 responses
	 * @param url
	 * @param response
	 */
	public void assertCodeAndMultipleResponse() {
		switch (action) {
		case POST:
			given().contentType(ContentType.JSON).body(fullJson).when().post(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1)).and().body(containsString(response2))
					.and().body(containsString(response3)).and().body(containsString(response4));
			break;
		case PUT:
			given().contentType(ContentType.JSON).body(fullJson).when().put(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1)).and().body(containsString(response2))
					.and().body(containsString(response3)).and().body(containsString(response4));
			break;
		case GET:
			given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1))
					.and().body(containsString(response2)).and().body(containsString(response3)).and()
					.body(containsString(response4));
			break;
		case DELETE:
			given().when().delete(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1))
					.and().body(containsString(response2)).and().body(containsString(response3)).and()
					.body(containsString(response4));
			break;
		default:
			break;
		}
	}

	/**
	 * Sends request and asserts status code, contains response, and doesn't
	 * contain second response.
	 * @param url
	 * @param statusCode
	 * @param response1
	 * @param response2
	 */
	public void assertStatusAndResponseNotContain() {
		switch (action) {
		case POST:
			given().contentType(ContentType.JSON).body(fullJson).when().post(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1)).and().body(not(contains(response2)));
			break;
		case PUT:
			given().contentType(ContentType.JSON).body(fullJson).when().put(url).then().assertThat()
					.statusCode(statusCode).and().body(containsString(response1)).and().body(not(contains(response2)));
			break;
		case GET:
			given().when().get(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1))
					.and().body(not(contains(response2)));
			break;
		case DELETE:
			given().when().delete(url).then().assertThat().statusCode(statusCode).and().body(containsString(response1))
					.and().body(not(contains(response2)));
			break;
		default:
			break;
		}
	}

	/**
	 * Sends a request and gets a JsonObject response
	 * @param contentType
	 * @param json
	 * @param url
	 * @return JsonObject
	 */
	public JsonObject getJsonObjectResponse() {
		switch (action) {
		case POST:
			obj = given().contentType(ContentType.JSON).body(fullJson).when().post(url).andReturn();
			return (JsonObject) new JsonParser().parse(obj.asString());
		case PUT:
			Response obj =  given().contentType(ContentType.JSON).body(fullJson).when().put(url).andReturn();	
			return	(JsonObject) new JsonParser().parse(obj.asString());
		default:
			return null;
		}
	}
	
	/**
	 * Runs a RequestHandler thread and interrupts it when it's done.
	 * @param requestHandler
	 */
	public static void runRequestInThread(RequestHandler requestHandler) {
		Thread thread = new Thread(requestHandler);
		thread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		
		thread.interrupt();
	}

	/**
	 * Runs a thread that sends a Rest API request
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		System.out.println("sending request...");
		sendRequest();
		System.out.println("req sent");	
	}	
}
