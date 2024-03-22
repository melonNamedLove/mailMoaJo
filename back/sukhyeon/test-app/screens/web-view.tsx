
import React from 'react';
import {SafeAreaView, StyleSheet, Dimensions} from 'react-native';
import WebView from 'react-native-webview';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

const Webview = () => {

  return (
    <SafeAreaView style={styles.container}>
      <WebView
        // ref={ref}
        style={styles.webview}
        source={{uri: 'https://auth.worksmobile.com/oauth2/v2.0/authorize?client_id=deloPYgxenkwauBKAZ39&redirect_uri=https://43.200.60.175:80&scope=mail,mail.read&response_type=code&state=test'}}
        // onNavigationStateChange={e => setNavState(e)}
        shouldOverrideUrlLoadingSynchronousMethodEnabled={false}
      />
    </SafeAreaView>
  );
};

export default Webview;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  webview: {
    flex: 1,
    width: windowWidth,
    height: windowHeight,
  },
});