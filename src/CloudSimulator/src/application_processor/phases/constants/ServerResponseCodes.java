package application_processor.phases.constants;

public class ServerResponseCodes {
	
	public static final int DEFAULT = 0;
	public static final int SUCCESS = 200;
	public static final int SUCCESS_CREATE = 201;
	public static final int UNAUTHORIZED = 403;
	public static final int BAD_REQUEST = 400;
	public static final int SIMGO_SERVER_ERROR = 500;
	public static final int UNKNOWN = -1;
	
	
	/**
	 * return response code
	 * @param code
	 * @return
	 */
	public static String getResponseCodeByCode(int code){
		switch(code){
			case DEFAULT:
				return "DEFAULT";
			case SUCCESS:
				return "SUCCESS";
			case SUCCESS_CREATE:
				return "SUCCESS_CREATE";
			case UNAUTHORIZED:
				return "UNAUTHORIZED";
			case BAD_REQUEST:
				return "BAD_REQUEST";
			case SIMGO_SERVER_ERROR:
				return "SIMGO_SERVER_ERROR";
			default:
				return "UNKNOWN";
		}
	}
}