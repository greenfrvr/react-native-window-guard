# react-native-window-guard

[![npm package](https://img.shields.io/github/package-json/v/greenfrvr/react-native-window-guard.svg)](https://www.npmjs.com/package/react-native-window-guard)
[![license](https://img.shields.io/github/license/greenfrvr/react-native-window-guard.svg)](https://github.com/greenfrvr/react-native-window-guard/blob/master/LICENSE)


Project represents simple way to handle notches and system ui decorations for React Native. In comparison to alternatives (e.g. `SafeAreaView`) it works on both iOS and Android and doesn't use hardcoded values, getting all insets with operating system APIs instead.

Android             |  iOS
:-------------------------:|:-------------------------:
<img src="https://github.com/greenfrvr/react-native-window-guard/blob/master/example/snapshot_android.png" height=400/>  |  <img src="https://github.com/greenfrvr/react-native-window-guard/blob/master/example/snapshot_ios.png" height=400/>

## Getting started

`$ npm install react-native-window-guard --save`

### Mostly automatic installation

`$ react-native link react-native-window-guard`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-window-guard` and add `RNWindowGuard.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNWindowGuard.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.github.greenfrvr.RNWindowGuardPackage;` to the imports at the top of the file
  - Add `new RNWindowGuardPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-window-guard'
  	project(':react-native-window-guard').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-window-guard/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-window-guard')
  	```


## Usage
Put your layout inside `WindowGuard` component and define which insets you want to be applied. To make this use `applyInsets` prop. It takes array with sides which should be affected with insets. Available values: `top`, `bottom`, `left`, `right`. After this relevant window insets will be requested from native and applied as paddings to `WindowGuard` component. 

For convenience and better perfomance there are serveral insets configurations that are predefined. They are defined statically in `WindowGuard`. Available predefined insets configurations are: 
- `left`
- `right`
- `top`
- `bottom`
- `vertical`
- `horizontal`
- `all`

Below is an example of applying insets both for top and bottom sides of your content: 

```javascript
import WindowGuard from 'react-native-window-guard';

export default class App extends React.Component {

  componentDidMount() {
    WindowGuard.requestWindowInsets();
  }

  render() {
    return (
      <WindowGuard
        style={{flex: 1}}
        applyInsets={WindowGuard.vertical}>
        //content
      </WindowGuard>
    );
  }
}
```
Notice that you can still add paddings to `WindowGuard` and they will be added to applied window insets. Currently all paddings definitions are supported includeing `paddingHorizontal`, `paddingVertical` and `padding` attributes.

## Dynamic changes

Window guard will handle orientation changes and apply new relevant insets automatically for you. Unfortunately there are still cases where you need to handle some ui changes manually. For example hiding status bar. You can request window guard to refresh insets after configuation change by calling `adjustInsets` method. Below is small usage example:

 ```javascript
import WindowGuard from 'react-native-window-guard';

export default class App extends React.Component {

  toggleStatusBar = () => {
    //change statusBarHidden state
    StatusBar.setHidden(statusBarHidden, true);       //change system ui views state
    this.container && this.container.adjustInsets()   //request to refresh insets values
  };

  render() {
    return (
      <WindowGuard
        ref={r => this.container = r}
        style={{flex: 1}}
        applyInsets={WindowGuard.all}>
        //content
      </WindowGuard>
    );
  }
}
```

## HOC 
For convenience there is HOC wich will simplify `WindowGuard` usage. `withWindowGuard` HOC will return component wrapped into `WindowGuard` with defined insets configuration. For example 

`const GuardedView = withWindowGuard(View, WindowGuard.all)`

will return `View` component wrapped into `WindowGuard` with insets applied to all sides.
