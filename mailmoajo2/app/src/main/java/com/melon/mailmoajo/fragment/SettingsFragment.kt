package com.melon.mailmoajo.fragment

import android.R
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
import com.melon.mailmoajo.PostResult
import com.melon.mailmoajo.adapter.OptionListItemAdapter
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import com.melon.mailmoajo.dataclass.mailData
import com.melon.mailmoajo.dataclass.mailId
import com.melon.mailmoajo.gmailLoadBtn
import com.melon.mailmoajo.gotMailList
import com.melon.mailmoajo.payload_json
import com.melon.mailmoajo.supabase
import com.microsoft.graph.models.PublicClientApplication
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
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


var mailmail = mutableListOf<mailId>()
/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    /* Azure AD Variables */
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private var mAccount: IAccount? = null
    var listItemAdapter = OptionListItemAdapter()
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

        binding.optionListView.adapter = listItemAdapter

        //기존 지메일 로드 액티비티 이동 setOnClickListener, 1번째 리스트로 구현
//        binding.gmailLoadBtn.setOnClickListener{
//            var i: Intent = Intent(context, GmailLoadActivity::class.java)
//            startActivity(i)
//        }

        listItemAdapter.addItem("0번째 리스트입니다.","로그가 찍힙니다.", requireContext(), 0)
        listItemAdapter.addItem("1번째 리스트입니다.","gmailLoadFun과 똑같은 역할을 합니다.", requireContext(), 1)
        listItemAdapter.addItem("2번째 리스트입니다.","retro token load 뭐뭐랑 똑같은 역할을 합니다.", requireContext(), 2)
        listItemAdapter.addItem("3번째 리스트입니다.","retro2랑 똑같은 역할을 합니다.", requireContext(), 3)

/*      기존 retro2 구현 setOnClickListener, 2번째 리스트로 구현
        binding.retro2Btn.setOnClickListener{
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(AccessToken::class.java)

            val userid =tokenprefs.getString("userid","").toString()
            service.getMailList(
                userid,
                "Bearer "+tokenprefs.getString("accesstoken","").toString(),
            ).enqueue(object :Callback<gotMailList>{
                override fun onResponse(call: Call<gotMailList>, response: Response<gotMailList>) {


                    Log.d("meow",  response.code().toString())
                    if(response.isSuccessful.not()){
                        Log.d("meow", "nope")
                        Log.d("meow", response.errorBody()?.string()!!)

                        return
                    }
                    val msg =response.body()?.messages
//                    msg?.get(0)
                    Log.d("meow", response.body()?.messages.toString())
//
                    var gson = Gson()
                    for (i:Int in 0 until 50){
                        Log.w("meow", response.body()!!.messages[i].toString())
                        val stringtodataclass = gson.fromJson(response.body()!!.messages[i].toString(), mailData::class.java)
                        Log.d("meow", stringtodataclass.id)
                        mailmail.add(mailId(stringtodataclass.id))
//        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))
                    }
//                    Log.i("meow",stringtodataclass.mailids.toString())
//                    item.messages[0]
                }

                override fun onFailure(call: Call<gotMailList>, t: Throwable) {
                    Log.d("meow", "Failed API call with call: " + call +
                            " + exception: " + t)
                }


            })

        }
*/

/*        기존 access token load setOnClickListener, 3번째 리스트로 구현
        binding.retroBtn.setOnClickListener{
            var accesscode = tokenprefs.getString("access_code","")
            accesscode = URLDecoder.decode(accesscode, "UTF-8")
            Log.i("meow",accesscode)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(AccessToken::class.java)

            service.postAccessToken(
                accesscode,
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

                        //gson 인스턴스 생성
                        var gson = Gson()

                        val stringtodataclass = gson.fromJson(payload, payload_json::class.java)

                        Log.i("meow",stringtodataclass.email)
                        tokenprefs.edit().putString("userid",stringtodataclass.email).apply()

                        //json을 클래스로 변환


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
 */

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