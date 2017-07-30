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
    
    var isComplete: Bool = false {
        didSet {
            self.homeButton?.isEnabled = true
        }
    }
    
    var response: GameResponse! {
        didSet {
            self.submitResponse()
        }
    }
    
    func submitResponse() {
        DataController.shared.addGameResponse(response: self.response) { [weak self] in
            self?.isComplete = true
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.homeButton.isEnabled = self.isComplete
    }

    @IBAction func didSelectHomeButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

}
