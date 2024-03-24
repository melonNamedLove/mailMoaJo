package com.melon.mailmoajo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.melon.mailmoajo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        addListener()

    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          //  .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode("451769492147-5nrqs8jmnf16eo8nf081sa5gltfl2ii5.apps.googleusercontent.com") // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
    }

    private fun addListener() {
        binding.clGoogleLogin.setOnClickListener { // 버튼 역할을 하는 clGoogleLogin에 클릭리스너를 달아준다.

            requestGoogleLogin()
        }
    }

    private fun requestGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }


    private fun moveSignUpActivity() {
        requireActivity().run {
            startActivity(Intent(requireContext(), SignUpActivity::class.java))
            finish()
        }
    }
}