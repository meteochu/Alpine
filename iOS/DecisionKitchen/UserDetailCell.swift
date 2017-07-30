//
//  UserDetailCell.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class UserDetailCell: UITableViewCell {

    var user: User! {
        didSet {
            guard let user = self.user else { return }
            self.textLabel!.text = user.name
            self.detailTextLabel!.text = user.email
            self.imageView?.setImage(with: user.img) {
                return self.user.name == user.name
            }
            self.layoutIfNeeded()
        }
    }
    
    override init(style: UITableViewCellStyle, reuseIdentifier: String?) {
        super.init(style: .subtitle, reuseIdentifier: reuseIdentifier)
        self.imageView!.contentMode = .scaleAspectFill
        self.imageView!.clipsToBounds = true
        self.imageView!.image = #imageLiteral(resourceName: "RoundedUtensils")
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.imageView!.frame.size = CGSize(width: 44, height: 44)
        self.imageView!.layer.cornerRadius = 22
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        self.accessoryType = selected ? .checkmark : .none
    }
    
}
