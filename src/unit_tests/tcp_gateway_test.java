package unit_tests;



import teles_simulator.TelesHttpInterface;

import java.util.Random;

import cloudProtocol.MessageTypes.Challenge;
import cloudProtocol.MessageTypes.Session;
import global.TypeConversions;
import tcp_gateway.*;

public class tcp_gateway_test {
	
	
	 			
	 	public static void main(String[] args) {
	 		
	 		
	 		TelesHttpInterface teles = new TelesHttpInterface();
	 		
			
			//System.out.println(teles.addSimToSimUnit("6","1","425000000000009","2g3g"));
	 					
	 		teles.forceAuthError();
	 		ProtocolInterface pi = new ProtocolInterface("gwtcp.qa.gimso.net");
	 		
	 		ManageTCPAgent tcp = new ManageTCPAgent();
	 		TCPMessage tcp_response;
	 		
	 		
	 		tcp.createNewTcpSocket("gwtcp.qa.gimso.net");
	 		
	 		int allocationId = 69;
	 		
	 		byte [] sessionIDByte = TypeConversions.convertIntToBytes(allocationId);
	 		
	 		
	 		
	 		tcp_response = pi.sendAuthRequestAndGetResponse(allocationId, "123456", "000040000016");
	 		
	 		System.out.println(tcp_response.getResultCode());
	 
	 		
	 		
	 		
	 		
	 	}			
	 	
	 }
	 	



