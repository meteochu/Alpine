//
//  Game.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

struct Game: Codable {
    
    var categories: [Category]
    
    var meta: Meta
    
    var rating: [UserID: Int]
    
    var responses: [UserID: [Int]]
    
    var result: Result
    
    struct Meta: Codable {
        
        var start: Date
        
        var end: Date?
        
    }
    
    struct Result: Codable {
        
        var restaurantId: String
        
        enum CodingKeys: String, CodingKey {
            case restaurantId = "restaurant_id"
        }
        
        init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)
            restaurantId = try container.decode(String.self, forKey: .restaurantId)
        }
    }
    
}
