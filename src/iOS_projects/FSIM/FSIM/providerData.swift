//
//  providerData.swift
//  MarX
//
//  Created by Or Shachar on 2/19/15.
//  Copyright (c) 2015 Simgo. All rights reserved.
//

import UIKit
import CoreTelephony


public class providerData: NSObject {
    
    // Located here are all the functions that are meant to get connectivity data, MCC, MNC, 
    // ability to output calls and etc.
    
    
    
    
   

    
        public func is3gInternet() -> Bool {   // Return True if able to use 3G Internet, false in all other cases (even if WiFi Internet is available)
    
        
        
           let reachability: Reachability = Reachability.reachabilityForInternetConnection()
            
           let DataStatus: Int? = reachability.currentReachabilityStatus().rawValue;
            
            
            
            let isWan: NetworkStatus = ReachableViaWWAN
            //let isWifi: NetworkStatus = ReachableViaWiFi
            
        
    
            if (DataStatus == isWan.rawValue)
            {
                return true
            }
            else
            {
                return false
            }
    }
        
    
        

 
    
    
            public func getOperatorData() -> (MCC:String?, MNC: String?, CarrierName: String?)?{
        
            
            
            
            let NetworkInfo = CTTelephonyNetworkInfo ();
            
            
            var Carrier = CTCarrier();
            
            var flag = NetworkInfo.subscriberCellularProviderDidUpdateNotifier
        
        
            if let CellularProvider = NetworkInfo.subscriberCellularProvider
            
            
           
            
            
        
            {
               
            
            let MCC = CellularProvider.mobileCountryCode
            let MNC = CellularProvider.mobileNetworkCode
            let CarrierName = CellularProvider.carrierName
            
                
            return (MCC, MNC, CarrierName)
            
            
            }
            
            else
            
            {
                return nil
            }
            
            
           // var didChangeCarrier = NetworkInfo.subscriberCellularProviderDidUpdateNotifier(Carrier);
        
            
        
            
            
    }
        
        
        
        
            
    
    
    
    
    

}

