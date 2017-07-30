//
//  GameDiningOptionsViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

enum DiningOption {
    case dineIn
    case takeOut
}

class GameDiningOptionsViewController: UIViewController {

    @IBOutlet weak var dineInButton: UIButton!
    
    @IBOutlet weak var takeOutButton: UIButton!
    
    var diningOption: DiningOption = .dineIn
    
    @IBAction func didSelectDiningOptionButton(_ sender: UIButton) {
        if sender.tag == 1 {
            dineInButton.backgroundColor = .purpleTint
            dineInButton.setTitleColor(.white, for: .normal)
            takeOutButton.backgroundColor = .buttonGrey
            takeOutButton.setTitleColor(.darkGray, for: .normal)
            self.diningOption = .dineIn
        } else {
            takeOutButton.backgroundColor = .purpleTint
            takeOutButton.setTitleColor(.white, for: .normal)
            dineInButton.backgroundColor = .buttonGrey
            dineInButton.setTitleColor(.darkGray, for: .normal)
            self.diningOption = .takeOut
        }
        
    }
    
}
