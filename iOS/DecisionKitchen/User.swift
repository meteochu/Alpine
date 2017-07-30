//
//  User.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation

struct User: Codable {
    
    var name: String
    
    var firstName: String
    
    var lastName: String
    
    var id: UserID
    
    var email: String
    
    var img: URL
    
    enum CodingKeys: String, CodingKey {
        case name, id, email, img
        case firstName = "first_name"
        case lastName = "last_name"
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        name = try container.decode(String.self, forKey: .name)
        firstName = try container.decode(String.self, forKey: .firstName)
        lastName = try container.decode(String.self, forKey: .lastName)
        id = try container.decode(String.self, forKey: .id)
        email = try container.decode(String.self, forKey: .email)
        img = try container.decode(URL.self, forKey: .img)
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(name, forKey: .name)
        try container.encode(firstName, forKey: .firstName)
        try container.encode(lastName, forKey: .lastName)
        try container.encode(id, forKey: .id)
        try container.encode(email, forKey: .email)
        try container.encode(img, forKey: .img)
    }
    
    
}
