package application_processor.phases.constants;

public class StatusCode {

	public static final int SUCCESS = 0;
	public static final int RESOURCE_UNAVAILABLE = 1;
	public static final int REJECTED = 2;
	public static final int NO_REDIRECTION = 3;
	public static final int REDIRECT = 4;
	public static final int REDIRECT_TO_LAST_GIVEN =5;
	public static final int ALLOCATION_DOES_NOT_EXIST = 6;
	public static final int FAILED_TO_ACCESS_SIM_UNIT = 7;
	public static final int AUTHENTICATION_FAILED_ON_SIM_UNIT = 8;
	public static final int ALLOCATION_NOT_FOUND = 9;
	public static final int DELETION_NOT_ALLOWED = 10;
	public static final int ALLOCATION_CAN_NOT_BE_DELETED = 11;
	public static final int UNKNOWN_USER = 12; 
	
	public static final int HTTP_ERROR = -1;
	public static final int DEFAULT = -1;
		
	public static String getInfoByCode(int code){
		switch(code){
		case SUCCESS:
			return "Success";
		case RESOURCE_UNAVAILABLE:
			return "Resource Unavailable";
		case REJECTED:
			return "Rejected";
		case NO_REDIRECTION:
			return "No Redirection";
		case REDIRECT:
			return "Redirect";
		case REDIRECT_TO_LAST_GIVEN:
			return "Redirect to last given";
		case ALLOCATION_DOES_NOT_EXIST:
			return "Allocation doesn’t exist";
		case FAILED_TO_ACCESS_SIM_UNIT:
			return "Failed to access SIM unit";
		case AUTHENTICATION_FAILED_ON_SIM_UNIT:
			return "Authentication failed on SIM unit";
		case ALLOCATION_NOT_FOUND:
			return "Allocation not found";
		case DELETION_NOT_ALLOWED:
			return "Deletion not allowed";
		case ALLOCATION_CAN_NOT_BE_DELETED:
			return "Allocation can not be deleted";
		case UNKNOWN_USER:
			return "Unknown User";
		default :
			return "";
		}
	}
}
