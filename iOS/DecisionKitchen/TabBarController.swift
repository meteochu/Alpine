//
//  TabBarController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit
import FirebaseAuthUI

class TabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Auth.auth().currentUser == nil {
            self.performSegue(withIdentifier: "presentLoginView", sender: self)
        }
    }
    
}
