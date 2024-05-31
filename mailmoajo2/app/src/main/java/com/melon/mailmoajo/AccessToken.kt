package com.melon.mailmoajo

import com.melon.mailmoajo.dataclass.PostResult
import com.melon.mailmoajo.dataclass.gotMailData
import com.melon.mailmoajo.dataclass.gotMailList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccessToken {

//    @FormUrlEncoded
//     @Headers("Content-Type: application/json")
    @POST("/oauth2/v4/token/")
    fun postAccessToken(
//        @FieldMap param: HashMap<String, String>
//        @QueryMap values: Map<String,String>

        @Query("code") code:String,
        @Query("client_id") client_id:String,
        @Query("scope") scope:String,
        @Query("client_secret") client_secret:String,
        @Query("redirect_uri") redirect_uri:String,
        @Query("grant_type") grant_type:String,

        ): Call<PostResult>


    @GET("/gmail/v1/users/{user_id}/messages")
    fun getMailList(
        @Path("user_id") user_id:String,
        @Header("Authorization") Authorization:String,
        @Query("pageToken") pageToken:String?,
        @Query("maxResults") maxResults:Int = 50,
        @Query("includeSpamTrash") includeSpamTrash:Boolean = false,

        ): Call<gotMailList>

//    @GET("/gmail/v1/users/{user_id}/messages")
//    fun getMailList1(
//        @Path("user_id") user_id:String,
//        @Header("Authorization") Authorization:String,
//        @Query("pageToken") pageToken:String?,
//        @Query("maxResults") maxResults:Int = 50,
//        @Query("includeSpamTrash") includeSpamTrash:Boolean = false,
//
//        ): Call<gotMailList>

    @GET("/gmail/v1/users/{user_id}/messages/{id}")
    fun getMailData(
        @Path("user_id") user_id:String,
        @Path("id") id:String,
        @Header("Authorization") Authorization:String,
        @Query("format") format:String = "metadata",
        @Query("metadataHeaders") mheader1:String = "Subject",
        @Query("metadataHeaders") mheader2:String = "Received",
        @Query("metadataHeaders") mheader3:String = "From",
    ): Call<gotMailData>

}