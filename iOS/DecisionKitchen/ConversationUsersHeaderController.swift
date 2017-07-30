//
//  ConversationUsersHeaderController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class ConversationUsersHeaderController: CollectionViewController, UICollectionViewDelegateFlowLayout {
    
    var group: Group? {
        didSet {
            self.beginFetchingUsers()
        }
    }
    
    private var users: [User] = [] {
        didSet {
            self.collectionView!.reloadData()
        }
    }
    
    var fetchToken: UInt?

    convenience init() {
        self.init(collectionViewLayout: ConversationUsersHeaderLayout())  
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        let blurView = UIVisualEffectView(effect: UIBlurEffect(style: .light))
        self.collectionView!.backgroundView = blurView
        self.collectionView!.backgroundColor = .clear
        self.collectionView!.delegate = self
        self.collectionView!.dataSource = self
        
        self.collectionView!.register(UserHeaderCell.self)
        self.collectionView!.reloadData()
        self.collectionView!.alwaysBounceHorizontal = true
        
        let divider = UIView()
        divider.backgroundColor = UIColor(white: 0, alpha: 0.2)
        self.view.addSubview(divider)
        divider.translatesAutoresizingMaskIntoConstraints = false
        
        divider.heightAnchor.constraint(equalToConstant: 1/UIScreen.main.scale).isActive = true
        divider.widthAnchor.constraint(equalTo: self.view.widthAnchor).isActive = true
        divider.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        divider.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        divider.bottomAnchor.constraint(equalTo: self.view.bottomAnchor).isActive = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.collectionView!.collectionViewLayout.invalidateLayout()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        self.collectionView!.collectionViewLayout.invalidateLayout()
    }
    
    // fetching...
    func beginFetchingUsers() {
        guard let groupId = group?.id else { return }
        fetchToken = DataController.shared.fetchGroup(with: groupId, callback: { [weak self] group in
            if let group = group {
                self?.users = group.members.map { DataController.shared.users[$0]! }
            }
        })
        
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
    
    deinit {
        if let token = fetchToken {
            DataController.shared.stop(handle: token)
        }
    }
    

}
