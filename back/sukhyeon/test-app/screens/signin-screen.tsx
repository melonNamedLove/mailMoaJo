import { useNavigation } from "@react-navigation/native";
import { Alert, Button, Text, TextInput, View } from "react-native";
import styled from "styled-components";
import {
  GoogleSignin,
  GoogleSigninButton,
  statusCodes,
} from "@react-native-google-signin/google-signin";
import * as WebBrowser from "expo-web-browser";
import * as Google from "expo-auth-session/providers/google";
import React from "react";
import { useEffect } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

const Input1 = styled(TextInput)`
  height: 50px;
  background-color: skyblue;
  margin-bottom: 10px;
`;
const Input2 = styled(TextInput)`
  height: 50px;
  background-color: #afd3eb;
`;
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

// 로그인 버튼 누르면 웹 브라우저가 열리고, 구글 로그인 페이지로 이동함.
export default () => {
  // 안드로이드, 웹 클라이언트 아이디를 사용하여 인증 요청 보냄.
  // Google 인증 요청을 위한 훅 초기화
  // promptAsync: 인증 요청 보냄.
  const [request, response, promptAsync] = Google.useAuthRequest({
    webClientId:
      "451769492147-ft3ci12mv45gnv5efge6knv6e85uhpa7.apps.googleusercontent.com",
    androidClientId:
      "451769492147-5nrqs8jmnf16eo8nf081sa5gltfl2ii5.apps.googleusercontent.com",
  });

  const [userInfo, setUserInfo] = React.useState(null);

  // Google 로그인 처리하는 함수
  const handleSignInWithGoogle = async () => {
    const user = await AsyncStorage.getItem("@user");
    if (!user) {
      if (response?.type === "success") {
        // 인증 요청에 대한 응답이 성공이면, 토큰을 이용하여 유저 정보를 가져옴.
        await getUserInfo(response.authentication?.accessToken);
      }
    } else {
      // 유저 정보가 이미 있으면, 유저 정보를 가져옴.
      setUserInfo(JSON.parse(user));
    }
  };

  // 토큰을 이용하여 유저 정보를 가져오는 함수
  const getUserInfo = async (token: any) => {
    if (!token) return;
    try {
      const response = await fetch(
        "https://www.googleapis.com/oauth2/v3/userinfo",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      const userInfoResponse = await response.json();
      // 유저 정보를 AsyncStorage에 저장, 상태업뎃
      await AsyncStorage.setItem("@user", JSON.stringify(userInfoResponse));
      setUserInfo(userInfoResponse);
    } catch (e) {
      console.log(e);
    }
  };
  const handleLogout = async () => {
    await AsyncStorage.removeItem("@user");
    setUserInfo(null);
  };

  // Google 인증 응답이 바뀔때마다 실행
  useEffect(() => {
    handleSignInWithGoogle();
  }, [response]);

  return (
    <View>
      <Text>{JSON.stringify(userInfo, null, 2)}</Text>
      <Button
        disabled={!request}
        title="Login"
        onPress={() => {
          promptAsync();
        }}
      />
      <Button title="logout" onPress={() => handleLogout()} />
    </View>
  );
};
