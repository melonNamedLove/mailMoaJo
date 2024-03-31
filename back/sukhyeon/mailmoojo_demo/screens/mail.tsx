//  ./components의 Auth.tsx에서 받아온 token을 받아 id를 추출하고 gmail api에서 메일 목록을 불러옴

import React, { createContext, useContext, useState } from 'react';
// import axios from 'axios';
// import { useRouter } from 'next/router';
// import { useCookies } from 'react-cookie';
import { useEffect } from 'react';

import { Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';



// const UserContext = createContext(null);

// export default() => {
//     const { userInfo } = useContext(UserContext);
    
//     console.log(UserInformation);
//     return(
//         <View>
//         </View>
//     ) 
// }



export default (route:any) => {


  return (
    <SafeAreaView>

        <View>
        // 데이터 출력
            <Text>{route.params.userInfo.email}</Text>
            <Text>{route.params.userInfo.id}</Text>
        </View>

    </SafeAreaView>
  );
};