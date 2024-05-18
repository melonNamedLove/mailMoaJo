package com.melon.mailmoajo.x

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitTest{
    companion object{
        const val BASE_URL = "https://www.googleapis.com/"
         var INSTANCE: Retrofit? = null

        fun getInstance(): Retrofit {
            if(INSTANCE == null) {  // null인 경우에만 생성
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)  // API 베이스 URL 설정
                    .addConverterFactory(GsonConverterFactory.create()) // 1)
                    .build()
            }
            return INSTANCE!!
        }
    }

}

//{
//    "access_token": "ya29.a0Ad52N38GqbCAzLKmB92hvyHim4lgLMdsfwOePNQhVWLz8TEz4cTcJH0fTQI6ioWhE3KUAY3NdJs3B6_zdEHOVkhL5lbVhWoXm12WMcuuc_Dxna2scHhxnJ7-VvhhCVmHuxCT8Kmvx6WKRvbBrYQw8A00ML5F0wv2ggH4aCgYKAZwSARMSFQHGX2MimpRubgzWTiTBhOQaiVRi7Q0171",
//    "expires_in": 3599,
//    "scope": "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/gmail.readonly openid",
//    "token_type": "Bearer",
//    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjZjZTExYWVjZjllYjE0MDI0YTQ0YmJmZDFiY2Y4YjMyYTEyMjg3ZmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDUwNzAxNjcyOTMzLTBwOHJ1dHB2cDhndGFmcmRxb2o5YWtnMmxucDFkY2ZjLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA1MDcwMTY3MjkzMy0wcDhydXRwdnA4Z3RhZnJkcW9qOWFrZzJsbnAxZGNmYy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMTMzMjc1MzYwMzYyOTUyMzI0MCIsImVtYWlsIjoiaHVpYmVvbWplb25nOTlAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJHTGNLXzlmTENRdDdXT21tNVZVUUxnIiwiaWF0IjoxNzEzNzk3NzY0LCJleHAiOjE3MTM4MDEzNjR9.pOmamEpfdrmO7ei6I5vkrJSIeCM22HZsVxg8sbepqYKYtaxg82OlThG76NG5MH50TnGGypDpwKadq6pXOlDfSr3CsBxvULCK8DJA63_0vfj0wAXholLWxdRAiE_emttKzyHRocJ5l7JEsZ0tspuGhiXT2Rtt7y-sCgY9iSDcr8VKr_htpWIo7VLzdnbBZqqH8KszeIByP2iJtz6RloR1fgig-BYAxmxDIRu0YyfeNSHZCNCOJDli45O6s5C2oOqV3SqcsxjohPj8yVE0h8if-6-aPLiiU-mhPRX_L0lM6YErfWdgHZiWZHzYj1xiTwjmuzm-d9Shsf_lW3idnajKzw"
//}