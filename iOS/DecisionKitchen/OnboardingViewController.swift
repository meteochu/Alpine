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

class OnboardingViewController: UIViewController, LoginButtonDelegate {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let loginButton = LoginButton(readPermissions: [.publicProfile, .email])
        loginButton.delegate = self
        loginButton.center = self.view.center
        self.view.addSubview(loginButton)
    }
    
    func loginButtonDidCompleteLogin(_ loginButton: LoginButton, result: LoginResult) {
        print(#function)
        switch result {
        case .success(_, _, let token):
            let credential = FacebookAuthProvider.credential(withAccessToken: token.authenticationToken)
            Auth.auth().signIn(with: credential) { user, error in
                if let error = error {
                    print(error)
                } else if let user = user {
                    print(user, user.displayName ?? "NO DISPLAY NAME")
                }
            }
        default:
            break
        }
        
    }
    
    func loginButtonDidLogOut(_ loginButton: LoginButton) {
        print("Did Logout")
    }
    
}
