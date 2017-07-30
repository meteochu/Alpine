//
//  UIColor+Random.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

extension UIColor {
    
    private static func randomNumber() -> CGFloat {
        let limit: UInt32 = 180
        return CGFloat(32 + Int(arc4random_uniform(limit)))
    }
    
    static func random() -> UIColor {
        return UIColor(red: randomNumber()/255.0, green: randomNumber()/255.0, blue: randomNumber()/255.0, alpha: 1.0)
    }
    
}
