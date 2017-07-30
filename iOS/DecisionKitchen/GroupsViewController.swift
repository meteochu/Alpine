//
//  GroupsViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit
import Firebase
import FirebaseDatabase

class GroupsViewController: UITableViewController {

    private lazy var databaseRef = Database.database().reference()
    
    private var groups: [Group] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(GroupDetailCell.self)
        
        databaseRef.child("groups").observeSingleEvent(of: .value, with: { snapshot in
            if let object = snapshot.value as? [String: Any] {
                do {
                    let decoder = JSONDecoder()
                    decoder.dateDecodingStrategy = .iso8601
                    for (key, value) in object {
                        let data = try JSONSerialization.data(withJSONObject: value, options: [])
                        var group = try decoder.decode(Group.self, from: data)
                        group.name = key
                        self.groups.append(group)
                    }
                    self.tableView.reloadData()
                } catch {
                    print(error)
                }
            }
        }) { error in
            print(error)
        }
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return groups.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(for: indexPath) as GroupDetailCell
        cell.group = groups[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }

}
