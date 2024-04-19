package com.wonddak.googletest

import android.content.Context
import android.content.IntentSender
import android.credentials.CredentialManager
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class GoogleLoginHelper(
    context: Context
) {

    companion object {
        const val TAG = "GoogleLogin"
    }

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleIdOption: GetGoogleIdOption

    init {

        val credentialManager = CredentialManager.create(context)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("INPUT_WEB_CLIENT_ID")
                    // Only show accounts previously used to sign in.(true)
                    .setFilterByAuthorizedAccounts(false)
                    .build()


            )

            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("INPUT_WEB_CLIENT_ID")
            .build()
    }

    suspend fun requestGoogleLogin(
        activityContext : Context,
        failAction: (msg: String) -> Unit,
        successAction: (result: AuthResult) -> Unit
    ) {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        runCatching {
            credentialManager.getCredential(
                request = request,
                context = activityContext,
            )
        }.onSuccess {
            //성공시 액션
            val credential = it.credential
            when(credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            // Use googleIdTokenCredential and extract id to validate and
                            // authenticate on your server.
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                            registerToFirebase(
                                googleIdTokenCredential.idToken,
                                failAction,
                                successAction
                            )
                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e(TAG, "Received an invalid google id token response", e)
                        }
                    }
                }
            }
        }.onFailure {
            //실패시 액션
            failAction(it.localizedMessage ?: "unknown error")
        }
    }

    fun registerToFirebase(
        result: ActivityResult,
        failAction: (msg: String) -> Unit,
        successAction: (result: AuthResult) -> Unit
    ) {
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val token = credential.googleIdToken
        try {
            if (token == null) {
                // error
            } else {
                // firebase auth register
                val firebaseCredential = GoogleAuthProvider.getCredential(token, null)
                val auth = Firebase.auth
                auth.signInWithCredential(firebaseCredential)
                    .addOnSuccessListener {
                        successAction(it)
                    }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    failAction("One-tap dialog was closed.")
                }

                CommonStatusCodes.NETWORK_ERROR -> {
                    failAction("One-tap encountered a network error.")
                }

                else -> {
                    failAction("Couldn't get credential from result. (${e.localizedMessage})")
                }
            }
        }
    }
}