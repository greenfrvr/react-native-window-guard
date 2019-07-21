import React from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  StatusBar,
  TouchableOpacity,
  NativeModules
} from 'react-native';
import WindowGuard, {withWindowGuard} from 'react-native-window-guard';

const {UIVisibility} = NativeModules;
const GuardedView = withWindowGuard(View, WindowGuard.all);

const mainColor = '#A5DA34';
const title = Platform.select({ios: 'IOS', android: 'ANDROID'});
const subtitle = Platform.select({ios: 'Safe Area', android: 'Window Insets'});
const toggleStatusBar = 'Toggle status bar';
const toggleNavigationBar = 'Toggle navigation bar';

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
    if (Platform.OS === 'ios') {
      StatusBar.setHidden(this.statusBarHidden, true);
    } else {
      UIVisibility.changeStatusBarVisibility(this.statusBarHidden);
    }
    this.container && this.container.adjustInsets()
  };

  //Android only
  toggleNavigationBar = () => {
    this.navigationBarHidden = !this.navigationBarHidden;
    UIVisibility.changeNavigationBarVisibility(this.navigationBarHidden);
    this.container && this.container.adjustInsets()
  };

  navigationBarButton = () => {
    return (Platform.OS === 'android' &&
      <View style={styles.navigationButton}>
        <TouchableOpacity
          style={styles.button}
          onPress={this.toggleNavigationBar}>
          <Text style={styles.buttonTitle}>{toggleNavigationBar}</Text>
        </TouchableOpacity>
      </View>);
  };

  render() {
    return (
      <GuardedView
        ref={r => this.container = r}
        style={styles.container}>
        <View style={styles.topContainer}>
          <Text style={styles.title}>{title}</Text>
          <Text style={styles.subtitle}>{subtitle}</Text>
        </View>
        <View style={styles.bottomContainer}>
          <TouchableOpacity
            style={styles.button}
            onPress={this.toggleStatusBar}>
            <Text style={styles.buttonTitle}>{toggleStatusBar}</Text>
          </TouchableOpacity>
          {this.navigationBarButton()}
        </View>
      </GuardedView>
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
  topContainer: {
    flex: 5,
    alignItems: 'center',
    justifyContent: 'center'
  },
  bottomContainer: {
    flex: 4,
    alignItems: 'center',
    justifyContent: 'center'
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
  button: {
    paddingHorizontal: 16,
    paddingVertical: 8
  },
  navigationButton: {
    marginTop: 8
  },
  buttonTitle: {
    fontSize: 16,
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
