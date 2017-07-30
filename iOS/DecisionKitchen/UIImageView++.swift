//
//  UIImageView++.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

extension UIImageView {
    
    func setImage(with url: URL, validator: @escaping () -> Bool) {
        DispatchQueue.global(qos: .background).async {
            if let data = try? Data(contentsOf: url) {
                let image = UIImage(data: data)
                DispatchQueue.main.async {
                    if validator() {
                        self.image = image
                    }
                }
            }
        }
        
    }
    
}
