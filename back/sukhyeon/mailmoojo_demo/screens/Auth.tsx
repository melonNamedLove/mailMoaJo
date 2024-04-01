import {
    GoogleSignin,
    GoogleSigninButton,
    statusCodes,
  } from '@react-native-google-signin/google-signin';
import { useNavigation } from '@react-navigation/native';
import { createContext, useEffect, useState } from 'react';
import { Button, View } from 'react-native';


export default () =>{

    const navigation = useNavigation();
        //move to Detail function


    GoogleSignin.configure({
    scopes: ['https://www.googleapis.com/auth/gmail.readonly'], // what API you want to access on behalf of the user, default is email and profile
    
    webClientId: '281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com', // client ID of type WEB for your server (needed to verify user ID and offline access). Required to get the `idToken` on the user object!
    
    // offlineAccess: true, // if you want to access Google API on behalf of the user FROM YOUR SERVER
    // hostedDomain: '', // specifies a hosted domain restriction
    // forceCodeForRefreshToken: true, // [Android] related to `serverAuthCode`, read the docs link below *.
    // accountName: '', // [Android] specifies an account name on the device that should be used
    // iosClientId: '<FROM DEVELOPER CONSOLE>', // [iOS] if you want to specify the client ID of type iOS (otherwise, it is taken from GoogleService-Info.plist)
    // googleServicePlistPath: '', // [iOS] if you renamed your GoogleService-Info file, new name here, e.g. GoogleService-Info-Staging
    // openIdRealm: '', // [iOS] The OpenID2 realm of the home web server. This allows Google to include the user's OpenID Identifier in the OpenID Connect ID token.
    // profileImageSize: 120, // [iOS] The desired height (and width) of the profile image. Defaults to 120px
    });

    let currentUser1 ="";
    let ui:any;
    
    return (
        <View>
    <GoogleSigninButton
        size={GoogleSigninButton.Size.Wide}
        color={GoogleSigninButton.Color.Dark}
        onPress={async () => {
            try {
                await GoogleSignin.hasPlayServices();
                // console.log(JSON.stringify(userInfo, null, 2));
                const userInfo = await GoogleSignin.signIn();
                console.log(userInfo);
                ui=userInfo;

                const currentUser = GoogleSignin.getTokens().then((res)=>{
                    console.log(res.accessToken );
                    currentUser1 = res.accessToken;
    });
                //   setState({ userInfo });
            } catch (error: any) {
                if (error.code === statusCodes.SIGN_IN_CANCELLED) {
                    // user cancelled the login flow
                } else if (error.code === statusCodes.IN_PROGRESS) {
                    // operation (e.g. sign in) is in progress already
                } else if (error.code === statusCodes.PLAY_SERVICES_NOT_AVAILABLE) {
                    // play services not available or outdated
                } else {
                    // some other error happened
                }
            }
        } } />
        <Button title='hi' onPress={()=> navigation.navigate("MAIL", {name:"meoow", tk:currentUser1, uidata:JSON.stringify(ui)})}></Button>
        </View>
    );
  }