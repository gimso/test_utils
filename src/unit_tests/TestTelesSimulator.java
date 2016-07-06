package unit_tests;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import teles_simulator.TelesHttpInterface;

public class TestTelesSimulator {

	public static void main(String[] args) {
		TelesHttpInterface teles = new TelesHttpInterface();
		
						
			System.out.println(teles.addSimToSimUnit("5","1","425000000000008","2g3g"));
			System.out.println(teles.deleteSimFromSimUnit("5","1","425000000000008","2g3g"));
			System.out.println(teles.deleteBoard("1"));
			System.out.println(teles.forceAuthError());
	
		
		}

	}


