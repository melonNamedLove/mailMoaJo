package com.melon.mailmoajo.dataclass

import com.google.gson.annotations.SerializedName


data class PostResult (
    @SerializedName("access_token") var access_token:String,

    @SerializedName("expires_in") var expires_in:Int,

    @SerializedName("refresh_token") var refresh_token:String,

    @SerializedName("scope") var scope:String,

    @SerializedName("token_type") var token_type:String,

    @SerializedName("id_token") var id_token:String

)


data class RefAccessTokenResult (
    @SerializedName("access_token") var access_token:String,

    @SerializedName("expires_in") var expires_in:Int,

    @SerializedName("scope") var scope:String,

    @SerializedName("token_type") var token_type:String,

)
//{
//    "access_token": "ya29.a0Ad52N39WdUB5YuJC3PB-zBWbLrG1MzgW1faw9LaHYP_Xx-l7kF_aygAWw_RUP12VfqWsng_l5pNQ_kPix6123YlJh5XsoqFo4e6IrY6dZozgSgclm0mwTRWiTm6Q6e4exInSsye-i3NZG25cvMosc7Nv-aEr-7q85S6eaCgYKATgSARISFQHGX2MiQqlEZHeVyr_SUiExPgnqzA0171",
//    "expires_in": 3599,
//    "scope": "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/gmail.readonly openid",
//    "token_type": "Bearer",
//    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjZjZTExYWVjZjllYjE0MDI0YTQ0YmJmZDFiY2Y4YjMyYTEyMjg3ZmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDUwNzAxNjcyOTMzLTBwOHJ1dHB2cDhndGFmcmRxb2o5YWtnMmxucDFkY2ZjLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA1MDcwMTY3MjkzMy0wcDhydXRwdnA4Z3RhZnJkcW9qOWFrZzJsbnAxZGNmYy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwNjYyMjYwMzgzMjYzNTY5NzMzMyIsImVtYWlsIjoiaHVpYmVvbTEyM0BnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Imx2eTJTZm5qVjJ3QzI0MXVENE9FR1EiLCJpYXQiOjE3MTM2OTEyOTMsImV4cCI6MTcxMzY5NDg5M30.FKamFE3W4e6rt0PdtadkcbCoF66tx1qnjWCeHDaWtttICATfuBvqWn1UYgI-ZRkWI7I2anKJikwi7QLimASsc6BJGIf9srzRn8gyS3ou2p6dyQ-ky72TBGM14dPzRx9Po7Wt0srU64UbBZsbUJms15smI3Sqr_l0BvK6R4OY1pznizXXSgf7e_TqXM-dTVK74I_1iMuT3Zoq2dQ52eBoT5twpGB_kqZ_eM1JPyQoKdBJFzn4TozNLh3k4oQdfC6FeAvYgEctV0yCxoVt0V-VfgFTZCILvkuLw958fbt7T36doIeQjBd6fC3muhX7fMw2z_yiYmIAJ8Evho4x_vH3eg"
//}




data class payload_json(
    val iss:String =  "",
    val azp:String = "",
    val aud:String ="",
    val sub:String = "",
    val email:String = "",
    val email_verified:Boolean = false,
    val at_hash:String = "",
    val iat:Int = 0,
    val exp:Int = 0

)

//{
//    "iss": "https://accounts.google.com",
//    "azp": "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
//    "aud": "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
//    "sub": "101332753603629523240",
//    "email": "huibeomjeong99@gmail.com",
//    "email_verified": true,
//    "at_hash": "GLcK_9fLCQt7WOmm5VUQLg",
//    "iat": 1713797764,
//    "exp": 1713801364
//}
data class gotGMailList(

    @SerializedName("messages") val messages:ArrayList<Any>,
    @SerializedName("nextPageToken")val nextPageToken:String?,
    @SerializedName("resultSizeEstimate")val resultSizeEstimate:Int

)

data class gotGMailData(
    val historyId: String,
    val id: String,
    val internalDate: String,
    val labelIds: List<String>,
    val payload: Payload,
    val sizeEstimate: Int,
    val snippet: String,
    val threadId: String
)

data class Payload(
    val headers: List<Header>,
    val mimeType: String
)

data class Header(
    val name: String,
    val value: String
)


data class gotOutlookMail(

    @SerializedName("@odata.context") val datacontext:String,
    @SerializedName("@odata.nextLink") val nextLink:String?,
    @SerializedName("value") val value:List<Value>
)

data class Value(
    @SerializedName("@odata.etag") val etag:String,
    @SerializedName("id") val valueid:String,
    @SerializedName("receivedDateTime") val receivedDateTime:String,
    @SerializedName("subject") val subject:String,
    @SerializedName("sender") val sender:Sender
)

data class Sender(
    val emailAddress: EmailAddress
)

data class EmailAddress(
    val address: String,
    val name: String
)