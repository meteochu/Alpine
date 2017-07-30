//
//  TableViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class TableViewController: UITableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        let imageView = UIImageView(image: #imageLiteral(resourceName: "Background"))
        imageView.tintColor = UIColor(white: 0.05, alpha: 0.1)
        tableView.backgroundView = imageView
    }
    
}
