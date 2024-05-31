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
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.GoogleSignInActivity
import com.melon.mailmoajo.GoogleSignInActivity.Companion.prefs
import com.melon.mailmoajo.MSGraphRequestWrapper
import com.melon.mailmoajo.MSGraphRequestWrapper.callGraphAPIUsingVolley
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import com.melon.mailmoajo.dataclass.Value
import com.melon.mailmoajo.dataclass.gotOutlookMail
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
import java.util.Arrays


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

    fun handleMailData(data: gotOutlookMail) {
        // gotOutlookMail 데이터를 처리하는 코드
        Log.d(TAG, "Received mail data: $data")

//        data.nextLink
    }

    fun handleMailDataError(error: Throwable) {
        // 에러 처리 코드
        Log.e(TAG, "Error receiving mail data: $error")
    }
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


            CoroutineScope(Dispatchers.Default).launch{
                try {
//
                    prefs.edit().remove("loginData").commit()
                    Log.i("meow", prefs.getString("loginData","").toString()+"로그아웃")
                    supabase.auth.signOut()
                    var i: Intent = Intent(context, GoogleSignInActivity::class.java)
                    startActivity(i)
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

        Log.d("meow",_gotOutLookMail.toString())
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

                val olMailAPIUrl = "$defaultGraphResourceUrl/messages?\$select=sender,subject,receivedDateTime"

                callGraphAPI(
                    authenticationResult,
                    olMailAPIUrl,
                    {result->
                        Log.d(TAG, result.toString())
                        handleMailData(result) // Fragment의 메서드 호출
                    },
                    {error ->
                        Log.e(TAG, "Failed to call API: $error")
                        handleMailDataError(error) // Fragment의 메서드 호출

                    }
                )

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
    private fun callGraphAPI(authenticationResult: IAuthenticationResult, url: String,
                             onResponse: (gotOutlookMail) -> Unit,
                             onError: (Throwable) -> Unit
    ) {
        callGraphAPIUsingVolley(
            requireContext(),
            url,
            authenticationResult.accessToken,
            com.android.volley.Response.Listener{ response -> /* Successfully called graph, process data and send to UI */
                try {
                    val gson = Gson()
                    val result = gson.fromJson(response.toString(), gotOutlookMail::class.java)
                    onResponse(result)
                } catch (e: Exception) {
                    onError(e)
                }
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

        private var _gotOutLookMail:gotOutlookMail = gotOutlookMail("","",listOf<Value>())
    }
}