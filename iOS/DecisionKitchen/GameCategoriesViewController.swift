//
//  GameCategoriesViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

class GameCategoriesViewController: UICollectionViewController, UICollectionViewDelegateFlowLayout {
    
    let allCategories = Category.allOptions
    
    var selectedCategories: [Category] {
        return self.collectionView!.indexPathsForSelectedItems!.map { self.allCategories[$0.item] }
    }
    
    var response: GameResponse!

    override func viewDidLoad() {
        super.viewDidLoad()
        self.collectionView!.allowsMultipleSelection = true
        self.collectionView!.register(CategoryItemCell.self)
        if let layout = self.collectionViewLayout as? UICollectionViewFlowLayout {
            layout.estimatedItemSize = CGSize(width: 150, height: 45)
            layout.minimumInteritemSpacing = 8
            layout.minimumLineSpacing = 8
        }
    }
    
    @IBAction func didSelectCheckmark(_ sender: UIButton) {
        var indexes = [Int]()
        let selected = selectedCategories
        for idx in 0..<allCategories.count {
            indexes.append(selected.contains(allCategories[idx]) ? 1 : 0)
        }
        response.selectedCategoryIndexes = indexes
        self.performSegue(withIdentifier: "beginGameStage3", sender: self)
    }
    
    // MARK: UICollectionViewDataSource

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.allCategories.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        if kind == UICollectionElementKindSectionHeader {
            return collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: "HeaderCell", for: indexPath)
        } else {
            return collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: "FooterButton", for: indexPath)
        }
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(for: indexPath) as CategoryItemCell
        cell.category = self.allCategories[indexPath.row]
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 8, left: 16, bottom: 16, right: 16)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? GameDiningOptionsViewController {
            destination.response = response
        }
    }
    
}
