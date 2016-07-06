//

//  testFSIM.swift

//  SimTest

//

//  Created by Nir Berman on 5/19/15.

//  Copyright (c) 2015 Nir Berman. All rights reserved.

//



import UIKit

import XCTest

import CoreTelephony

import FSIM





class testFSIM: XCTestCase {
    
    
    
    
    
    
    
    
    
    
    
    //  var pd:providerData = providerData()
    
    
    
    override func setUp() {
        
        super.setUp()
        
        
        
        // Send the FSIM technician code
        
        
        
        
        
        
        
    }
    
    
    
    override func tearDown() {
        
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        
        super.tearDown()
        
    }
    
    
    
    func testThatPhoneHasFooSIM() {
        
        
        let mccFake = expectationWithDescription("MCC was set as Fake")
        
        
        
        let mncFake = expectationWithDescription("MNC was set as Fake")
        
        
        
        let carrierFake = expectationWithDescription("Carrier Was set as fake")
        
        
        
        let url:NSURL = NSURL(string: "tel://000001")!
    
        
        
        let sendFSIM:Bool = (UIApplication.sharedApplication().openURL(url))
        
        
        
        XCTAssert(sendFSIM, "Could not send an FSIM via the phone, failure on the phone level");
        
        
        
        let pd:providerData = providerData();
        
        
        
        var fsimSet:Bool = false;
        
            
            
            sleep (3);
            
            
            
            if let od =  pd.getOperatorData() {
                
                
                
                
                if (od.MCC != nil )
                {if (od.MCC! == "001")   {fsimSet = true; mccFake.fulfill()}};
                
                
                if (od.MNC != nil )
                {if (od.MNC! == "01")    {mncFake.fulfill()}};
                
                if (od.CarrierName != nil)
                {if (od.CarrierName == "Carrier Lab")   {carrierFake.fulfill()}};
                
                
                
            }
            
            
            
            
            
            
            
    
        
        
        
        
        
        
        
        waitForExpectationsWithTimeout(10) { error in
            
            
            
            if let error = error {
                
                print("Error: \(error.localizedDescription)", terminator: "")
                
                
                
            }
            
            
            
            
            
        }
        
        
        
        
        
    }
    
    
    
}

