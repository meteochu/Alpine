//
//  GameResultViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GameResultViewController: UIViewController {

    
    @IBOutlet weak var homeButton: UIButton!
    
    var response: GameResponse! {
        didSet {
            self.submitResponse()
        }
    }
    
    func submitResponse() {
        DataController.shared.addGameResponse(response: self.response) {
            self.homeButton.isEnabled = true
        }
    }

    @IBAction func didSelectHomeButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

}
