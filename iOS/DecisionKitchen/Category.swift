//
//  Category.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

enum Category: String {
    case breakfastAndBrunch = "Breakfast & Brunch"
    case chinese = "Chinese"
    case diners = "Diners"
    case fastFood = "Fast Food"
    case hotpot = "Hot Pot"
    case italian = "Italian"
    case japanese = "Japanese"
    case korean = "Korean"
    case mongolian = "Mongolian"
    case pizza = "Pizza"
    case steakhouses = "Steakhouses"
    case sushiBars = "Sushi Bars"
    case american = "American (Traditional)"
    case vegetarian = "Vegetarian"
    
    static var allOptions: [Category] = [
        .breakfastAndBrunch, .chinese, .diners, .fastFood,
        .hotpot, .italian, .japanese, .korean, .mongolian,
        .pizza, .steakhouses, .sushiBars, .american, .vegetarian
    ]
}
