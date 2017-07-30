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
    
    var deviceLocation: Location = Location(longitude: 0, latitude: 0)
    
    let group: Group
    
    let game: Game
    
    init(group: Group, game: Game) {
        self.group = group
        self.game = game
        super.init()
    }
    
    func createFirebaseObject() -> Game.Response {
        return Game.Response(location: deviceLocation,
                             value: [
                                preferredPriceRange,
                                selectedCategoryIndexes,
                                diningOptions
            ])
    }
    
}
