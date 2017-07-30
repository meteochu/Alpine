//
//  OnboardingViewController.swift
//  DecisionKitchen
//
//  Created by Andy Liang on 2017-07-29.
//  Copyright © 2017 Andy Liang. All rights reserved.
//

import UIKit
import Firebase
import FacebookLogin

class OnboardingViewController: BackgroundViewController, LoginButtonDelegate {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let loginButton = LoginButton(readPermissions: [.publicProfile, .email])
        loginButton.delegate = self
        loginButton.center = self.view.center
        self.view.addSubview(loginButton)
    }
    
    func loginButtonDidCompleteLogin(_ loginButton: LoginButton, result: LoginResult) {
        switch result {
        case .success(_, _, let token):
            let credential = FacebookAuthProvider.credential(withAccessToken: token.authenticationToken)
            Auth.auth().signIn(with: credential) { user, error in
                if let user = user {
                    let reference = Database.database().reference().child("users")
                    reference.child(user.uid).child("img").setValue(user.photoURL!.absoluteString)
                    reference.child(user.uid).child("id").setValue(user.uid)
                    reference.child(user.uid).child("email").setValue(user.email!)
                    let fullName = user.displayName!
                    reference.child(user.uid).child("name").setValue(fullName)
                    let components = fullName.components(separatedBy: " ")
                    if components.count == 1 {
                        reference.child(user.uid).child("first_name").setValue(components.first!)
                        reference.child(user.uid).child("last_name").setValue("")
                    } else if components.count > 1 {
                        reference.child(user.uid).child("first_name").setValue(components.first!)
                        reference.child(user.uid).child("last_name").setValue(components.last!)
                    }
                } else if let error = error {
                    print(error)
                }
            }
        default:
            break
        }
        
    }
    
    func loginButtonDidLogOut(_ loginButton: LoginButton) {
        // Action for logout...
    }
    
}
