package com.melon.mailmoajo

import android.content.Intent
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.Auth
//import com.google.android.gms.auth.api.Auth
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.melon.mailmoajo.ui.theme.Mailmoajo2Theme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

val supabase = createSupabaseClient(
    supabaseUrl = "https://jqsyjqwrbzyxfrmihszv.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impxc3lqcXdyYnp5eGZybWloc3p2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTM1MzA4NTMsImV4cCI6MjAyOTEwNjg1M30.sjPdLoTNWKQ9Iw0NGfy1MxBCTxmEx1QIhALW99AFh5I"
){
    install(io.github.jan.supabase.gotrue.Auth)
    install(Postgrest)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var prefLoginTF = this.getSharedPreferences("loginTF", MODE_PRIVATE)
//        prefLoginTF.edit().putBoolean("login",false).apply()
//        val loginTF = prefLoginTF.getBoolean("login", false)
//        if (loginTF==false){
//            var i: Intent = Intent(this, LoginActivity::class.java)
//            startActivity(i)
//        }

        setContent {

            val mailgo:() ->Unit = {
                var i: Intent = Intent(this, aaActivity::class.java)
                startActivity(i)
            }

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
                        InsertButton()
                        GoogleSignInButton()
                        Button(onClick = mailgo) {
                            Text(text = "webview")
                        }
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

                supabase.auth.signInWith(IDToken){
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }
//                Log.i("meow", googleIdToken)

                Toast.makeText(context,"You are signed in!", Toast.LENGTH_SHORT).show()
            }catch (e: GetCredentialException){
                Log.i("meow", e.toString())
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }catch (e: GoogleIdTokenParsingException){
                Log.i("meow", e.toString())
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Button(onClick = onClick) {
        Text("Sign in with Google")
    }
}
//6:45