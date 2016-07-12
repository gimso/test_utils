import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import application_processor.phases.allocation.AllocationManager;
import application_processor.phases.authentication.AuthenticationManager;
import application_processor.phases.constants.ApKeys;
import application_processor.phases.constants.ServerMessages;
import application_processor.phases.constants.ServerResponseCodes;
import application_processor.phases.update.UpdateManager;

@Path("/")
public class CloudSimulator {

	public static final String TAG = "Cloud Simulator";

	private static final String JSON_KEY_PHASE = "phase";
	private static final String JSON_KEY_RESPONSE_CODE = "response_code";
	private static final String JSON_KEY_STATUS_CODE = "status_code";
	private static final int DEFAULT_VALUE = -1;

	public static void main(String[] args) {

	}

	// /////////////////////////////////// TESTING \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	@POST
	@Path("/verify")
	@Produces(MediaType.APPLICATION_JSON)
	// expected incoming data type
	public Response simulatorREST(InputStream incomingData) {
		try {
			JSONObject incoming = getIncomingData(incomingData);
			System.out.println("TAG - Data Received: " + incoming.toString());
			// return HTTP response 200 in case of success
			return Response.status(200).entity(incoming.toString()).build();
		} catch (Exception e) {
			return Response.status(400).build();
		}
	}

	@GET
	@Path("isOnline")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "Simgo Cloud Simulator Successfully started..";
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////

	@POST
	@Path("change_state")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changeState(InputStream incomingData) {
		try {
			JSONObject incoming = getIncomingData(incomingData);
			if (incoming != null && incoming.length() > 0
					&& incoming.has(JSON_KEY_PHASE)
					&& incoming.has(JSON_KEY_RESPONSE_CODE)) {
				String phase = incoming.getString(JSON_KEY_PHASE);
				int server_response_code = incoming
						.getInt(JSON_KEY_RESPONSE_CODE);
				int status_code = DEFAULT_VALUE;
				if (incoming.has(JSON_KEY_STATUS_CODE)
						&& !isErrorCode(server_response_code)) {
					status_code = incoming.getInt(JSON_KEY_STATUS_CODE);
				}
				handleChangedState(phase, server_response_code, status_code);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////APPLICATION PROCESS REQUESTS <-> RESPONSES \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	@POST
	@Path("allocation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllocation(InputStream incomingData) {
		// String request = getIncomingData(incomingData);
		int serverStatus = AllocationManager.getInstance()
				.getEServerResponseCodes();
		if (isErrorCode(serverStatus)) {
			return Response.status(serverStatus).build();
		} else if (serverStatus == ServerResponseCodes.DEFAULT) {
			return Response.status(ServerResponseCodes.SIMGO_SERVER_ERROR)
					.entity(ServerMessages.ERROR_STATE_WAS_NOT_SET).build();
		}
		try {
			String response = AllocationManager.getInstance().getResponse();
			return Response.status(serverStatus).entity(response).build();
		} catch (ParseException | JSONException e) {
			return Response.status(ServerResponseCodes.SIMGO_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("authentication")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authentication(InputStream incomingData) {
		int serverStatus = AuthenticationManager.getInstance()
				.getEServerResponseCodes();
		if (isErrorCode(serverStatus)) {
			return Response.status(serverStatus).build();
		} else if (serverStatus == ServerResponseCodes.DEFAULT) {
			return Response.status(ServerResponseCodes.SIMGO_SERVER_ERROR)
					.entity(ServerMessages.ERROR_STATE_WAS_NOT_SET).build();
		}
		try {
			String response = AuthenticationManager.getInstance().getResponse();
			return Response.status(serverStatus).entity(response).build();
		} catch (ParseException | JSONException e) {
			return Response.status(ServerResponseCodes.SIMGO_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(InputStream incomingData) {
		int serverStatus = UpdateManager.getInstance()
				.getEServerResponseCodes();
		if (isErrorCode(serverStatus)) {
			return Response.status(serverStatus).build();
		} else if (serverStatus == ServerResponseCodes.DEFAULT) {
			return Response.status(ServerResponseCodes.SIMGO_SERVER_ERROR)
					.entity(ServerMessages.ERROR_STATE_WAS_NOT_SET).build();
		}
		try {
			String response = UpdateManager.getInstance().getResponse();
			return Response.status(serverStatus).entity(response).build();
		} catch (ParseException | JSONException e) {
			return Response.status(ServerResponseCodes.SIMGO_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Method returns incoming data via http request
	 * 
	 * @param incomingData
	 * @return message via http request
	 * @throws Exception
	 */
	private JSONObject getIncomingData(InputStream incomingData)
			throws Exception, JSONException {
		StringBuilder incoming = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData));
			String line;
			while ((line = in.readLine()) != null) {
				incoming.append(line);
			}
			in.close();
			return new JSONObject(incoming.toString());
		} catch (Exception e) {
			throw new Exception("could not get stream, error: "
					+ e.getMessage());
		}
	}

	/**
	 * handles incoming state prior test
	 * @param aPhase - what phase we test
	 * @param aServerResponseCode - expected response code
	 * @param aStatusCode inner status code in case of code 200
	 */
	private void handleChangedState(String aPhase, int aServerResponseCode,
			int aStatusCode) {
		switch (aPhase) {
		case ApKeys.ALLOCATION:
			AllocationManager.getInstance().setResponseCode(
					aServerResponseCode, aStatusCode);
			break;
		case ApKeys.AUTHENTICATION:
			AuthenticationManager.getInstance().setResponseCode(
					aServerResponseCode, aStatusCode);
			break;
		case ApKeys.UPDATE:
			UpdateManager.getInstance().setResponseCode(aServerResponseCode,
					aStatusCode);
			break;
		default:
			break;
		}
	}

	/**
	 * check whether the status code is error or not
	 * 
	 * @param code
	 * @return true if its an error code (401,403,500)
	 */
	private boolean isErrorCode(int code) {
		return code == ServerResponseCodes.BAD_REQUEST
				|| code == ServerResponseCodes.UNAUTHORIZED
				|| code == ServerResponseCodes.SIMGO_SERVER_ERROR;
	}
}
