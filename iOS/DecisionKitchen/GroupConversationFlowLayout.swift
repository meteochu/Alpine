//
//  GroupConversationFlowLayout.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GroupConversationFlowLayout: UICollectionViewFlowLayout {
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    func commonInit() {
        self.minimumLineSpacing = 4
        self.minimumInteritemSpacing = 0
        self.scrollDirection = .vertical
        self.sectionInset = .zero
    }
    

}
