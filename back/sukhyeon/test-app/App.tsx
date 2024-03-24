import { createStackNavigator } from '@react-navigation/stack';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import 'react-native-gesture-handler';
import signinScreen from './screens/signin-screen';
import Webviewscreen from './screens/web-view';

const Stack = createStackNavigator();

export default function App() {
  return <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="LOGIN" component={signinScreen} ></Stack.Screen>
        <Stack.Screen name="WEB" component={Webviewscreen} ></Stack.Screen>
      </Stack.Navigator>
    </NavigationContainer>

}