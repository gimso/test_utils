package cloudProtocol;


import java.nio.charset.Charset;
import java.util.Map;


import cloudProtocol.MessageTypes.Challenge;
import cloudProtocol.MessageTypes.Session;
import global.TypeConversions;



public class GateKeeperInterface {
	MessageTypes messageTypes;
	ProtocolUtil protocolUtil;
	Challenge challenge;
	Session  session;
	String CLOUD_URL = "d.qa.gimso.net:5150";
	
	
		
	public GateKeeperInterface() {
				
		protocolUtil = new ProtocolUtil(CLOUD_URL);
		messageTypes = new MessageTypes();
		
		
	}
	
	
	
	/**
	 * @param plugid 
	 * @return a byte array containing the Challenge response or null 
	 */
	public MessageTypes.Challenge getChallengeForPlug(String plugid) {
		
		challenge = messageTypes.new Challenge();
		
		// Add plug prefix and convert it to a byte array
		
		byte [] plugIdByte = protocolUtil.plugAddPrefix(plugid);
		
		// Construct the challenge header
		
		byte[] header = new byte[] {
				(byte) ((0xd << 4) | protocolUtil.getSequenceNumber()),
				Enums.GET_CHALLENGE_REQUEST };
		
		// Construct the TLV
		
		byte [] tlv = protocolUtil.generateTlv(0x00, plugIdByte);

		// Construct the request
		
		byte [] request = TypeConversions.combineByteArrays(header,tlv);
		
		// Fire the request
		
		byte[] response = protocolUtil.removeCommonHeader(protocolUtil.executeUdpQuery(request));
		
		// Validate response
		
		if (response == null) {
			System.err.println("getSessionChallenge Failed to Get Proper Response From Cloud");
			return null;
		} else if (response.length < 13) {
			System.err.println("getSessionChallenge Reply From Cloud is Too Small ["
					+ Integer.toString(response.length) + "]");
			return null;
		}	
		
		
		// Set Challenge Object
		
		// Challenge Response
		//ByteBuffer msgByteBuffer = ByteBuffer.wrap(response);
		
		Map<Byte, byte[]> decodedTlv = protocolUtil.decodeTlvRecords(response);
		//byte [] challengeResponse = protocolUtil.decodeTlvRecord(0x01, response);
		
		byte [] challengeResponse = decodedTlv.get((Enums.TLV_TYPE[1]));
		challenge.setChallengeResponse(challengeResponse);
		// Status Code
		//byte[] statusCode = protocolUtil.decodeTlvRecord(0, response);
		byte [] statusCode = decodedTlv.get((Enums.TLV_TYPE[0]));
		challenge.setStatus(TypeConversions.hexStringToDecimalInt(TypeConversions.byteArrayToHexString(statusCode)));
		
		return challenge;
		
		
	
		
	}
	
	
	 /**
	 * @param plugId - plug ID as appears on the persist DB
	 * @param mcc - e.g 425 for Israel
	 * @param sw_version - plug sw version - e.g 1.1.1
	 * @param fw_version - e.g 1.1.1
	 * @param plugChallenge - here provide the challenge response by @getChallengeForPlug
	 * @return A hashMap with all the TLV's create session response, in a Hex Format within a byte array. 
	 */
	public  MessageTypes.Session createSession(String plugId, int mcc, String plug_sw_version, String plug_fw_version, final byte[] plugChallenge)
	    {	
			
			session = messageTypes.new Session();
			
		 	// Perform an ugly java casting - no time for encoding properly
			
			byte [] plugIdByte = protocolUtil.plugAddPrefix(plugId);
			byte[] mccByte = global.TypeConversions.convertIntToBytes(mcc);
			byte[] swVersion = plug_sw_version.getBytes(Charset.forName("UTF-16"));
			byte[] hwVersion = plug_fw_version.getBytes(Charset.forName("UTF-16"));
		
			// Validate input
			
			if (plugChallenge == null || plugChallenge.length <= 0 || plugIdByte == null
					|| plugIdByte.length <= 0) {
				System.err
						.println("openSession Received INVALID initialization parameter(s)");
				return null;
			}

			
			// Construct The header
			
			byte[] header = new byte[] {
					(byte) ((0xd << 4) | protocolUtil.getSequenceNumber()),
					Enums.CREATE_SESSION_REQUEST };
			
			
			// Construct TLV's according to the protocol specification
				
				byte [] plugTLV = protocolUtil.generateTlv(0x00, plugIdByte); 
				byte [] challengeTLV = protocolUtil.generateTlv(0x01, plugChallenge);
				byte [] mccTLV = protocolUtil.generateTlv(0x02, mccByte);	
				byte [] swVersionTLV = protocolUtil.generateTlv(0x03, swVersion);
				byte [] hwVersionTLV = protocolUtil.generateTlv(0x04, hwVersion);
				
			// Construct the Open Session request	
							
			byte [] request = TypeConversions.combineMultipleByteArrays(header,plugTLV,challengeTLV,mccTLV,swVersionTLV,hwVersionTLV);
					

			// Fire the request and wait for a synchronous response from the gatekeeper

			byte[] response = protocolUtil.removeCommonHeader(protocolUtil.executeUdpQuery(request));
			
			// Validate response is not null
			
			if (response == null) {
				System.err.println("The gatekeeper response is null");
				return null;
			} else if (response.length < 24) {
				System.err.println("OpenSession Reply From Cloud is Too Small ["
						+ Integer.toString(response.length) + "]");
				return null;
			}

			// Parse Cloud response
			
			//Decode byte array to TLV records
			
			Map<Byte, byte[]> decodedTlv = protocolUtil.decodeTlvRecords(response);
			
			// Decode the status code
			
			byte[] statusCode = decodedTlv.get(Enums.TLV_TYPE[0]);
			int statusInt = TypeConversions.hexStringToDecimalInt(TypeConversions.byteArrayToHexString(statusCode));
			session.setStatus(statusInt);
							
			if (statusCode != null && statusInt == 0) {
					
			// Extract session ID
			byte [] sessionIDByte = decodedTlv.get(Enums.TLV_TYPE[1]);	
			if (sessionIDByte != null) {		
			//int sessionId = TypeConversions.hexStringToDecimalInt(TypeConversions.byteArrayToHexString(sessionIDByte));
			session.setSessionId(sessionIDByte);
			}
			
			// Extract SIM Digest
			byte[] digestByte = decodedTlv.get(Enums.TLV_TYPE[2]);
			if (digestByte != null) {
			String digest = (TypeConversions.byteArrayToHexString(digestByte));
			session.setDigest(digest);
			    }
			}
			
			// Extract UTCtime
			
			
			byte[] UTCtimeByte = decodedTlv.get(Enums.TLV_TYPE[3]);
			if (UTCtimeByte != null){
			String UTCtime = (TypeConversions.byteArrayToHexString(UTCtimeByte));
			session.setUTCtime(UTCtime);
			}
			/* TODO - Decode and Cast these parameters in response as well
			
			byte[] UTCoffset = decodedTlv.get(TLV_TYPE[4]);
			byte[] capabilitiesBitmask = decodedTlv.get(TLV_TYPE[5]);
			byte[] PlugUpdateURL = decodedTlv.get(TLV_TYPE[6]);
			byte[] ForcePlugUpdate = decodedTlv.get(TLV_TYPE[7]);
			byte[] APNName = decodedTlv.get(TLV_TYPE[8]);
			byte[] APN = decodedTlv.get(TLV_TYPE[9]);
			
			*/

			// Get the decimal values
			
		
		
			return session;
	    }
	 
	/**
	 * 
	 * @param plugId
	 * @param mcc
	 * @param plugChallenge
	 * @return A hashMap with all the TLV's create session response, in a Hex Format within a byte array. 
	 */
	public MessageTypes.Session createSession(String plugId, int mcc, byte[] plugChallenge) {
			return createSession(plugId,mcc,"1","1",plugChallenge);
	}
	

	public String echoRequest(byte[] sessionId, String mcc) {
		
		// Verify input
		
		
		
		// Construct header

		byte[] header = new byte[] {(byte) ((0xd << 4) | protocolUtil.getSequenceNumber()), Enums.KEEP_ALIVE_REQUEST };
		
		// Construct TLVs
		
		byte[] sessionTLV = protocolUtil.generateTlv(0x00, sessionId);
		
		// Construct MCC TLV  
		
		byte[]  mccTLV = protocolUtil.generateTlv(0x04, TypeConversions.decimalNumberToByteArray(mcc));
		
		// Construct Request
		
		byte [] request = TypeConversions.combineMultipleByteArrays(header, sessionTLV, mccTLV);
		
		// Fire reqeust and get response
		
		byte [] response = protocolUtil.removeCommonHeader(protocolUtil.executeUdpQuery(request));
		
		
		
		// TODO - handle echo response

		if (response != null) {
			return "echo request was received";
		} else {
			System.err.println("echoRequest Failed to Get Proper Response From Cloud");
			return null;
		}
	}
	
	
	public String echoRequest(byte[] sessionId){
		return echoRequest(sessionId, "425");
	}
	
	public byte [] authRequest(byte [] sessionId, String authKey) {
		
		// Construct header

		byte[] header = new byte[] {(byte) ((0xd << 4) | protocolUtil.getSequenceNumber()), Enums.AUTHENTICATION_REQUEST };

		// Construct TLVs
		
		byte[] sessionTLV = protocolUtil.generateTlv(0x00, sessionId);
		byte [] authRequest = protocolUtil.generateTlv(0x01, TypeConversions.decimalNumberToByteArray(authKey));
		
		// Construct Request
		
		byte [] request = TypeConversions.combineMultipleByteArrays(header, sessionTLV, authRequest);
		
      
		// Fire reqeust and get response
		
		byte [] response = protocolUtil.removeCommonHeader(protocolUtil.executeUdpQuery(request));
 
		
		
		// TODO - handle auth response

		if (response != null) {
			
			return response;
			
		}
		else {
			System.err.println("echoRequest Failed to Get Proper Response From Cloud");
			return null;
		}
        
        
		
	}

	
	


	public byte [] outgoingCallRequest (byte [] sessionId, long destNumber) {
		
		// Construct header

		byte[] header = new byte[] {(byte) ((0xd << 4) | protocolUtil.getSequenceNumber()), Enums.OUTGOING_CALL_REQUEST };

		// Construct TLVs
		
		byte [] sessionTLV = protocolUtil.generateTlv(0x00, sessionId);
		
		
		byte [] destTLV = protocolUtil.generateTlv(0x01, TypeConversions.decimalToBCDArray(destNumber));
		
		
		// Construct Request
		
		byte [] request = TypeConversions.combineMultipleByteArrays(header, sessionTLV, destTLV);
		
      
		// Fire reqeust and get response
		
		byte [] response = protocolUtil.removeCommonHeader(protocolUtil.executeUdpQuery(request));
 
		
		
		// TODO - handle outgoing call response

		if (response != null) {
			
			return response;
			
		}
		else {
			System.err.println("echoRequest Failed to Get Proper Response From Cloud");
			return null;
		}
        
		
	}
	
}

   


