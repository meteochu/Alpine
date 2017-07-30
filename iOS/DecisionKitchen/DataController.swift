//
//  DataController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import Foundation
import Firebase
import FirebaseDatabase

class DataController: NSObject {
    
    static var shared = DataController()
    
    private var groups: [Group] = []
    
    private(set) var users: [UserID: User] = [:]
    
    private(set) var restaurants: [String: Restaurant] = [:]
    
    // JSON decoder
    private let decoder: JSONDecoder = {
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601
        return decoder
    }()
    
    func fetchGroups(with callback: @escaping ([Group]?) -> Void) {
        let reference = Database.database().reference()
        reference.child("groups").observeSingleEvent(of: .value, with: { [weak self] snapshot in
            guard let object = snapshot.value as? [String: Any], let decoder = self?.decoder else {
                return callback(nil)
            }
            do {
                var groups = [Group]()
                for (_, value) in object {
                    let data = try JSONSerialization.data(withJSONObject: value, options: [])
                    let group = try decoder.decode(Group.self, from: data)
                    groups.append(group)
                    self?.add(group)
                }
                callback(groups)
            } catch {
                print(error)
                callback(nil)
            }
        }) { error in
            callback(nil)
        }
    }
    
    func fetchUsers(with callback: @escaping ([User]?) -> Void) {
        let reference = Database.database().reference()
        reference.child("users").observeSingleEvent(of: .value, with: { [weak self] snapshot in
            guard let object = snapshot.value as? [String: Any], let decoder = self?.decoder else {
                return callback(nil)
            }
            do {
                var users = [User]()
                for (_, value) in object {
                    let data = try JSONSerialization.data(withJSONObject: value, options: [])
                    let user = try decoder.decode(User.self, from: data)
                    users.append(user)
                    self?.add(user)
                }
                callback(users)
            } catch {
                print(error)
                callback(nil)
            }
        }) { error in
            callback(nil)
        }
    }

    func add(_ group: Group) {
        self.groups.append(group)
        
        for (id, value) in group.restaurants {
            self.restaurants[id] = value
        }
    }
    
    func add(_ user: User) {
        self.users[user.id] = user
    }
    
}
