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
import com.android.volley.Response
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.gson.Gson
import com.melon.mailmoajo.CallbackInterface
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.GoogleSignInActivity
import com.melon.mailmoajo.GoogleSignInActivity.Companion.prefs
import com.melon.mailmoajo.HomeActivity.Companion.database
import com.melon.mailmoajo.MSGraphRequestWrapper
import com.melon.mailmoajo.MSGraphRequestWrapper.callGraphAPIUsingVolley
import com.melon.mailmoajo.OutlookLoadActivity
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import com.melon.mailmoajo.dataclass.Value
import com.melon.mailmoajo.dataclass.gotOutlookMail
import com.melon.mailmoajo.dataclass.token
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
class SettingsFragment : Fragment(){

    companion object {
        /* Azure AD Variables */
        var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
        var mAccount: IAccount? = null
        private val TAG = SettingsFragment::class.java.simpleName

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
// MSAL 설정

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
                    Log.d(TAG, exception.toString())
                }
            })


        binding.MSloadBtn.setOnClickListener {
            if(mSingleAccountApp ==null){ }

                // ProgressActivity로 이동
                val intent = Intent(context, OutlookLoadActivity::class.java).apply {
//                    putExtra("TOKEN", _OLtoken)
                }
                startActivity(intent)


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



    private fun showToastOnSignOut() {
        val signOutText = "Signed Out."
        Toast.makeText(context, signOutText, Toast.LENGTH_SHORT)
            .show()
    }


}