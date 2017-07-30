//
//  GameResponse.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

class GameResponse: NSObject {
    
    var diningOptions: [Int]  = []
    
    var selectedCategoryIndexes: [Int] = []
    
    var preferredPriceRange: [Int] = []
    
    var restaurantName: String = ""
    
    let group: Group
    
    init(group: Group) {
        self.group = group
        super.init()
    }
    
}
