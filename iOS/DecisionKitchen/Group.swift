//
//  Group.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

typealias UserID = String

struct Group: Codable {
    
    var name: String
    
    var password: String
    
    var members: [UserID]
    
    var restaurants: [String: Restaurant]?
    
    var games: [Game]?
    
    var id: String
    
}
