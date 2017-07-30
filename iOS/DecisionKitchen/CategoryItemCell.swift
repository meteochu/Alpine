//
//  CategoryItemCell.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class CategoryItemCell: UICollectionViewCell {
    
    var category: Category! {
        didSet {
            self.textLabel.text = category.rawValue
        }
    }
    
    let textLabel = UILabel()
    
    override var isSelected: Bool {
        didSet {
            self.backgroundColor = self.isSelected ? .purpleTint : .buttonGrey
            textLabel.textColor  = self.isSelected ? .white : .darkGray
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    func commonInit() {
        self.layer.cornerRadius = 8
        self.layer.masksToBounds = true
        self.backgroundColor = .buttonGrey
        textLabel.textColor  = .darkGray
        textLabel.font = UIFont.boldSystemFont(ofSize: 17)
        textLabel.textAlignment = .center
        contentView.addSubview(textLabel)
        textLabel.translatesAutoresizingMaskIntoConstraints = false
        textLabel.leadingAnchor.constraint(equalTo: self.leadingAnchor).isActive = true
        textLabel.trailingAnchor.constraint(equalTo: self.trailingAnchor).isActive = true
        textLabel.topAnchor.constraint(equalTo: self.topAnchor).isActive = true
        textLabel.bottomAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let textSize = self.textLabel.sizeThatFits(layoutAttributes.bounds.size)
        layoutAttributes.size = CGSize(width: textSize.width + 32, height: 44)
        return layoutAttributes
    }
    
}
