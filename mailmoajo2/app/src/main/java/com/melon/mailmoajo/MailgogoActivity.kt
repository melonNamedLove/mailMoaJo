package com.melon.mailmoajo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.melon.mailmoajo.databinding.ActivityMailgogoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MailgogoActivity : AppCompatActivity() {
    val binding = ActivityMailgogoBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getAccessToken("")

    }
}

private val getAccessTokenBaseUrl = "https://gmail.googleapis.com"
fun getAccessToken(authCode:String) {
    LoginService.loginRetrofit(getAccessTokenBaseUrl).getAccessToken(
        request = LoginGoogleRequestModel(
            grant_type = "authorization_code",
            client_id = "281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com",
            client_secret = "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",
            code = authCode.orEmpty()
        )
    ).enqueue(object : Callback<LoginGoogleResponseModel> {
        override fun onResponse(call: Call<LoginGoogleResponseModel>, response: Response<LoginGoogleResponseModel>) {
            if(response.isSuccessful) {
                val accessToken = response.body()?.access_token.orEmpty()
                Log.d("meow", "accessToken: $accessToken")
            }
        }
        override fun onFailure(call: Call<LoginGoogleResponseModel>, t: Throwable) {
            Log.e("meow", "getOnFailure: ",t.fillInStackTrace() )
        }
    })
}