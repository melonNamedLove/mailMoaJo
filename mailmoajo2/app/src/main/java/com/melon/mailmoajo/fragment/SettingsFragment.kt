package com.melon.mailmoajo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.gson.Gson
import com.melon.mailmoajo.AccessToken
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.GoogleSignInActivity
import com.melon.mailmoajo.GoogleSignInActivity.Companion.prefs
import com.melon.mailmoajo.GoogleSignInActivity.Companion.tokenprefs
import com.melon.mailmoajo.MSGraphRequestWrapper
import com.melon.mailmoajo.MSGraphRequestWrapper.callGraphAPIUsingVolley
import com.melon.mailmoajo.dataclass.PostResult
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import com.melon.mailmoajo.dataclass.mailData
import com.melon.mailmoajo.dataclass.mailId
import com.melon.mailmoajo.dataclass.gotMailList
import com.melon.mailmoajo.dataclass.payload_json
import com.melon.mailmoajo.supabase
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import entities.mails
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.Arrays
import java.util.Base64


var mailmail = mutableListOf<mails>()
/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    /* Azure AD Variables */
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private var mAccount: IAccount? = null
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

//        binding.retro2Btn.setOnClickListener{
//
//
//
//        }
//
//
//        //access token load 부
//        binding.retroBtn.setOnClickListener{
//
//
////            AccessToken.create().postAccessToken(
////                accesscode.toString(),
////                "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
////                "https://www.googleapis.com/auth/gmail.readonly",
////                "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",
////                "http://localhost:5500/test.html",
////                "authorization_code"
////            ).enqueue(object : Callback<PostResult>{
////                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
////                    if(response.isSuccessful.not()){
////                        Log.d("meow", "nope")
////                        return
////                    }
////
////                        Log.d("meow", response.body()?.access_token.toString())
////
////                }
////
////                override fun onFailure(call: Call<PostResult>, t: Throwable) {
////                    Log.d("meow", "fail")
////                }
////            }
////
////            )
//        }

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


        com.microsoft.identity.client.PublicClientApplication.createSingleAccountPublicClientApplication(
            requireContext(),
            com.melon.mailmoajo.R.raw.msalconfiguration,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    mSingleAccountApp = application
                }

                override fun onError(exception: MsalException?) {
                    //Log Exception Here
                    Log.d(TAG, exception.toString())
                }
            })

        val scopes:Array<String> = arrayOf(
                "Mail.Read",
                "email",
                "User.Read"
                )



        binding.MSloadBtn.setOnClickListener {
            if(mSingleAccountApp ==null){}
            val signInParameters: SignInParameters = SignInParameters.builder()
                .withActivity(requireActivity())
                .withLoginHint(null)
                .withScopes(Arrays.asList(*scopes))
                .withCallback(authInteractiveCallback)
                .build()
            mSingleAccountApp!!.signIn(signInParameters)

        }
        binding.MSsignoutBtn.setOnClickListener( {
            if (mSingleAccountApp == null) {
            }

            /*
                     * Removes the signed-in account and cached tokens from this app (or device, if the device is in shared mode).
                     */mSingleAccountApp!!.signOut(object :
            ISingleAccountPublicClientApplication.SignOutCallback {
            override fun onSignOut() {
                mAccount = null
                showToastOnSignOut()
            }

            override fun onError(exception: MsalException) {
                Log.d(
                    TAG,
                    "Authentication failed: $exception"
                )
            }
        })
        })
        return binding.root
    }
    private val authInteractiveCallback: AuthenticationCallback
        private get() = object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated")
                Log.d(
                    TAG,
                    authenticationResult.accessToken.toString()
                )
                Log.d(TAG, "ID Token: " + authenticationResult.account.claims!!["id_token"])

                /* Update account */mAccount = authenticationResult.account

                /* call graph */callGraphAPI(authenticationResult, defaultGraphResourceUrl)
                /* call graph */callGraphAPI(authenticationResult, defaultGraphResourceUrl+"/messages?\$select=sender,subject,receivedDateTime")
            }

            override fun onError(exception: MsalException) {
                /* Failed to acquireToken */
                Log.d(
                    TAG,
                    "Authentication failed: $exception"
                )
//                displayError(exception)
                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            override fun onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.")
            }
        }

    val defaultGraphResourceUrl = MSGraphRequestWrapper.MS_GRAPH_ROOT_ENDPOINT + "v1.0/me"
    private fun callGraphAPI(authenticationResult: IAuthenticationResult, url: String) {
        callGraphAPIUsingVolley(
            requireContext(),
            url,
            authenticationResult.accessToken,
            com.android.volley.Response.Listener<JSONObject> { response -> /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: $response")

//                displayGraphResult(response)
            },
            com.android.volley.Response.ErrorListener { error ->
                Log.d(TAG, "Error: $error")
//                displayError(error)
            })
    }
    private fun showToastOnSignOut() {
        val signOutText = "Signed Out."
        Toast.makeText(context, signOutText, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
    }
}