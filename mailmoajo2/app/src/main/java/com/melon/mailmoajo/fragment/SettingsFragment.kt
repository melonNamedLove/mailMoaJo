package com.melon.mailmoajo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.melon.mailmoajo.AccessToken
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.GoogleSignInActivity
import com.melon.mailmoajo.GoogleSignInActivity.Companion.prefs
import com.melon.mailmoajo.GoogleSignInActivity.Companion.tokenprefs
import com.melon.mailmoajo.HomeActivity
import com.melon.mailmoajo.PostResult
import com.melon.mailmoajo.R
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import com.melon.mailmoajo.supabase
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

//    override fun onPause() {
//        super.onPause()
//    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingsBinding.inflate(layoutInflater)

        binding.gmailLoadBtn.setOnClickListener{
            var i: Intent = Intent(context, GmailLoadActivity::class.java)
            startActivity(i)
        }

        binding.retro2Btn.setOnClickListener{


        }


        //access token load 부
        binding.retroBtn.setOnClickListener{
            var accesscode = tokenprefs.getString("access_code","")
            accesscode = URLDecoder.decode(accesscode, "UTF-8")
            Log.i("meow",accesscode)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(AccessToken::class.java)

            service.postAccessToken(accesscode,
                "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
                "https://www.googleapis.com/auth/gmail.readonly",
                "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",
                "http://localhost:5500/test.html",
                "authorization_code").enqueue(object :Callback<PostResult>{
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {


                    Log.d("meow",  response.code().toString())
                    if(response.isSuccessful.not()){
                        Log.d("meow", "nope")
                        Log.d("meow", response.errorBody()?.string()!!)

                        return
                    }
                    Log.d("meow", response.body()?.access_token.toString())
                    if (response.body()?.access_token !=null){
                        tokenprefs.edit().putString("accesstoken", response.body()?.access_token).apply()
                        val tokenParts = response.body()!!.id_token.split(".")
                        Log.d("tokenmeow",tokenParts[0])
                        Log.d("tokenmeow",tokenParts[1])
                        Log.d("tokenmeow",tokenParts[2])

                        val decoder = Base64.getUrlDecoder()

                        val header = String(decoder.decode(tokenParts[0]))
                        val payload = String(decoder.decode(tokenParts[1]))
                        val signature = String(decoder.decode(tokenParts[2]))

                        Log.d("tokenmeow","header"+header)
                        Log.d("tokenmeow","payload"+payload)
                        Log.d("tokenmeow","signature"+signature)
                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("meow", "Failed API call with call: " + call +
                    " + exception: " + t)
                }


                })
//            AccessToken.create().postAccessToken(
//                accesscode.toString(),
//                "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
//                "https://www.googleapis.com/auth/gmail.readonly",
//                "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",
//                "http://localhost:5500/test.html",
//                "authorization_code"
//            ).enqueue(object : Callback<PostResult>{
//                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
//                    if(response.isSuccessful.not()){
//                        Log.d("meow", "nope")
//                        return
//                    }
//
//                        Log.d("meow", response.body()?.access_token.toString())
//
//                }
//
//                override fun onFailure(call: Call<PostResult>, t: Throwable) {
//                    Log.d("meow", "fail")
//                }
//            }
//
//            )
        }

        binding.logoutBtn.setOnClickListener{


            CoroutineScope(Dispatchers.Default).launch{
                try {
//                    val result = credentialManager.getCredential(
//                        request = request,
//                        context = requireContext()
//                    )
//                    val credential = result.credential
//
//                    val googleIdTokenCredential = GoogleIdTokenCredential
//                        .createFrom(credential.data)
//
//                    val googleIdToken = googleIdTokenCredential.idToken
//                    Log.i("meow", googleIdTokenCredential.id)                //google email
//                    googleIdTokenCredential.displayName
//                    googleIdTokenCredential
//                    supabase.auth.signInWith(IDToken){              //supabase  auth
//                        idToken = googleIdToken
//                        provider = Google
//                        nonce = rawNonce
//                    }
//                    Log.d("meow",supabase.auth.currentIdentitiesOrNull().toString())
                    prefs.edit().remove("loginData").commit()
                    Log.i("meow", prefs.getString("loginData","").toString()+"로그아웃")
                    supabase.auth.signOut()
                    var i: Intent = Intent(context, GoogleSignInActivity::class.java)
                    startActivity(i)
//                Log.i("meow", googleIdToken)
//                var tempLoginFile=File.createTempFile("loginTF", null, context.cacheDir)
//                    val cacheFile = File(requireContext().cacheDir, "loginTF")
//                    val outputStream = FileOutputStream(cacheFile)
//                    outputStream.write("false".toByteArray())
//                    outputStream.close()

//                tempLoginFile.writeText(true.toString())
//                var prefLoginTF = context.getSharedPreferences("loginTF", MODE_PRIVATE)
//                prefLoginTF.edit().putBoolean("login",true)
                    Toast.makeText(context,"You signed out", Toast.LENGTH_SHORT).show()
                }catch (e: GetCredentialException){
                    Log.i("meow", e.toString())
                    Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
                }catch (e: GoogleIdTokenParsingException){
                    Log.i("meow", e.toString())
                    Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
                }
            }

//            var p = this.context.getSharedPreferences("loginData", ComponentActivity.MODE_PRIVATE)

        }
        return binding.root
    }

}