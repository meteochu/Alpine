//
//  GroupConversationViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GroupConversationViewController: UICollectionViewController, UICollectionViewDelegateFlowLayout {
    
    var group: Group! {
        didSet {
            self.title = group.name
        }
    }
    
    private lazy var usersHeader = ConversationUsersHeaderView()
    
    override func loadView() {
        super.loadView()
        
        self.view.addSubview(usersHeader)
        usersHeader.translatesAutoresizingMaskIntoConstraints = false
        usersHeader.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        usersHeader.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        usersHeader.topAnchor.constraint(equalTo: self.view.topAnchor).isActive = true
        usersHeader.heightAnchor.constraint(equalToConstant: 44).isActive = true

        let insets = UIEdgeInsets(top: 8, left: 0, bottom: 0, right: 0)
        self.collectionView!.contentInset = insets
        self.collectionView!.scrollIndicatorInsets = insets
        
        self.collectionView!.alwaysBounceVertical = true
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.collectionView!.register(GameDetailCell.self)
        self.collectionView!.reloadData()
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
        return group.games.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(for: indexPath) as GameDetailCell
        cell.game = group.games[indexPath.row]
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let maxWidth = self.collectionView!.bounds.width-32
        return CGSize(width: maxWidth, height: 120)
    }
    
}
