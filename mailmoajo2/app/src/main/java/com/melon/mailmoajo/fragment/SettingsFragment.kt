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
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.GoogleSignInActivity
import com.melon.mailmoajo.GoogleSignInActivity.Companion.prefs
import com.melon.mailmoajo.HomeActivity
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
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
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

        binding.logoutBtn.setOnClickListener{
            val credentialManager = CredentialManager.create(requireContext())
            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold(""){str, it ->str +"%02x".format(it)}


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