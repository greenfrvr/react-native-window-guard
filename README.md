# react-native-window-guard

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
Put your layout inside `WindowGuard` component and define which insets you want to be applied. For this use `applyInsets` prop. It takes array with sides which should be affected with insets. Available values: `top`, `bottom`, `left`, `right`. After this relevant window insets will be requested from native and applied as paddings to `WindowGuard` component. 
```javascript
//...some other imports
import WindowGuard from 'react-native-window-guard';

export default class App extends React.Component {

  componentDidMount() {
    WindowGuard.requestWindowInsets();
  }

  render() {
    return (
      <WindowGuard
        ref={r => this.container = r}
        style={{flex: 1}}
        applyInsets={['top', 'bottom', 'left', 'right']}
        >
        //content
      </WindowGuard>
    );
  }
}
```
Notice that you can still add paddings to `WindowGuard` and they will be added to applied window insets. Currently `paddingHorizontal` and `paddingVertical` are not supported, so padding should be defined explicitly.

Also for better perfomance you may want to call `WindowGuard.requestWindowInsets()`. For now it's needed due to async calls to native code, so React Native needs some time to deliver inset values.
