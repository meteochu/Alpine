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
    
    var response: GameResponse!
    
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
    
    @IBAction func didSelectCheckmark(_ sender: UIButton) {
        switch self.diningOption {
        case .dineIn:
            response.diningOptions = [1, 0]
        case .takeOut:
            response.diningOptions = [0, 1]
        }
        self.performSegue(withIdentifier: "beginGameFinal", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? GameResultViewController {
            destination.response = response
        }
    }
    
    
}
