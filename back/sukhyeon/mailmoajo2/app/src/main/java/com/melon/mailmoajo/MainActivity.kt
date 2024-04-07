package com.melon.mailmoajo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.melon.mailmoajo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var resultLauncher : ActivityResultLauncher<Intent>

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.let {
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
        }?: Toast.makeText(this, "Not Yet", Toast.LENGTH_SHORT).show()
    }

    val serverCI = "281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setResultSignUp()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(serverCI)
            .build()

        with(binding){
            btnSignIn.setOnClickListener{
                signIn()
            }

            btnSignOut.setOnClickListener {
                signOut()
            }
        }
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
        setContentView(binding.root)

    }
    private fun setResultSignUp(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        //정상적으로 결과가 받아질 때 조건문 실행
            if(result.resultCode == Activity.RESULT_OK){
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }

        }
    }

    private fun handleSignInResult(completedTask:Task<GoogleSignInAccount>){
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()
            val tok = account?.idToken.toString()

            Log.d("kkktoken", tok)
        }catch (e:ApiException){
            //the api Exception status code indicates the detailed failure reason
            //please refer to GoogleSignInStatusCodes class reference for more information
            Log.w("failed","signInResult:failed code"+ e.statusCode)
        }
    }

    private fun signIn(){
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        resultLauncher.launch(signInIntent)

    }

    private fun signOut(){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this){

            }
    }
    private fun revokeAccess(){
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this){

            }
    }


}