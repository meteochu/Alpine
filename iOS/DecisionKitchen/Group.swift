//
//  Group.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

typealias UserID = String

class Group: NSObject, Codable {
    
    var name: String
    
    var password: String
    
    var members: [UserID]
    
    var restaurants: [String: Restaurant]?
    
    var games: [Game]?
    
    var id: String
    
    init(name: String, password: String, members: [UserID], restaurants: [String: Restaurant]?, games: [Game]?, id: String) {
        self.name = name
        self.password = password
        self.members = members
        self.restaurants = restaurants
        self.games = games
        self.id = id
        super.init()
    }
    
}
