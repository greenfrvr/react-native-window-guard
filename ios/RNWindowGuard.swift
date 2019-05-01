import UIKit

@objc(RNWindowGuard)
class RNWindowGuard: NSObject {
    
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
                if let window = UIApplication.shared.keyWindow {
                    insets = self.convertToWindowInsetsMap(insets: window.safeAreaInsets)
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
        //TODO: we need to calculate inset for different orientation configuration based on status bar frame
        //currently app is used only in portrait orientation so such implementation is enough
        return [
            "leftInset": 0,
            "rightInset": 0,
            "topInset": frame.height,
            "bottomInset": 0,
            "hasNotch": false
        ]
    }
    
}
