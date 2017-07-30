//
//  GameLobbyViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GameLobbyViewController: UITableViewController {

    var group: Group! {
        didSet {
            self.users = group.members.map { DataController.shared.users[$0]! }
            self.response = GameResponse(group: group)
            tableView.reloadData()
        }
    }
    
    var users: [User] = []
    
    var response: GameResponse!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(UITableViewCell.self)
        tableView.register(UserDetailCell.self)
        tableView.allowsMultipleSelection = true
    }
    
    @IBAction func didSelectLeaveButton(_ sender: UIBarButtonItem) {
        self.dismiss(animated: true, completion: nil)
    }
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return section == 0 ? users.count : 1
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard indexPath.section == 0 else {
            let cell = tableView.dequeueReusableCell(for: indexPath) as UITableViewCell
            cell.textLabel!.text = "Send Notification..."
            cell.textLabel!.textAlignment = .center
            cell.textLabel!.textColor = self.view.tintColor
            return cell
        }
        let cell = tableView.dequeueReusableCell(for: indexPath) as UserDetailCell
        cell.user = self.users[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard indexPath.section == 1 else { return }
        tableView.deselectRow(at: indexPath, animated: true)
        guard let indexPaths = tableView.indexPathsForSelectedRows, !indexPaths.isEmpty else { return }
        let selectedUsers = indexPaths.map { self.users[$0.row] }
        print(selectedUsers)
        self.performSegue(withIdentifier: "beginGameStage1", sender: self)
    }

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? GameRestaurantViewController {
            destination.response = self.response
        }
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    

}
