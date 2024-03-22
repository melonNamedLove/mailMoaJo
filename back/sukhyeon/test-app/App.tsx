import { createStackNavigator } from '@react-navigation/stack';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import signinScreen from './screens/signin-screen';
import { NavigationContainer } from '@react-navigation/native';
import 'react-native-gesture-handler';
import Webview from './screens/web-view';

const Stack = createStackNavigator();

export default function App() {
  return <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="WEB" component={Webview} ></Stack.Screen>
        <Stack.Screen name="LOGIN" component={signinScreen} ></Stack.Screen>
      </Stack.Navigator>
    </NavigationContainer>

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
