package com.melon.mailmoajo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.melon.mailmoajo.databinding.ActivityMailgogoBinding
import com.melon.mailmoajo.ui.theme.Mailmoajo2Theme
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //이건 웹뷰로 구성해볼 예정
//            var re = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
//                if(result.resultCode == 1){
//                    Log.i("meow", "--------------data here ------------------")
//                }else{
//                    Log.i("meow", "error")
//                }
//            }
//            val mailapi:() ->Unit = {
//
//                var i: Intent = Intent(this, MailgogoActivity::class.java)
//
//                re.launch(i)
//            }

            val mailgo:() ->Unit = {
                var i: Intent = Intent(this, MailgogoActivity::class.java)
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
                        GoogleSignInButton()
                        Button(onClick = mailgo) {
                            Text("google 동기화")
                        }
                    }
                }
            }
        }
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
            .setServerClientId("281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com")
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

                Log.i("meow", googleIdToken)

                Toast.makeText(context,"You are signed in!", Toast.LENGTH_SHORT).show()
            }catch (e: GetCredentialException){
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }catch (e: GoogleIdTokenParsingException){
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Button(onClick = onClick) {
        Text("Sign in with Google")
    }
}
//6:45