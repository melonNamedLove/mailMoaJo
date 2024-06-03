package com.melon.mailmoajo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.google.gson.Gson
import com.melon.mailmoajo.HomeActivity.Companion.mAccount
import com.melon.mailmoajo.HomeActivity.Companion.mSingleAccountApp
import com.melon.mailmoajo.databinding.ActivityOutlookLoadBinding
import com.melon.mailmoajo.dataclass.gotOutlookMail
import com.melon.mailmoajo.formatter.MailTimeFormatter
import com.melon.mailmoajo.fragment.SettingsFragment
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import entities.OutlookMails
import entities.mails

class OutlookLoadActivity : AppCompatActivity(), CallbackInterface {

    private lateinit var binding: ActivityOutlookLoadBinding

    companion object {
        private const val TAG = "OutlookLoadActivity"
    }

    var _OLtoken: String = ""
    private var remainingMails = 0 // 저장할 메일의 수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding 초기화
        binding = ActivityOutlookLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        binding.outlookprogressbar.visibility = View.GONE
        val scopes = arrayOf(
            "Mail.Read",
            "email",
            "User.Read"
        )

        val signInParameters: SignInParameters = SignInParameters.builder()
            .withActivity(this)
            .withLoginHint(null)
            .withScopes(scopes.toList())
                        .withCallback(authInteractiveCallback)
            .build()


        // mSingleAccountApp 체크 후 signIn 호출
        mSingleAccountApp?.signIn(signInParameters)
            ?: Log.e(TAG, "mSingleAccountApp is null")


    }

    private val authInteractiveCallback: AuthenticationCallback
        get() = object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                Log.d(TAG, "Successfully authenticated")
                _OLtoken = authenticationResult.accessToken
                Log.d(TAG, "ID Token: " + authenticationResult.account.claims!!["id_token"])

                mAccount = authenticationResult.account
                binding.outlookprogressbar.visibility = View.VISIBLE

                val olMailAPIUrl = "${MSGraphRequestWrapper.MS_GRAPH_ROOT_ENDPOINT}v1.0/me/messages?\$select=sender,subject,receivedDateTime"

                callGraphAPI(
                    _OLtoken,
                    olMailAPIUrl,
                    { result ->
                        Log.d(TAG, result.toString())
                        onMailDataReceived(result)
                    },
                    { error ->
                        Log.e(TAG, "Failed to call API: $error")
                        onMailDataError(error)
                    }
                )


            }

            override fun onError(exception: MsalException) {
                Log.d(TAG, "Authentication failed: $exception")
                if (exception is MsalClientException) {
                    // MSAL 내부 예외 처리
                } else if (exception is MsalServiceException) {
                    // STS와 통신하는 동안 발생한 예외 처리
                }
            }

            override fun onCancel() {
                Log.d(TAG, "User cancelled login.")
            }
        }

    fun callGraphAPI(token: String, url: String,
                             onResponse: (gotOutlookMail) -> Unit,
                             onError: (Throwable) -> Unit) {
        MSGraphRequestWrapper.callGraphAPIUsingVolley(
            this,
            url,
            token,
            Response.Listener { response ->
                try {
                    val gson = Gson()
                    val result = gson.fromJson(response.toString(), gotOutlookMail::class.java)
                    onResponse(result)
                } catch (e: Exception) {
                    onError(e)
                }
            },
            Response.ErrorListener { error ->
                Log.d(TAG, "Error: $error")
                onError(error)
            })
    }

    override fun onMailDataReceived(data: gotOutlookMail) {
        Log.d(TAG, "Received mail data: $data")
        remainingMails += data.value.size // 저장할 메일 수
        val db = HomeActivity.database(applicationContext)
        db.outlookDao().resetmails()  //------------------------------------------------------------------------------reset mail db
        saveMailDataToDatabase(data)

        data.nextLink?.let { nextPageUrl ->
            callGraphAPI(
                _OLtoken, nextPageUrl,
                { result ->
                    Log.d(TAG, result.toString())
                    onMailDataReceived(result)
                },
                { error ->
                    Log.e(TAG, "Failed to call API: $error")
                    onMailDataError(error)
                }
            )
        }?:run{
            checkIfAllMailsSaved()
        }
    }

    override fun onMailDataError(error: Throwable) {
        Log.e(TAG, "Error receiving mail data: $error")
    }
    private fun checkIfAllMailsSaved() {
        if (remainingMails == 0) {
            finish() // 모든 메일 저장이 완료되면 Activity를 종료
        }
    }
    private fun saveMailDataToDatabase(data: gotOutlookMail) {
        val db = HomeActivity.database(this)
        data.value.forEach { mail ->

            val received = MailTimeFormatter().extractDateTime(mail.receivedDateTime)
            val formatted = MailTimeFormatter().convertToLocaleTimeAndFormat(received!!)
            Log.d("wow", formatted)

            val mailEntity = OutlookMails(
                title = mail.subject,
                receivedTime = formatted,
                sender = mail.sender.emailAddress.address,
                mailfolderid = 0
            )
//            Log.d("wow", mailEntity.receivedTime.toString())
//
//            Log.d("wow", MailTimeFormatter().convertToLocaleTimeAndFormat(received!!).toString())

            db.outlookDao().insert(mailEntity)
            remainingMails-- // 저장할 메일 수를 감소
            checkIfAllMailsSaved() // 메일 저장이 완료되었는지 확인
        }
    }
}
