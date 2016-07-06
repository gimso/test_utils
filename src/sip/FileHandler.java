package sip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import testing_utils.TestOutput;

import org.testng.Reporter;



public class FileHandler {
	
	
	
	

	
	 public File createResultFile(String str_input, String filename) { // Write input to a temporary file
	     
			     
	     
	     
			try{
		 		 
    			//create a temp file
    			File file = File.createTempFile(filename, ".csv");
   
    			//String FilePath  = temp.getAbsolutePath();
    			
	 
    			// write the input to the file
    			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
    			bw.write(str_input);
    			bw.close();
    			return file.getAbsoluteFile();
    			
  		     
    			
    	  }catch(IOException e){

    		  e.printStackTrace();
    		 return null;

    	  					}
	     
	     
			
	    	     
	     
	    
	  }
	 
	 
	 
	
	 
	 
	 public String getTempFileLocation(File filename) {
		return filename.getAbsolutePath();
	 }
	
	
	
	
	
	
	
		  
		 
	public TestOutput searchCallInFile(File csvFile, String callernum, String callednum) {
		 
	

	
			  
	Boolean found = false;
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	String error = "Unknown error";
	
	
	
	
	try {
		 
		br = new BufferedReader(new FileReader(csvFile));
		/*
		if (br.readLine() == null)
				{
			       found = false;
			       error = "No SIP records were created!";
				}
		
		
		  else
				
			{
				found = false;
				error = "could not find the call in the sip logs";
			}
			 
		
		*/
		
		while (((line = br.readLine()) != null) && (found == false)){
			
			
			
		        // use comma as separator
			
			
			String[] cli = line.split(cvsSplitBy);
			
			if ((cli[0].equals(callernum)) || (cli[1].equals(callednum)))
			{
				
				if ((cli[0].equals(callernum)) && (cli[1].equals(callednum)))
				{
					found = true;
					error = "found";
									
				}
				else if ((cli[0].equals(callernum)) && (!cli[1].equals(callednum)))
				{	
					found = false;
					error = "could not find the called number for this caller";
					
				}
				
				else 
					
				{
				   found = false;
				   error =  "could not find the correct caller for the called number";
					
				}
				
			}	
				
		
			
		
				
		 
			
			
		
						
 
											}
		
		 TestOutput output = new TestOutput(found,error);
		 return output;

		}
	
	catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	return null;
		  }
		  
}




	
	


