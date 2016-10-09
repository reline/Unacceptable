//
//  ViewController.swift
//  Unacceptable
//
//  Created by Nathan Reline on 10/8/16.
//  Copyright © 2016 Nathan Reline. All rights reserved.
//

import UIKit
import AVFoundation

class ViewController: UIViewController {

    // MARK: Properties
    @IBOutlet weak var lemonImageView: UIImageView!
    var timer = Timer()
    var audioPlayer = AVAudioPlayer()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let music = Bundle.main.path(forResource: "lemon_grab_unacceptable", ofType: "mp3")
        let url = URL(fileURLWithPath: music!)
        
        do {
            audioPlayer = try AVAudioPlayer(contentsOf: url)
        }
        catch{
            print(error)
        }
        
        let tap = UITapGestureRecognizer(target: self, action:#selector(ViewController.onTap))
        lemonImageView.addGestureRecognizer(tap)
        lemonImageView.isUserInteractionEnabled = true
    }
    
    func initTimer() -> Void {
        Timer.scheduledTimer(timeInterval: 3,
                                     target: self,
                                     selector: #selector(resetLemon),
                                     userInfo: nil,
                                     repeats: false)
    }
    
    // MARK: Actions
    func onTap() {
        if lemonImageView.isAnimating == false {
            lemonImageView.image = #imageLiteral(resourceName: "lemongrab")
            
            audioPlayer.play()
            
            let animation = CABasicAnimation(keyPath: "position")
            animation.duration = 0.15
            animation.repeatCount = 10
            animation.autoreverses = true
            animation.fromValue = NSValue(cgPoint: CGPoint(x: lemonImageView.center.x - 10, y: lemonImageView.center.y))
            animation.toValue = NSValue(cgPoint: CGPoint(x: lemonImageView.center.x + 10, y: lemonImageView.center.y))
            lemonImageView.layer.add(animation, forKey: "position")
            
            Timer.scheduledTimer(timeInterval: 3,
                                 target: self,
                                 selector: #selector(resetLemon),
                                 userInfo: nil,
                                 repeats: false)
            
//            UIView.animate(withDuration: 0.3, delay: 0.0, options: UIViewAnimationOptions.curveLinear, animations: {
//                
//                // put here the code you would like to animate
//                self.lemonImageView.center = CGPoint(x: self.lemonImageView.center.x + 2, y: self.lemonImageView.center.y)
//                    
//                }, completion: {(finished:Bool) in
//                    // the code you put here will be compiled once the animation finishes
//                    self.lemonImageView.image = #imageLiteral(resourceName: "lemon")
//            })
        }
        
    }
    
    func resetLemon() -> Void {
        lemonImageView.image = #imageLiteral(resourceName: "lemon")
    }

}

