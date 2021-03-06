//
//  GroupConversationViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit
import FirebaseAuth

class GroupConversationViewController: CollectionViewController, UICollectionViewDelegateFlowLayout {
    
    var group: Group! {
        didSet {
            self.title = group.name
        }
    }
    
    var selectedGame: Game?
    
    private lazy var usersHeaderController = ConversationUsersHeaderController()
    
    private var fetchToken: UInt?
    
    override func loadView() {
        super.loadView()
        let imageView = UIImageView(image: #imageLiteral(resourceName: "Background"))
        imageView.tintColor = UIColor(white: 0.05, alpha: 0.1)
        collectionView!.backgroundView = imageView
        
        // header
        let usersHeader = UIView()
        self.view.addSubview(usersHeader)
        usersHeader.translatesAutoresizingMaskIntoConstraints = false
        usersHeader.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        usersHeader.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        usersHeader.topAnchor.constraint(equalTo: self.topLayoutGuide.bottomAnchor).isActive = true
        usersHeader.heightAnchor.constraint(equalToConstant: 100).isActive = true
        
        self.addChildViewController(usersHeaderController)
        usersHeaderController.view.frame = usersHeader.bounds
        usersHeaderController.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        usersHeader.addSubview(usersHeaderController.view)
        usersHeaderController.didMove(toParentViewController: self)
        // footer
        let newGamesFooter = UIVisualEffectView(effect: UIBlurEffect(style: .light))
        self.view.addSubview(newGamesFooter)
        newGamesFooter.translatesAutoresizingMaskIntoConstraints = false
        newGamesFooter.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        newGamesFooter.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        newGamesFooter.bottomAnchor.constraint(equalTo: self.bottomLayoutGuide.topAnchor).isActive = true
        newGamesFooter.heightAnchor.constraint(equalToConstant: 80).isActive = true
        
        let button = UIButton(type: .custom)
        button.contentEdgeInsets = UIEdgeInsets(top: 4, left: 4, bottom: 4, right: 4)
        button.backgroundColor = .purpleTint
        button.layer.cornerRadius = 8
        button.layer.masksToBounds = true
        button.setTitleColor(.white, for: .normal)
        button.setTitle("Start Vote ", for: .normal)
        button.contentEdgeInsets = UIEdgeInsets(top: 8, left: 16, bottom: 8, right: 16)
        button.sizeToFit()
        newGamesFooter.contentView.addSubview(button)
        
        button.translatesAutoresizingMaskIntoConstraints = false
        button.centerXAnchor.constraint(equalTo: newGamesFooter.centerXAnchor).isActive = true
        button.bottomAnchor.constraint(equalTo: newGamesFooter.bottomAnchor, constant: -16).isActive = true
        button.addTarget(self, action: #selector(didSelectNewGameButton), for: .touchUpInside)
        
        let insets = UIEdgeInsets(top: 108, left: 0, bottom: 120, right: 0)
        self.collectionView!.contentInset = insets
        self.collectionView!.scrollIndicatorInsets = insets
        self.collectionView!.alwaysBounceVertical = true
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.collectionView!.register(GameDetailCell.self)
        self.collectionView!.reloadData()
        
        fetchToken = DataController.shared.fetchUsers { [weak self] _ in
            self?.usersHeaderController.group = self?.group
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.collectionView!.collectionViewLayout.invalidateLayout()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        self.collectionView!.collectionViewLayout.invalidateLayout()
    }
    
    @objc func didSelectNewGameButton(sender: UIBarButtonItem) {
        // trigger new game...
        self.performSegue(withIdentifier: "beginGame", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "beginGame" {
            if let destination = segue.destination as? UINavigationController,
                let rootVC = destination.topViewController as? GameLobbyViewController {
                rootVC.group = self.group
                rootVC.game = self.selectedGame
            }
        }
        
    }

    // MARK: UICollectionViewDataSource

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return group.games?.count ?? 0
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(for: indexPath) as GameDetailCell
        cell.game = group.games![indexPath.row]
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let maxWidth = self.collectionView!.bounds.width-8
        return CGSize(width: maxWidth, height: 120)
    }
    
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let game = group.games![indexPath.row]
        if game.responses == nil || !game.responses!.keys.contains(Auth.auth().currentUser!.uid) {
            // begin game...
            self.selectedGame = game
            self.performSegue(withIdentifier: "beginGame", sender: self)
        }
    }
    
    deinit {
        if let token = fetchToken {
            DataController.shared.stop(handle: token)
        }
    }
    
}
