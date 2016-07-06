//
//  EchoTests.swift
//  EchoTests
//
//  Created by Nir Berman on 7/14/15.
//  Copyright (c) 2015 Nir Berman. All rights reserved.
//

import UIKit
import XCTest
import CoreTelephony

class SendEcho: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testSendEcho() {
        // This is an example of a functional test case.
   
        func sendEcho()-> Bool{
        
        let url:NSURL = NSURL(string: "tel://000002")!
       
        
            
        
            let echo:Bool = (UIApplication.sharedApplication().openURL(url));
            return echo;
            
        }
            
         
        
            
            
        
        
        XCTAssert( sendEcho(), "Could not send an echo via the phone, failure on the phone level");
            
        
        
        
        XCTAssert(true, "Pass")
    
    
    }
  
    }
    

