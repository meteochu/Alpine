//
//  UIImageView+Initials.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-30.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit

extension UIImageView {
    
    func setImage(for string: String) {
        let initials = self.initials(from: string)
        self.image = imageSnapshot(text: initials)
    }
    
    func initials(from string: String) -> String {
        var displayString: String = ""
        var words: [String] = string.components(separatedBy: .whitespacesAndNewlines)
        
        // Get first letter of the first and last word
        if words.count > 0 {
            if words.first!.characters.count > 0 {
                // Get character range to handle emoji (emojis consist of 2 characters in sequence)
                let range = words.first!.startIndex..<words.first!.index(words.first!.startIndex, offsetBy: 1)
                let firstLetterRange = words.first!.rangeOfComposedCharacterSequences(for: range)
                displayString.append(words.first!.substring(with: firstLetterRange).uppercased())
            }
            
            if words.count >= 2 {
                var lastWord: String = words.last!
                while lastWord.characters.count == 0 && words.count >= 2 {
                    words.removeLast()
                    lastWord = words.last!
                }
                
                if words.count > 1 {
                    // Get character range to handle emoji (emojis consist of 2 characters in sequence)
                    let range = lastWord.startIndex..<lastWord.index(lastWord.startIndex, offsetBy: 1)
                    let lastLetterRange = lastWord.rangeOfComposedCharacterSequences(for: range)
                    displayString.append(lastWord.substring(with: lastLetterRange).uppercased())
                }
            }
        }
        
        return displayString
    }
    
    func imageSnapshot(text imageText: String) -> UIImage {
        return UIGraphicsImageRenderer(bounds: self.bounds).image { context in
            let path = CGPath(ellipseIn: self.bounds, transform: nil)
            let cgContext = context.cgContext
            let attributes = [NSForegroundColorAttributeName: UIColor.white,
                              NSFontAttributeName: UIFont.boldSystemFont(ofSize: 17)]
            cgContext.addPath(path)
            cgContext.clip()
            cgContext.setFillColor(UIColor.random().cgColor)
            cgContext.fill(CGRect(origin: .zero, size: self.bounds.size))
            
            let aString = imageText as NSString
            let textSize: CGSize = aString.size(attributes: attributes)
            aString.draw(in: CGRect(x: self.bounds.midX - textSize.width / 2, y: self.bounds.midY - textSize.height / 2,
                                    width: textSize.width, height: textSize.height),
                         withAttributes: attributes)
        }
    }
    
}
