package com.melon.mailmoajo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.melon.mailmoajo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)

            // 이름, 이메일 등이 필요하다면 아래와 같이 account를 통해 각 메소드를 불러올 수 있다.
            val userName = account.givenName
            val serverAuth = account.serverAuthCode

            moveSignUpActivity()

        } catch (e: ApiException) {
            Log.e("dd", e.stackTraceToString())
        }
    }
//    override fun onStart() {
//        super.onStart()
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        account?.let {
//            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
//        }?:Toast.makeText(this, "NotYet", Toast.LENGTH_SHORT).show()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        addListener()

    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          //  .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode("451769492147-5nrqs8jmnf16eo8nf081sa5gltfl2ii5.apps.googleusercontent.com") // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        return GoogleSignIn.getClient(SignUpActivity(), googleSignInOption)
    }

    private fun addListener() {
        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.clgooglelogin.setOnClickListener { // 버튼 역할을 하는 clGoogleLogin에 클릭리스너를 달아준다.

            requestGoogleLogin()
        }
    }

    private fun requestGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }


    private fun moveSignUpActivity() {
        SignUpActivity().run {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}