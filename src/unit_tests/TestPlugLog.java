package unit_tests;

import logging.PlugLogger;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;



public class TestPlugLog {
	
	
	
	
	
	
	
	
  @Test
  public void testCloudLog2() throws InterruptedException {
	
	  PlugLogger pl = new PlugLogger("COM11");
	  pl.readPlugLog();
	  java.lang.Thread.sleep(20000);
	  
	  pl.stopPlugLog();
	  
  }
  @BeforeTest
  public void beforeTest() {
	 
	
	 
	  
	  
	
	
	  
	  
	  
  }

  @AfterTest
  public void afterTest() {
	
	  System.out.println("fine");
	 
	  
  }
    
}
