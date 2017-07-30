//
//  Game.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

struct Game: Codable {
    
    var meta: Meta
    
    var rating: [UserID: Int]?
    
    // TODO: Update to the current schema.. zzz
    var responses: [UserID: [[Int]]]?
    
    var result: Result?
    
    struct Meta: Codable {
        
        let start: Date
        
        var end: Date?
        
        init(start: Date) {
            self.start = start
        }
        
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
