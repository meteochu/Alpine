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
    
    var selectedIndexes: [Int] = []
    
    
    @IBAction func didSelectMoneyButton(_ sender: UIButton) {
        if let index = selectedIndexes.index(of: sender.tag) {
            sender.backgroundColor = .buttonGrey
            sender.setTitleColor(.darkGray, for: .normal)
            selectedIndexes.remove(at: index)
        } else {
            sender.backgroundColor = .purpleTint
            sender.setTitleColor(.white, for: .normal)
            selectedIndexes.append(sender.tag)
        }
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
