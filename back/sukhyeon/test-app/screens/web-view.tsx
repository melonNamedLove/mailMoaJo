
import React, { useEffect, useRef } from 'react';
import {SafeAreaView, StyleSheet, Dimensions, Platform, BackHandler} from 'react-native';
import WebView from 'react-native-webview';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

// const webViewRef = useRef(null);
// const onAndroidBackPress = () =>{
//   if (webViewRef.current) {
//     webViewRef.current.goBack()
//     return true;
//   }
// }

// useEffect(() => {
//   if (Platform.OS === 'android') {
//     BackHandler.addEventListener('hardwareBackPress', onAndroidBackPress);
//     return () => {
//       BackHandler.removeEventListener('hardwareBackPress', onAndroidBackPress);
//     };
//   }
// }, []);

export default () => {
  return (
    <SafeAreaView style={styles.container}>
      <WebView
        // ref={ref}
        style={styles.webview}
        source={{uri: 'https://www.naver.com'}}
        setSupportMultipleWindows={false}
        //shouldOverrideUrlLoadingSynchronousMethodEnabled={false}
        // onShouldStartLoadWithRequest={(request) => {
    // Only allow navigating within this website
          // return request.url.startsWith('https://reactnative.dev');
          // }}
      />
    </SafeAreaView>
  );
};

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