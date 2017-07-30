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
    
    private var groups: [String: Group] = [:]
    
    private(set) var users: [UserID: User] = [:]
    
    private(set) var restaurants: [String: Restaurant] = [:]
    
    // JSON decoder
    private let decoder: JSONDecoder = {
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601
        return decoder
    }()
    
    private let encoder: JSONEncoder = {
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .iso8601
        return encoder
    }()
    
    func fetchGroups(with callback: @escaping ([Group]?) -> Void) -> UInt {
        let reference = Database.database().reference()
        return reference.child("groups").observe(.value, with: { [weak self] snapshot in
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
    
    func fetchGroupsOnce(with callback: @escaping ([Group]?) -> Void) {
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
    
    func fetchUsers(with callback: @escaping ([User]?) -> Void) -> UInt {
        let reference = Database.database().reference()
        return reference.child("users").observe(.value, with: { [weak self] snapshot in
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
    
    func fetchUsersOnce(with callback: @escaping ([User]?) -> Void) {
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
    
    func fetchGroup(with groupId: String, callback: @escaping (Group?) -> Void) -> UInt {
        let databaseRef = Database.database().reference()
        return databaseRef.child("groups").child(groupId).observe(.value, with: { [weak self] snapshot in
            guard let object = snapshot.value as? [String: Any], let decoder = self?.decoder else {
                return callback(nil)
            }
            do {
                let data = try JSONSerialization.data(withJSONObject: object, options: [])
                let group = try decoder.decode(Group.self, from: data)
                self?.add(group)
                callback(group)
            } catch {
                print(error)
                callback(nil)
            }
        }) { error in
            callback(nil)
        }
    }
    
    func stop(handle: UInt) {
        Database.database().reference().removeObserver(withHandle: handle)
    }
    
    func createOrJoinGroup(named name: String, password: String, callback: (Bool) -> Void) {
        let databaseRef = Database.database().reference()
        var group: Group?
        for aGroup in self.groups.values where aGroup.name == name && aGroup.password == password {
            // join group
            group = aGroup
            break
        }
        
        if let group = group {
            group.members.append(Auth.auth().currentUser!.uid)
        } else {
            let uuid = UUID().uuidString
            group = Group(name: name, password: password, members: [Auth.auth().currentUser!.uid],
                          restaurants: [:], games: [], id: uuid)
        }
        
        if let encodedUser = try? encoder.encode(group),
            let json = try? JSONSerialization.jsonObject(with: encodedUser, options: []) {
            databaseRef.child("groups").child(group!.id).setValue(json)
            callback(true)
        } else {
            callback(false)
        }
    }
    
    func createGame(in group: Group, callback: (Game) -> Void) {
        let databaseRef = Database.database().reference()
        let group = group
        let meta = Game.Meta(start: Date())
        let game = Game(meta: meta, rating: [:], response: [:], result: nil)
        if group.games == nil {
            group.games = []
        }
        group.games!.append(game)
        
        if let encodedGroup = try? encoder.encode(group),
            let json = try? JSONSerialization.jsonObject(with: encodedGroup, options: []) {
            databaseRef.child("groups").child(group.id).setValue(json)
            callback(game)
        }
    }
    
    func addGameResponse(response: GameResponse, callback: () -> Void) {
        let databaseRef = Database.database().reference()
        let group = response.group
        let game = response.game
        let uid = Auth.auth().currentUser!.uid
        if game.responses == nil {
            game.responses = [:]
        }
        game.responses![uid] = response.createFirebaseObject()
        if let index = group.games!.index(where: { $0.meta.start == game.meta.start }) {
            group.games![index] = game
        }
        
        if let encodedGroup = try? encoder.encode(group),
            let json = try? JSONSerialization.jsonObject(with: encodedGroup, options: []) {
            databaseRef.child("groups").child(group.id).setValue(json)
            callback()
        }
    }

    func add(_ group: Group) {
        self.groups[group.id] = group
        
        for (id, value) in group.restaurants ?? [:] {
            self.restaurants[id] = value
        }
    }
    
    func add(_ user: User) {
        self.users[user.id] = user
    }
    
}
