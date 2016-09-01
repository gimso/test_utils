package tcp_gateway;

import java.util.Timer;

import global.TypeConversions;

/**
 * @author Or
 * Provides a TCP gateway interface for sending Simgo protocol requests and receiving parsed responses 
 */
public class ProtocolInterface  {
	
	TCPHandler tcp = new TCPHandler();
	TCPMessage tcp_response;
	String cloudUrl;
	private static final long SOCKET_TIMEOUT = 10 * 1000; 
	
	public ProtocolInterface(String cloudUrl) {
		this.cloudUrl = cloudUrl;
	}
	

	/**
	 * Constructs and returns TCP Simgo protocol auth request according to a provided key, allocation and device 
	 * @param allocationId
	 * @param authKey
	 * @param deviceId
	 * @return A Simgo Protocol request with the header and TLVS
	 */
	public byte[] constructAuthenticationRequest(byte[] allocationId, String authKey, String deviceId) {
	    TLVArray tlvs = new TLVArray(3);
	    // Check If auth key length is even, else add "-1"
	    if ((authKey.length() %2)!=0) 
	    {
	    	authKey = authKey.concat("-1");
	    }
	    
	    // Construct TLVs
	    tlvs.add(new TLV(Tag.ALLOCATION_ID, allocationId));
	    tlvs.add(new TLV(Tag.AUTHENTICATION_REQUEST, TypeConversions.hexStringToByteArray(authKey)));
	    tlvs.add(new TLV(Tag.DEVICE_ID, deviceId.getBytes()));

	    return TCPMessage.encodeMessage(Opcode.AUTHENTICATION, tlvs);
	}
	

	/** Creates a TCP socket and fires a keep alive request
	 * @return
	 */
	public byte [] sendKeepAlive(){
		
		tcp.createNewTcpSocket(cloudUrl);
		byte [] keepAliveRequest =  TCPMessage.encodeMessage(Opcode.KEEP_ALIVE, new byte[]{});
		byte[] keepAliveResponse = tcp.sendMessage(keepAliveRequest);
		return keepAliveResponse;
	}
	

	/** Creates a Socket, fires a keep alive, and keeps the socket open until server timeout is reached
	 * @return
	 */
	public byte [] sendKeepAliveWaitForSessionTimeout(){
		
		tcp.createNewTcpSocket(cloudUrl, true, SOCKET_TIMEOUT);
		byte [] keepAliveRequest =  TCPMessage.encodeMessage(Opcode.KEEP_ALIVE, new byte[]{});
		byte[] keepAliveResponse = tcp.sendMessage(keepAliveRequest);
		return keepAliveResponse;
	}
	
		
	
	/**
	 * Creates a Socket, fires a Simgo protocol auth request and returns a TCPMessage type with response data
	 * @param allocationId
	 * @param authKey
	 * @param deviceId
	 * @return TCP Message Type, which contains the response, result code and tlv's
	 */
	public TCPMessage sendAuthRequestAndGetResponse (int allocationId, String authKey, String deviceId) {
		
		
		tcp.createNewTcpSocket(cloudUrl);
		byte [] authRequest = constructAuthenticationRequest(TypeConversions.convertIntToBytes(allocationId), authKey, deviceId);
		byte [] authResponse = tcp.sendMessage(authRequest);
		tcp_response = new TCPMessage(authResponse);
		return tcp_response;
		
		
	}
    

}
