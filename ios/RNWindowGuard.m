#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RNWindowGuard, NSObject)

RCT_EXTERN_METHOD(requestWindowInsets: (RCTPromiseResolveBlock)resolve rejecter: (RCTPromiseRejectBlock)reject)

@end
