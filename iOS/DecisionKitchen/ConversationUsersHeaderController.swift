//
//  ConversationUsersHeaderController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

private let reuseIdentifier = "Cell"

class ConversationUsersHeaderController: UICollectionViewController {
    
    var users: [User] = [] {
        didSet {
            self.collectionView!.reloadData()
        }
    }

    convenience init() {
        self.init(collectionViewLayout: ConversationUsersHeaderLayout())
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let blurView = UIVisualEffectView(effect: UIBlurEffect(style: .light))
        self.collectionView!.backgroundView = blurView
        self.collectionView!.backgroundColor = .clear
        
        self.collectionView!.register(UserHeaderCell.self)
        self.collectionView!.reloadData()
        self.collectionView!.alwaysBounceHorizontal = true
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.collectionView!.collectionViewLayout.invalidateLayout()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        self.collectionView!.collectionViewLayout.invalidateLayout()
    }


    // MARK: UICollectionViewDataSource

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }


    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return users.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(for: indexPath) as UserHeaderCell
        cell.user = users[indexPath.row]
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let maxHeight = self.collectionView!.frame.height
        return CGSize(width: maxHeight, height: maxHeight)
    }
    

}
