package unit_tests;



import teles_simulator.TelesHttpInterface;

import java.util.Random;

import org.apache.tools.ant.taskdefs.Sleep;

import cloudProtocol.MessageTypes.Challenge;
import cloudProtocol.MessageTypes.Session;
import global.PropertiesUtil;
import global.TypeConversions;
import tcp_gateway.*;

public class tcp_gateway_test {
	
	
	 			
	 	public static void main(String[] args) {
	 		
	 		
	 		TelesHttpInterface teles = new TelesHttpInterface(PropertiesUtil.getInstance("Resources/Dyno.properties").getProperty( "TELES_URL"));
	 		
			
			//System.out.println(teles.addSimToSimUnit("6","1","425000000000009","2g3g"));
	 					
	 		//teles.forceAuthError();
	 		ProtocolInterface pi = new ProtocolInterface("gwtcp.qa.gimso.net");
	 		TCPMessage tcp_response;
	 		byte [] keep_alive_response;
	 		
	 		
		
	 		int allocationId = 70;
	 		
	 		byte [] sessionIDByte = TypeConversions.convertIntToBytes(allocationId);
	 		
	 		
	 		
	 		tcp_response = pi.sendAuthRequestAndGetResponse(allocationId, "123456", "000040000016");
	 		
	 		
	 		
	 	    
	 		
	 		
	 		try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	    
	 		
	 		
	 		keep_alive_response = pi.sendKeepAlive();
	 		
	 		
	 		System.out.println(tcp_response.getResultCode());
	 		System.out.println(keep_alive_response);
	 
	 		
	 		
	 		
	 		
	 	}			
	 	
	 }
	 	



