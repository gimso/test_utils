package sip;


import java.io.File;








public class SipSSHInterface {
	
	private static final String PUB_RELATIV_PATH = "files/10.0.0.60[10.0.0.60]22.pub";



	public SSHManager SshToSipServer() {// This Method Creates an SSH session to the Sip Simulator VM (Currently Amy)
		// And returns the instance

		
		
		
	SSHManager instance = new SSHManager("simgo", "gimso!Tsoltar", "10.0.0.60", new File(PUB_RELATIV_PATH).getPath());

    String errorMessage = instance.connect();

		     if(errorMessage != null)
		     {
		        System.out.println(errorMessage);
		        return null;
		        
		     }
		     
	
	return instance;
	
	}
	
	
	
		
		public void CloseSipSsh(SSHManager instance)  {// This Method closes the session for the instance
			
			instance.close();
		}
		
		
		
		
	
		public String RunSipSimulator(SSHManager instance) { // This Method Runs the SIPP simulator server as
			// a background process, writing the output to the file sippout.log
		
			  
			  String stdout = instance.sendCommand("/home/simgo/Downloads/sipp-3.4.1/sipp -sf /home/simgo/Downloads/sipp-3.4.1/terminator.xml -trace_logs -bg -log_file /home/simgo/Downloads/sipp-3.4.1/sippout.log &");
			  
			  
			  
			  String sip_process_id = stdout.substring(stdout.indexOf("[") + 1, stdout.indexOf("]"));
					  
			  
			  return sip_process_id;
		
		
			  
		
		}
	
		
		public String StopSipSimulator(SSHManager instance, String sip_pid) {// Kills the sip simulator process gracefully
			System.out.println(sip_pid);
			String command = "kill -9 " + sip_pid;
			System.out.println(command);
			String stdout = instance.sendCommand(command);
			return stdout;
		}
		
		
		
		public String ReturnSippResultsInCsv(SSHManager instance) { // Runs a bash script, which modifies the sipp simulator
			// results file to a CSV file, containing only Caller number, and Called number, and return the contents as string
			
			
			 String stdout = instance.sendCommand("/home/simgo/scripts/sippout");			 
			 
			 return stdout;
			 
		}
		
		public String getCsvToString(SSHManager instance)
		{
			
			String results = instance.sendCommand("cat results.csv");
			
			return results;
		}
		
		
		public String deleteResultFiles(SSHManager instance)
		{
			String stdout = instance.sendCommand("/home/simgo/scripts/removefiles");
			
			
			return stdout;
		}
		
		
		public String killAllSippInstances(SSHManager instance)
		
		{
			
			String stdout = instance.sendCommand("pkill -f sipp");
			return stdout;
			
		}
	
	

	
}
		 
	


