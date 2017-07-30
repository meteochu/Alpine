//
//  Category.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

enum CategoryType: String {
    case region
    case price
    case other
}

struct Category: Codable {
    
    var name: String
    
    var type: CategoryType
    
    var stringValue: String?
    
    var intValue: Int?
    
    enum CodingKeys: String, CodingKey {
        case name
        case value
        case type
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        name = try container.decode(String.self, forKey: .name)
        let rawType = try container.decode(String.self, forKey: .type)
        
        type = CategoryType(rawValue: rawType) ?? .other
        if case .price = type {
            intValue = try container.decode(Int.self, forKey: .value)
        } else {
            stringValue = try container.decode(String.self, forKey: .value)
        }
        
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(name, forKey: .name)
        try container.encode(type.rawValue, forKey: .type)
        if let stringValue = stringValue {
            try container.encode(stringValue, forKey: .value)
        } else {
            try container.encode(intValue, forKey: .value)
        }
    }
    
}
