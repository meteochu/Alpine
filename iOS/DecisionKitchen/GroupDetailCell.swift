//
//  GroupDetailCell.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GroupDetailCell: UITableViewCell {
    
    var group: Group! {
        didSet {
            self.imageView?.setImage(for: group.name)
            self.textLabel?.text = group.name
            if let games = group.games, let game = games.first, !games.isEmpty {
                let restaurant = DataController.shared.restaurants[game.result.restaurantId]!.name
                if let endDate = game.meta.end {
                    self.detailTextLabel!.text = "Last Vote: \(restaurant) at \(DateFormatter.localizedString(from: endDate, dateStyle: .short, timeStyle: .short))"
                } else {
                    self.detailTextLabel!.text = "The current game for is still ongoing... 🍱"
                }
            } else {
                self.detailTextLabel!.text = "Looks like you haven't played any games yet. Create one today! 🍕"
            }
        }
    }

    override init(style: UITableViewCellStyle, reuseIdentifier: String?) {
        super.init(style: .subtitle, reuseIdentifier: reuseIdentifier)
        self.accessoryType = .disclosureIndicator
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.commonInit()
    }
    
    func commonInit() {
        self.imageView!.frame.size = CGSize(width: 44, height: 44)
        self.textLabel!.font = .boldSystemFont(ofSize: 18)
        self.detailTextLabel!.font = .systemFont(ofSize: 14)
        self.detailTextLabel!.numberOfLines = 0
    }
    
}
