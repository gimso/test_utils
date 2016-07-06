package cloudProtocol;

/**
 * This class holds the defined Simgo protocol Enums for the TLV messages opcodes 
 * @author orshachar
 *
 */
public class Enums {
	
	


	private static final byte PROTOCOL_VERSION = 1;
	private static byte[] swVersion = new byte[] { 0, 0, 0 };
	private static byte[] hwVersion = new byte[] { 0, 0, 0 };
	
	// Protocol Enums
	
	static final byte MAGIC_NUMBER = 0xd;
	
	
	// Open Session Response Statuses
	static final byte NO_ERROR = 0;
	static final byte GENERIC_ERROR = 1;
	static final byte INVALID_ARGUMENT = 2;
	static final byte RESOURCE_UNAVAILABLE = 3;
	static final byte REJECTED_ERROR = 4;
	static final byte USE_HSIM = 50;
	
	// Opcodes
	static final byte GET_CHALLENGE_REQUEST = 0x00;
	static final byte CREATE_SESSION_REQUEST = 0x01;
	static final byte DELETE_SESSION_REQUEST = 0x02;
	static final byte AUTHENTICATION_REQUEST = 0x03;
	static final byte KEEP_ALIVE_REQUEST = 0x04;
	static final byte OUTGOING_CALL_REQUEST = 0x05;
	static final int CREATE_SESSION_RESPONSE = 0x81;
	
	// Status code for outgoing calls
	static final byte ERROR = 0;
	static final byte NO_REDIRECTION = 1;
	static final byte REDIRECT = 2;
	static final byte REDIRECT_TO_LAST_GIVEN = 3;
	
	
	static final int PLUG_MSG_MAGIC_NUMBER_POSITION = 0;
	static final int PLUG_MSG_SIZE_POSITION = 1;
	static final int PLUG_MSG_TYPE_POSITION = 1;
	static final int PLUG_MSG_CHK_SUM_POSITION = 3;
	static final int PLUG_ID_LENGTH = 6;
	static final int MAX_PAYLOAD_SIZE = 140;

    static final byte[] TLV_TYPE = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	
	
	
	
}
