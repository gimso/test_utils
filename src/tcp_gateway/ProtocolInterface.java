package tcp_gateway;

import java.util.Timer;

import global.TypeConversions;

public class ProtocolInterface {
	
	ManageTCPAgent tcp = new ManageTCPAgent();
	TCPMessage tcp_response;
	String cloudUrl;
	private static final long SOCKET_TIMEOUT = 10 * 1000; 
	
	public ProtocolInterface(String cloudUrl) {
		this.cloudUrl = cloudUrl;
	}
	
	
	public byte[] createAuthenticationRequest(byte[] allocationId, String authKey, String deviceId) {
	    TLVArray tlvs = new TLVArray(3);
	    // Construct TLVs
	    tlvs.add(new TLV(Tag.ALLOCATION_ID, allocationId));
	    tlvs.add(new TLV(Tag.AUTHENTICATION_REQUEST, ConvertersUtil.hexStringToByteArray(authKey)));
	    tlvs.add(new TLV(Tag.DEVICE_ID, deviceId.getBytes()));

	    return TCPMessage.encodeMessage(Opcode.AUTHENTICATION, tlvs);
	}
	
	public byte [] sendKeepAlive(){
		
		tcp.createNewTcpSocket(cloudUrl);
		byte [] keepAliveRequest =  TCPMessage.encodeMessage(Opcode.KEEP_ALIVE, new byte[]{});
		byte[] keepAliveResponse = tcp.sendMessage(keepAliveRequest);
		return keepAliveResponse;
	}
	
	public byte [] sendKeepAliveWaitForSessionTimeout(){
		
		tcp.createNewTcpSocket(cloudUrl, true, SOCKET_TIMEOUT);
		byte [] keepAliveRequest =  TCPMessage.encodeMessage(Opcode.KEEP_ALIVE, new byte[]{});
		byte[] keepAliveResponse = tcp.sendMessage(keepAliveRequest);
		return keepAliveResponse;
	}
	
		
	
	public TCPMessage sendAuthRequestAndGetResponse (int allocationId, String authKey, String deviceId) {
		
		
		tcp.createNewTcpSocket(cloudUrl);
		byte [] authRequest = createAuthenticationRequest(TypeConversions.convertIntToBytes(allocationId), authKey, deviceId);
		byte [] authResponse = tcp.sendMessage(authRequest);
		tcp_response = new TCPMessage(authResponse);
		return tcp_response;
		
		
	}
    

}
