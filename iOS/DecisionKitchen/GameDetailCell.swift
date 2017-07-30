//
//  GameDetailCell.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GameDetailCell: UICollectionViewCell {
    
    private let imageView = UIImageView()
    private let nameLabel = UILabel()
    private let addressLabel = UILabel()
    private let dateLabel = UILabel()

    var game: Game! {
        didSet {
            if let end = game.meta.end {
                dateLabel.text = DateFormatter.localizedString(from: end, dateStyle: .medium, timeStyle: .medium)
            } else {
                dateLabel.text = "The game is still ongoing..."
            }
            var imageName: String = "🍕 🍽"
            if let restaurantId = game.result?.last?[0], let restaurant = DataController.shared.restaurants[restaurantId] {
                nameLabel.text = restaurant.name
                addressLabel.text = restaurant.address
                imageName = restaurant.name  
            }
            self.layoutIfNeeded()
            self.imageView.setImage(for: imageName)
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: .zero)
        self.commonInit()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        imageView.layer.cornerRadius = imageView.bounds.width/2
    }
    
    func commonInit() {
        contentView.backgroundColor = UIColor(white: 0.96, alpha: 1)
        contentView.layer.cornerRadius = 8
        contentView.layer.shadowOffset = CGSize(width: 0, height: 2)
        contentView.layer.shadowRadius = 2
        contentView.layer.shadowOpacity = 0.15
        
        imageView.backgroundColor = UIColor(white: 0.92, alpha: 1)
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        
        nameLabel.font = .systemFont(ofSize: 17, weight: UIFontWeightMedium)
        addressLabel.font = .systemFont(ofSize: 14)
        addressLabel.numberOfLines = 0
        dateLabel.font = .systemFont(ofSize: 12)
        
        self.contentView.addSubview(imageView)
        self.contentView.addSubview(nameLabel)
        self.contentView.addSubview(addressLabel)
        self.contentView.addSubview(dateLabel)
        
        contentView.translatesAutoresizingMaskIntoConstraints = false
        imageView.translatesAutoresizingMaskIntoConstraints = false
        nameLabel.translatesAutoresizingMaskIntoConstraints = false
        addressLabel.translatesAutoresizingMaskIntoConstraints = false
        dateLabel.translatesAutoresizingMaskIntoConstraints = false
        
        imageView.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: 16).isActive = true
        imageView.topAnchor.constraint(equalTo: self.topAnchor, constant: 16).isActive = true
        imageView.widthAnchor.constraint(equalTo: self.widthAnchor, multiplier: 0.20).isActive = true
        imageView.widthAnchor.constraint(equalTo: imageView.heightAnchor).isActive = true
        
        nameLabel.topAnchor.constraint(equalTo: imageView.topAnchor).isActive = true
        nameLabel.leadingAnchor.constraint(equalTo: imageView.trailingAnchor, constant: 16).isActive = true
        nameLabel.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: -16).isActive = true
        
        addressLabel.leadingAnchor.constraint(equalTo: nameLabel.leadingAnchor).isActive = true
        addressLabel.topAnchor.constraint(equalTo: nameLabel.bottomAnchor, constant: 8).isActive = true
        addressLabel.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: -16).isActive = true
        
        dateLabel.leadingAnchor.constraint(equalTo: nameLabel.leadingAnchor).isActive = true
        dateLabel.topAnchor.constraint(equalTo: addressLabel.bottomAnchor, constant: 8).isActive = true
        dateLabel.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: -16).isActive = true
    }
    
}
