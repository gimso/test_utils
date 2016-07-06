package unit_tests;

import teles_simulator.TelesHttpInterface;
import cloudProtocol.GateKeeperInterface;
import cloudProtocol.MessageTypes.Challenge;
import cloudProtocol.MessageTypes.Session;

public class CloudProtocol_Test {
	
	
	 			
	 	public static void main(String[] args) {
	 		
	 		
	 		TelesHttpInterface teles = new TelesHttpInterface();
	 		
			
			//System.out.println(teles.addSimToSimUnit("6","1","425000000000009","2g3g"));
	 				
	 		GateKeeperInterface gk = new GateKeeperInterface();		
	 		
	 				
			Challenge challenge = gk.getChallengeForPlug("000010002185");	
			
			byte [] res = challenge.getChallengeResponse();
	 				
	 		Session session= gk.createSession("000010002185", 425,"1","1",res);		
	 		byte [] sessionId = session.getSessionId();		
	 		String status = session.getStatus();		
	 		int sessionInt = session.getSessionIdAsInt();		
	 				
	 		System.out.println(status);		
	 		System.out.println(sessionInt);		
	 				
	 		gk.echoRequest(sessionId);	
	 		
	 		gk.authRequest(sessionId, "88872");
	 		
	 		
	 		
	 		gk.outgoingCallRequest(sessionId, 0526134331);
	 	}			
	 		
	 }


