//
//  CreateGroupViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class CreateGroupViewController: UITableViewController, UITextFieldDelegate {
    
    private var name: String = ""
    
    @IBAction func textFieldValueDidChange(_ sender: UITextField) {
        name = sender.text!
    }
    
    @IBAction func didSelectDismiss(_ sender: UIBarButtonItem) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func didSelectSaveButton(_ sender: UIBarButtonItem) {
        DataController.shared.createGroup(named: name) { _ in
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    
}
