package com.melon.mailmoajo

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.melon.mailmoajo.dataclass.RefAccessTokenResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenManager(private val context: Context) {
    fun getEncryptedSharedPreferences(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "encrypted_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveRefreshToken(refreshToken: String) {
        val sharedPreferences = getEncryptedSharedPreferences()
        val editor = sharedPreferences.edit()
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }
    fun getRefreshToken(): String? {
        val sharedPreferences = getEncryptedSharedPreferences()
        return sharedPreferences.getString("refresh_token", null)
    }

    fun refreshAccessToken() {
        val refreshToken = getRefreshToken()
        if (refreshToken == null) {
            // Refresh token이 없으면 처리
            return
        }

        val retrofit  = Retrofit.Builder()
            .baseUrl("https://oauth2.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)

        service.postTokenRefresh(
            "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
            "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",
            "refresh_token",
            refreshToken
        ).enqueue(object : retrofit2.Callback<RefAccessTokenResult> {
            override fun onResponse(call: Call<RefAccessTokenResult>, response: Response<RefAccessTokenResult>) {
                if (response.isSuccessful) {
                    val accessTokenResponse = response.body()
                    if (accessTokenResponse != null) {
                        val sharedPreferences = getEncryptedSharedPreferences()
                        with(sharedPreferences.edit()) {
                            putString("access_token", accessTokenResponse.access_token)
                            apply()
                        }
                    }
                } else {
                    // 에러 처리
                }
            }

            override fun onFailure(call: Call<RefAccessTokenResult>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }
}