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
    
    private var selectedIndex: Int = 0
    
    private var fetchToken: UInt?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(GroupDetailCell.self)
        guard let _ = Auth.auth().currentUser else {
            return
        }
        
        fetchToken = DataController.shared.fetchGroups { groups in
            if let groups = groups {
                self.groups = groups
            }
            self.tableView.reloadData()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Auth.auth().currentUser == nil {
            self.performSegue(withIdentifier: "presentLoginView", sender: self)
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
        self.selectedIndex = indexPath.row
        self.performSegue(withIdentifier: "showGroupConversation", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showGroupConversation" {
            if let destination = segue.destination as? GroupConversationViewController {
                destination.group = self.groups[selectedIndex]
            }
        }
    }
    
    deinit {
        if let token = fetchToken {
            DataController.shared.stop(handle: token)
        }
    }
    
}

