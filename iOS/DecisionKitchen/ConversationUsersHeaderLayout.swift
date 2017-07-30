//
//  ConversationUsersHeaderLayout.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class ConversationUsersHeaderLayout: UICollectionViewFlowLayout {

    override init() {
        super.init()
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    func commonInit() {
        self.minimumLineSpacing = 4
        self.minimumInteritemSpacing = 4
        self.scrollDirection = .horizontal
        self.sectionInset = .zero
    }
    
}
