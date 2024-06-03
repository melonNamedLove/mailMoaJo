package com.melon.mailmoajo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.melon.mailmoajo.HomeActivity.Companion.mSingleAccountApp
import com.melon.mailmoajo.dataclass.RefAccessTokenResult
import com.melon.mailmoajo.fragment.SettingsFragment
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    fun refreshAccessToken(callback: (Boolean) -> Unit) {
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

    // 기존 refreshAccessToken 함수에 suspend 함수로 변환
    suspend fun refreshAccessTokenSuspend(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val refreshToken = getRefreshToken()
            if (refreshToken == null) {
                continuation.resume(false)
                return@suspendCancellableCoroutine
            }

            val retrofit = Retrofit.Builder()
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
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<RefAccessTokenResult>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    val scopes = arrayOf(
        "Mail.Read",
        "email",
        "User.Read"
    )
    fun refreshToken() {

        com.microsoft.identity.client.PublicClientApplication.createSingleAccountPublicClientApplication(
            context,
            com.melon.mailmoajo.R.raw.msalconfiguration,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    mSingleAccountApp = application


                }

                override fun onError(exception: MsalException?) {
                    Log.d("yeah", exception.toString())
                }
            })

        val parameters = AcquireTokenSilentParameters.Builder()
            .withScopes(scopes.toList())
            .forAccount(mSingleAccountApp?.getCurrentAccount()?.currentAccount)
            .fromAuthority("https://login.microsoftonline.com/common")
            .withCallback(object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult) {
                    val accessToken = authenticationResult.accessToken
                    // 갱신된 access token을 사용하여 필요한 작업을 수행하십시오.
                    Log.d("yeah",accessToken)
                }

                override fun onError(exception: MsalException) {
                    // 예외 처리
                }

                override fun onCancel() {
                    // 취소 처리
                }
            })
            .build()

        mSingleAccountApp?.acquireTokenSilentAsync(parameters)

    }

}