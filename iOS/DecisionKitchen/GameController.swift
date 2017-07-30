//
//  GameController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

struct GameController {
    
    static var shared: GameController = GameController()
    
    private var restaurants: [String: Restaurant] = [:]
    
    func restaurant(for id: String) -> Restaurant? {
        return restaurants[id]
    }
    
    mutating func addRestaurants(from group: Group) {
        for (id, value) in group.restaurants {
            self.restaurants[id] = value
        }
    }
    
    
}
