package unit_tests;

import logging.CloudLogger;
import logging.PlugLogger;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;



public class TestCloudLog {
	

	
	  
		
	
	
	
	
	
  @Test
  public void testCloudLog() throws InterruptedException {
	  
	  
	  CloudLogger log1 = new CloudLogger(); 
	 
	 
	  log1.readCloudLog();
	 
	 
	 
	 Thread.sleep(5000);
	  
	 log1.stopCloudLog();
	 
	 
	 File foo = log1.getLogFile();
	 
	 
	
  }
  @BeforeTest
  public void beforeTest() {
	  
	  

	  
	  
	
	
	  
	  
	  
  }

  @AfterTest
  public void afterTest() {
	  
	
	  
	 
	 
  }
    
}
