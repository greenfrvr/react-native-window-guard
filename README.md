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
```javascript
import RNWindowGuard from 'react-native-window-guard';

// TODO: What to do with the module?
RNWindowGuard;
```
  