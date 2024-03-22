import { Alert, Button, Text, TextInput, View } from "react-native"
import styled from "styled-components"

const Input1 = styled(TextInput)`
    height: 50px;
    background-color: skyblue;
    margin-bottom: 10px;
`
const Input2 = styled(TextInput)`
    height: 50px;
    background-color: #afd3eb;
`
//아이디 비밀번호 입력 텍스트 박스 총 2개
export default ()=>{
    
    return<View>
            <Text> test test </Text>
            {/* <Input1 id ="id"></Input1>
            <Input2 id="pw"></Input2> */}
            <Button title="로그인" onPress = {() => console.log("a")}>  </Button>
        </View>

}