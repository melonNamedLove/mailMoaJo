package com.melon.mailmoajo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.melon.mailmoajo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
//    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth
//    auth = Firebase.auth
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//
//    paoverride fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        var currentUser = auth.getCurrentUser()
//        updateUI(currentUser);
//    }
//
//
//    signInRequest = BeginSignInRequest.builder()
//    .setGoogleIdTokenRequestOptions(
//    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//    .setSupported(true)
//    // Your server's client ID, not your Android client ID.
//    .setServerClientId(getString(R.string.your_web_client_id))
//    // Only show accounts previously used to sign in.
//    .setFilterByAuthorizedAccounts(true)
//    .build())
//    .build()
//
//
//    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
//    val idToken = googleCredential.googleIdToken
//    when {
//        idToken != null -> {
//            // Got an ID token from Google. Use it to authenticate
//            // with Firebase.
//            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//            auth.signInWithCredential(firebaseCredential)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "signInWithCredential:success")
//                        val user = auth.currentUser
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "signInWithCredential:failure", task.exception)
//                        updateUI(null)
//                    }
//                }
//        }
//        else -> {
//            // Shouldn't happen.
//            Log.d(TAG, "No ID token!")
//        }
//    }
}