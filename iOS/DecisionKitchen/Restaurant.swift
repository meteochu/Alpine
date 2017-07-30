//
//  Restaurant.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

struct Restaurant: Codable {
    
    var id: String
    
    var name: String
    
    var order: Order?
    
    var address: String
    
    var city: String
    
    var state: String
    
    var zip: String
    
}


