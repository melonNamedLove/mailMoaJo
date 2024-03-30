import { StatusBar } from 'expo-status-bar';
import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, View, Button } from 'react-native';
import axios from 'axios';
import * as WebBrowser from 'expo-web-browser';
import * as Google from 'expo-auth-session/providers/google';
import styled from "styled-components";

// const Input1 = styled(TextInput)`
//   height: 50px;
//   background-color: skyblue;
//   margin-bottom: 10px;
// `;
// const Input2 = styled(TextInput)`
//   height: 50px;
//   background-color: #afd3eb;
// `;
//아이디 비밀번호 입력 텍스트 박스 총 2개

WebBrowser.maybeCompleteAuthSession();

// signIn = async () => {
//     try {
//       await GoogleSignin.hasPlayServices();
//       const userInfo = await GoogleSignin.signIn();
//       setState({ userInfo });
//     } catch (error) {
//       if (error.code === statusCodes.SIGN_IN_CANCELLED) {
//         // user cancelled the login flow
//       } else if (error.code === statusCodes.IN_PROGRESS) {
//         // operation (e.g. sign in) is in progress already
//       } else if (error.code === statusCodes.PLAY_SERVICES_NOT_AVAILABLE) {
//         // play services not available or outdated
//       } else {
//         // some other error happened
//       }
//     }
//   };

// // 로그인 버튼 누르면 웹 브라우저가 열리고, 구글 로그인 페이지로 이동함.
// export default () => {
//   // 안드로이드, 웹 클라이언트 아이디를 사용하여 인증 요청 보냄.
//   // Google 인증 요청을 위한 훅 초기화
//   // promptAsync: 인증 요청 보냄.
//   const [request, response, promptAsync] = Google.useAuthRequest({
//     webClientId:
//       "451769492147-ft3ci12mv45gnv5efge6knv6e85uhpa7.apps.googleusercontent.com",
//     androidClientId:
//       "451769492147-5nrqs8jmnf16eo8nf081sa5gltfl2ii5.apps.googleusercontent.com",
//   });

//   const [userInfo, setUserInfo] = React.useState(null);

//   // Google 로그인 처리하는 함수
//   const handleSignInWithGoogle = async () => {
//     const user = await AsyncStorage.getItem("@user");
//     if (!user) {
//       if (response?.type === "success") {
//         // 인증 요청에 대한 응답이 성공이면, 토큰을 이용하여 유저 정보를 가져옴.
//         await getUserInfo(response.authentication?.accessToken);
//       }
//     } else {
//       // 유저 정보가 이미 있으면, 유저 정보를 가져옴.
//       setUserInfo(JSON.parse(user));
//     }
//   };

//   // 토큰을 이용하여 유저 정보를 가져오는 함수
//   const getUserInfo = async (token: any) => {
//     if (!token) return;
//     try {
//       const response = await fetch(
//         "https://www.googleapis.com/oauth2/v3/userinfo",
//         {
//           headers: { Authorization: `Bearer ${token}` },
//         }
//       );
//       const userInfoResponse = await response.json();
//       // 유저 정보를 AsyncStorage에 저장, 상태업뎃
//       await AsyncStorage.setItem("@user", JSON.stringify(userInfoResponse));
//       setUserInfo(userInfoResponse);
//     } catch (e) {
//       console.log(e);
//     }
//   };
//   const handleLogout = async () => {
//     await AsyncStorage.removeItem("@user");
//     setUserInfo(null);
//   };

//   // Google 인증 응답이 바뀔때마다 실행
//   useEffect(() => {
//     handleSignInWithGoogle();
//   }, [response]);



export default () => {

  const [gUser, setGUser] = useState(null);
  const [reqError, setReqError] = useState('');

  const [request, response, promptAsync] = Google.useAuthRequest({
      expoClientId: '451769492147-e8e8b7t8q41st21e2r3cr0k8srostisq.apps.googleusercontent.com',
      androidClientId: '451769492147-5nrqs8jmnf16eo8nf081sa5gltfl2ii5.apps.googleusercontent.com',
      // iosClientId: 'GOOGLE_GUID.apps.googleusercontent.com',
      webClientId: '451769492147-5nrqs8jmnf16eo8nf081sa5gltfl2ii5.apps.googleusercontent.com',
  });

  useEffect(() => {
      if (response?.type === 'success') {
          const { authentication } = response;

          getGoogleUser(authentication?.accessToken)
      }
  }, [response]);

  const getGoogleUser = async (accessToken:any) => {
      try{
          let gUserReq = await axios.get('https://www.googleapis.com/oauth2/v2/userinfo',
              {
                  headers: {
                      Authorization: `Bearer ${accessToken}`
                  }
              }
          );
          
          console.log(gUserReq.data);
          setGUser(gUserReq.data);
      }
      catch(error:unknown){
          console.log('GoogleUserReq error: ', error);
          //       console.log(e);
          // setReqError(error.message);
      }
}

  return (
 <View>
            {/* {
                reqError !== '' &&
                <View>
                    <Text>There was an error</Text>
                    <Text>{JSON.stringify(reqError, 'reqEr', 4)}</Text>
                </View>
            } */}

            <Text>Signed user</Text>

            {
                gUser === null && 
                <Text>No user</Text>
            }

            {
                gUser !== null && 
                <Text>{JSON.stringify(gUser, null, 4)}</Text>
            }

            <Button
                disabled={!request}
                title="Sign in"
                onPress={() => promptAsync()}               
            />

            <StatusBar style="auto" />
        </View>
    );
};
