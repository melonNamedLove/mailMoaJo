package com.melon.mailmoajo

import android.R.id
import com.google.gson.annotations.SerializedName


data class PostResult (
    @SerializedName("access_token") var access_token:String?,

    @SerializedName("expires_in") var expires_in:String?,

    @SerializedName("scope") var scope:String?,

    @SerializedName("token_type") var token_type:String?,

    @SerializedName("id_token") var id_token:String?,


)


//{
//    "access_token": "ya29.a0Ad52N39WdUB5YuJC3PB-zBWbLrG1MzgW1faw9LaHYP_Xx-l7kF_aygAWw_RUP12VfqWsng_l5pNQ_kPix6123YlJh5XsoqFo4e6IrY6dZozgSgclm0mwTRWiTm6Q6e4exInSsye-i3NZG25cvMosc7Nv-aEr-7q85S6eaCgYKATgSARISFQHGX2MiQqlEZHeVyr_SUiExPgnqzA0171",
//    "expires_in": 3599,
//    "scope": "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/gmail.readonly openid",
//    "token_type": "Bearer",
//    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjZjZTExYWVjZjllYjE0MDI0YTQ0YmJmZDFiY2Y4YjMyYTEyMjg3ZmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDUwNzAxNjcyOTMzLTBwOHJ1dHB2cDhndGFmcmRxb2o5YWtnMmxucDFkY2ZjLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA1MDcwMTY3MjkzMy0wcDhydXRwdnA4Z3RhZnJkcW9qOWFrZzJsbnAxZGNmYy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwNjYyMjYwMzgzMjYzNTY5NzMzMyIsImVtYWlsIjoiaHVpYmVvbTEyM0BnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Imx2eTJTZm5qVjJ3QzI0MXVENE9FR1EiLCJpYXQiOjE3MTM2OTEyOTMsImV4cCI6MTcxMzY5NDg5M30.FKamFE3W4e6rt0PdtadkcbCoF66tx1qnjWCeHDaWtttICATfuBvqWn1UYgI-ZRkWI7I2anKJikwi7QLimASsc6BJGIf9srzRn8gyS3ou2p6dyQ-ky72TBGM14dPzRx9Po7Wt0srU64UbBZsbUJms15smI3Sqr_l0BvK6R4OY1pznizXXSgf7e_TqXM-dTVK74I_1iMuT3Zoq2dQ52eBoT5twpGB_kqZ_eM1JPyQoKdBJFzn4TozNLh3k4oQdfC6FeAvYgEctV0yCxoVt0V-VfgFTZCILvkuLw958fbt7T36doIeQjBd6fC3muhX7fMw2z_yiYmIAJ8Evho4x_vH3eg"
//}

