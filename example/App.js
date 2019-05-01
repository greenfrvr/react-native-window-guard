import React from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Button,
  Dimensions,
  StatusBar
} from 'react-native';
import WindowGuard, {RNWindowGuard} from 'react-native-window-guard';

const mainColor = '#A5DA34';
const title = Platform.select({
  ios: 'IOS',
  android: 'ANDROID',
});
const subtitle = Platform.select({
  ios: 'Safe Area',
  android: 'Window Insets',
});

export default class App extends React.Component {

  constructor(props) {
    super(props);
    this.statusBarHidden = false;
    this.navigationBarHidden = true;
  }

  componentDidMount() {
    WindowGuard.requestWindowInsets();
  }

  toggleStatusBar = () => {
    this.statusBarHidden = !this.statusBarHidden;
    StatusBar.setHidden(this.statusBarHidden, true);
    // RNWindowGuard.changeStatusBarVisibility(this.statusBarHidden);
    this.container && this.container.update()
  };

  toggleNavigationBar = () => {
    this.navigationBarHidden = !this.navigationBarHidden;
    RNWindowGuard.changeNavigationBarVisibility(this.navigationBarHidden);
    this.container && this.container.update()
  };

  onLayout = (e) => {
    const {width, height} = Dimensions.get('window');
    console.log("Orientation did changed");
    console.log(width, height);
    this.container && this.container.update()
  };

  render() {
    return (
      <WindowGuard
        ref={r => this.container = r}
        style={{flex: 1}}
        applyInsets={['top', 'bottom', 'left', 'right']}
        onLayout={this.onLayout}>
        <View style={styles.container}>
          <View style={{flex: 5, alignItems: 'center', justifyContent: 'center'}}>
            <Text style={styles.title}>{title}</Text>
            <Text style={styles.subtitle}>{subtitle}</Text>
          </View>
          <View style={{flex: 4, alignItems: 'center', justifyContent: 'center'}}>
            <View style={{marginBottom: 20}}>
              <Button
                style={styles.button}
                title={'Toggle status bar'}
                color={mainColor}
                onPress={this.toggleStatusBar}/>
            </View>
            {Platform.OS === 'android' &&
            <Button
              title={'Toggle navigation bar'}
              color={mainColor}
              onPress={this.toggleNavigationBar}/>
            }
          </View>
        </View>
      </WindowGuard>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#222222',
  },
  title: {
    fontSize: Platform.select({
      ios: 84,
      android: 43,
    }),
    textAlign: 'center',
    fontFamily: 'DIN Alternate',
    color: mainColor
  },
  subtitle: {
    fontSize: 28,
    fontFamily: 'DIN Alternate',
    textAlign: 'center',
    marginBottom: 10,
    color: mainColor
  }
});
