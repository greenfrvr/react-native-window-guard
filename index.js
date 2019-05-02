import React from 'react';
import {
  View,
  StyleSheet,
  NativeModules,
} from 'react-native';

export const {RNWindowGuard} = NativeModules;

const WindowSides = Object.freeze(['left', 'right', 'top', 'bottom']);

class WindowGuard extends React.Component {

  constructor(props) {
    super(props);

    this.state = this.initialInsets();

    this.adjustInsets();
  }

  adjustInsets() {
    console.log("Adjust insets invoked");
    RNWindowGuard.requestWindowInsets()
      .then(windowInsets => {
        const {hasNotch, ...insets} = windowInsets;
        WindowGuard.hasNotch = hasNotch;
        WindowGuard.insets = insets; //need some more smart caching

        console.log("Got window insets");
        console.log(insets);

        this.setState(insets);
      });
  }

  initialInsets() {
    const {
      leftInset = 0,
      topInset = 0,
      rightInset = 0,
      bottomInset = 0
    } = WindowGuard.insets || {};

    return {
      leftInset,
      topInset,
      rightInset,
      bottomInset,
    }
  }

  defineStyle = (style) => {
    const {applyInsets} = this.props;

    let sizeStyle = {};

    if (applyInsets) {
      WindowSides.forEach(key => {
        const needsApply = applyInsets.includes(key);
        const attrKey = `padding${key.charAt(0).toUpperCase()}${key.slice(1)}`;
        const insetKey = `${key}Inset`;
        console.log(`Inset ${needsApply ? 'will' : 'won\'t'} be applied for: ${key}`);

        sizeStyle[attrKey] = (needsApply && this.state[insetKey]) || 0;
        sizeStyle[attrKey] += (style[attrKey] || 0); //here we apply padding from user defined style
      });

      if (style.height && typeof style.height === 'number') {
        sizeStyle.height = style.height + sizeStyle.paddingTop + sizeStyle.paddingBottom;
      }

      if (style.width && typeof style.width === 'number') {
        sizeStyle.width = style.width + sizeStyle.paddingLeft + sizeStyle.paddingRight;
      }
    }

    return Object.assign({}, style, sizeStyle)
  };

  render() {
    const {style, ...props} = this.props;
    const adjustedStyle = this.defineStyle(StyleSheet.flatten(style || {}));
    
    return (
      //perhaps it also makes sense to check if view touches display boundaries before applying padding (just as it is implemented in SafeAreaView)
      <View style={adjustedStyle} {...props}/>
    )
  }
}

//hasNotch can't be changed during runtime, so this method can be useful to know if device has notch
WindowGuard.requestWindowInsets = () => {
  RNWindowGuard.requestWindowInsets()
    .then(windowInsets => {
      const {hasNotch, ...insets} = windowInsets;
      WindowGuard.hasNotch = hasNotch;
      WindowGuard.insets = insets;
    });
};

export default WindowGuard;
