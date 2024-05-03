package com.melon.mailmoajo

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint.Style
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.common.util.SharedPreferencesUtils
//import com.google.android.gms.auth.api.Auth
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.melon.mailmoajo.GoogleSignInActivity.Companion.prefs
import com.melon.mailmoajo.ui.theme.Mailmoajo2Theme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.security.Provider
import java.util.Scanner
import java.util.UUID

val supabase = createSupabaseClient(
    supabaseUrl = "https://jqsyjqwrbzyxfrmihszv.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impxc3lqcXdyYnp5eGZybWloc3p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTM1MzA4NTMsImV4cCI6MjAyOTEwNjg1M30.sjPdLoTNWKQ9Iw0NGfy1MxBCTxmEx1QIhALW99AFh5I"
){
    install(io.github.jan.supabase.gotrue.Auth)
    install(Postgrest)
}

class GoogleSignInActivity : ComponentActivity() {
    companion object {
        lateinit var prefs: SharedPreferences
        lateinit var tokenprefs: SharedPreferences
        lateinit var contactprefs: SharedPreferences
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Log.d("meow",supabase.auth.currentIdentitiesOrNull().toString())

            prefs = this.getSharedPreferences("loginData", MODE_PRIVATE)
            Log.i("meow", prefs.getString("loginData","").toString()+"실행")
            if(supabase.auth.currentIdentitiesOrNull() != null || prefs.getString("loginData","")!=""){
                var i: Intent = Intent(this, HomeActivity::class.java)
                startActivity(i)

            }
//            val cacheFile = File(this.cacheDir, "loginTF")
////            var prefLoginTF = this.getSharedPreferences("loginTF", MODE_PRIVATE)
////            val loginTF = prefLoginTF.getBoolean("login", false)
//
//            //-----------------------------------로그인부 에러 ㅂ있음
//            val inputStream = FileInputStream(cacheFile)
//            val s = Scanner(inputStream)
//            var text = ""
//            while (s.hasNext()) {
//                text += s.nextLine()
//            }
//            Log.d("meow",text)
//            inputStream.close()
//            if(text.equals("true")){                                           //***로그인부
//                Log.d("meow",text.toString())
//                var i: Intent = Intent(this, HomeActivity::class.java)
//                startActivity(i)
//            }


            Mailmoajo2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        InsertButton()
                        GoogleSignInButton()
                    }
                }
            }
        }
    }
}

@Composable
fun InsertButton(){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Button(onClick = {
        coroutineScope.launch {
            try{

                supabase.from("posts").insert(mapOf("content" to "hello from android"))
                Toast.makeText(context, "New row inserted", Toast.LENGTH_SHORT).show()

            }catch (e: RestException){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()

        }
        }

//        //jetpack compose 에서  activity 밖 함수에서 activity 이동
//        Intent(context,aaActivity::class.java).also {
//        startActivity(context, it, null)
//    }
//

    }) {
        Text("야옹야옹멍멍")
        
    }
}


@Composable
fun GoogleSignInButton(){
    val context = LocalContext.current
    val coroutineScope =rememberCoroutineScope()

    val onClick:() ->Unit = {
        val credentialManager = CredentialManager.create(context)
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold(""){str, it ->str +"%02x".format(it)}

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com")//웹 클라이언트 아이디
            //05:64:38:C5:ED:0E:DF:2C:98:30:5D:2B:EC:23:65:E8:AD:D3:14:16
            //1D:53:CD:1F:9C:BA:14:C4:A7:1B:8C:92:AF:E0:9E:A3:01:F2:49:7C
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch{
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken
                Log.i("meow", googleIdTokenCredential.id)                //google email
                googleIdTokenCredential.displayName
                googleIdTokenCredential
                supabase.auth.signInWith(IDToken){              //supabase  auth
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }
//                Log.i("meow", googleIdToken)
//                var tempLoginFile=File.createTempFile("loginTF", null, context.cacheDir)
//                val cacheFile = File(context.cacheDir, "loginTF")
//                val outputStream = FileOutputStream(cacheFile)
//                outputStream.write("true".toByteArray())
//                outputStream.close()

//                tempLoginFile.writeText(true.toString())
//                var prefLoginTF = context.getSharedPreferences("loginTF", MODE_PRIVATE)
//                prefLoginTF.edit().putBoolean("login",true)
                Log.d("meow", supabase.auth.currentIdentitiesOrNull().toString())
                prefs.edit().putString("loginData",supabase.auth.currentIdentitiesOrNull().toString()).apply()
                Log.i("meow", prefs.getString("loginData","").toString())
                Toast.makeText(context,"You are signed in!", Toast.LENGTH_SHORT).show()
            }catch (e: GetCredentialException){
                Log.i("meow", e.toString())
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }catch (e: GoogleIdTokenParsingException){
                Log.i("meow", e.toString())
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }
        }
        var i: Intent = Intent(context, HomeActivity::class.java)
        startActivity(context, i, null)
    }
    Button(onClick = onClick) {
        Text("Sign in with Google")
    }
}
//6:45