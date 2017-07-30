//
//  GameRestaurantViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GameRestaurantViewController: UIViewController {
    
    var restaurant: String = ""

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func textFieldDidChange(_ sender: UITextField) {
        restaurant = sender.text!
    }
    
    @IBAction func didSelectCheckmark(_ sender: UIButton) {
        self.performSegue(withIdentifier: "beginGameStage2", sender: self)
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
