import UIKit

@objc(RNWindowGuard)
public class RNWindowGuard: NSObject {
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
    @objc
    func requestWindowInsets(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        if let insets = extractInsets() {
            resolve(insets)
        }
    }
    
    private func extractInsets() -> [String: Any]? {
        var insets: [String: Any]? = nil
        DispatchQueue.main.sync {
            if #available(iOS 11.0, *) {
                if let window = UIApplication.shared.keyWindow, !window.safeAreaInsets.isZero {
                    insets = self.convertToWindowInsetsMap(insets: window.safeAreaInsets)
                } else {
                    insets = self.convertToWindowInsetsMap(statusBarFrame: UIApplication.shared.statusBarFrame)
                }
            } else {
                insets = self.convertToWindowInsetsMap(statusBarFrame: UIApplication.shared.statusBarFrame)
            }
        }
        
        return insets
    }
    
    private func convertToWindowInsetsMap(insets: UIEdgeInsets) -> [String: Any] {
        return [
            "leftInset": insets.left,
            "rightInset": insets.right,
            "topInset": insets.top,
            "bottomInset": insets.bottom,
            "hasNotch": insets.bottom > 0 //should be improved for landscape case for example (consider using top > 24)
        ]
    }
    
    private func convertToWindowInsetsMap(statusBarFrame frame: CGRect) -> [String: Any] {
        return [
            "leftInset": 0,
            "rightInset": 0,
            "topInset": frame.height,
            "bottomInset": 0,
            "hasNotch": false
        ]
    }
    
}

private extension UIEdgeInsets {
    var isZero: Bool {
        return left == 0 && right == 0 && top == 0 && bottom == 0
    }
}
