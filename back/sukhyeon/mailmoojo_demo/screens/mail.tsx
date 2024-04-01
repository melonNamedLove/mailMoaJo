//  ./components의 Auth.tsx에서 받아온 token을 받아 id를 추출하고 gmail api에서 메일 목록을 불러옴

import React, { createContext, useContext, useState } from 'react';
// import axios from 'axios';
// import { useRouter } from 'next/router';
// import { useCookies } from 'react-cookie';
import { useEffect } from 'react';

import { Text, View,StyleSheet, Button } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { gmail } from '@googleapis/gmail';


// const UserContext = createContext(null);

// export default() => {
//     const { userInfo } = useContext(UserContext);
    
//     console.log(UserInformation);
//     return(
//         <View>
//         </View>
//     ) 
// }




export default ({route}) => {


  const styles = StyleSheet.create({
    screen: {
      flex: 1,
      backgroundColor: '#ffffff'
    },
    text: {
      fontSize: 20
    }
  })

  return (
    <SafeAreaView>

        <View>
            <Text style={styles.text}>{route.params.name.toString()}</Text>
            <Text style={styles.text}>{route.params.tk.toString()}</Text>
            <Text style={styles.text}>{route.params.uidata}</Text>
        </View>


    </SafeAreaView>
  );
};
