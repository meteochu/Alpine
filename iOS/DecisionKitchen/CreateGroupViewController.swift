//
//  CreateGroupViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class CreateGroupViewController: TableViewController, UITextFieldDelegate {
    
    private var name: String = ""
    
    private var password: String = ""
    
    @IBAction func textFieldValueDidChange(_ sender: UITextField) {
        if sender.tag == 0 {
            name = sender.text!
        } else if sender.tag == 1 {
            password = sender.text!
        }
    }
    
    @IBAction func didSelectDismiss(_ sender: UIBarButtonItem) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func didSelectSaveButton(_ sender: UIBarButtonItem) {
        DataController.shared.createOrJoinGroup(named: name, password: password) { _ in
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    
}
