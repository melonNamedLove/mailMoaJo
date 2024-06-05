package com.melon.mailmoajo

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.melon.mailmoajo.GoogleSignInActivity.Companion.tokenprefs
import com.melon.mailmoajo.HomeActivity.Companion.database
import com.melon.mailmoajo.dataclass.PostResult
import com.melon.mailmoajo.dataclass.gotGMailData
import com.melon.mailmoajo.dataclass.gotGMailList
import com.melon.mailmoajo.dataclass.mailData
import com.melon.mailmoajo.dataclass.payload_json
import com.melon.mailmoajo.formatter.EmailFormatter
import com.melon.mailmoajo.formatter.MailTimeFormatter
import entities.Gmails
import entities.mails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.time.format.DateTimeFormatter
import java.util.Base64


private var gottenData:String = ""
var res:Int =0
class myWebViewClient: WebViewClient(){
    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        gottenData = ""
        res =0
        Log.d("meow", "your current url when webpage loading..$url")
        if(url.contains("http://localhost:5500/test.html", ignoreCase = true) && !url.contains("https://accounts.google.com/v3/signin")){
            Log.d("meow", url.toString())
            gottenData = url
            res = 1
            var temp=gottenData.substring(37)
            Log.d("meow", temp)
            val access_code = temp.split("&scope")[0]
            Log.d("meow", access_code)
            tokenprefs.edit().putString("access_code",access_code.toString()).apply()
        }
        if (res ==1 && gottenData!=""){



            view.setVisibility(View.GONE)

                           //여기
//            view?.goBack()
        }
    }


    override fun onPageFinished(view: WebView, url: String) {
        Log.d("kkkk", "your current url when webpage loading.. finish$url")
        super.onPageFinished(view, url)


    }

    override fun onLoadResource(view: WebView, url: String) {
//        Log.d("kkkk", "여기")
        // TODO Auto-generated method stub
        super.onLoadResource(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        println("when you click on any interlink on webview that time you got url :-${request?.url.toString()}")

        return super.shouldOverrideUrlLoading(view, request)
    }
    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(
        view: WebView?, handler: SslErrorHandler,
        error: SslError?
    ) {

    }


}

class GmailLoadActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail_load)
        val btn: Button = findViewById<Button>(R.id.rrbtn)
        val db = database(applicationContext)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)

        fun fetchMailData(userid: String, mailId: String, tokenWithBearer: String) {
            service.getMailData(userid, mailId, tokenWithBearer).enqueue(object : Callback<gotGMailData> {
                override fun onResponse(call: Call<gotGMailData>, response: Response<gotGMailData>) {
                    response.body()?.let {
                        val subject = it.payload.headers.find { header -> header.name == "Subject" }?.value ?: "No Subject"
                        val from = EmailFormatter().extractEmail(it.payload.headers.find { header -> header.name == "From" }?.value ?: "Unknown Sender")
                        val receivedHeader = it.payload.headers.firstOrNull { header -> header.name == "Received" }?.value ?: "No Received Header"
                        val received = MailTimeFormatter().extractDateTime(receivedHeader)?.let { pacificTime ->
                            MailTimeFormatter().convertToLocaleTimeAndFormat(pacificTime)
                        } ?: "0000-00-00 00:00:00"

                        db.gmailDao().insert(Gmails(received, from, subject, 0))
                        Log.w("meow", "Subject: $subject, From: $from, Received: $received")
                    }
                }

                override fun onFailure(call: Call<gotGMailData>, t: Throwable) {
                    Log.d("meow", "Failed API call with call: $call + exception: $t")
                }
            })
        }
        fun fetchMailList(userid: String, token: String, pageToken: String?) {
            val tokenWithBearer = "Bearer $token"
            service.getMailList(userid, tokenWithBearer, pageToken).enqueue(object : Callback<gotGMailList> {
                override fun onResponse(call: Call<gotGMailList>, response: Response<gotGMailList>) {
                    if (!response.isSuccessful) {
                        Log.d("meow", "nope")
                        response.errorBody()?.string()?.let { Log.d("meow", it) }
                        return
                    }

                    val mailList = response.body()
                    mailList?.messages?.forEach { message ->
                        val gson = Gson()
                        val stringToDataClass = gson.fromJson(message.toString(), mailData::class.java)
                        fetchMailData(userid, stringToDataClass.id, tokenWithBearer)
                    }

                    // 다음 페이지가 있으면 재귀 호출
                    val nextPageToken = mailList?.nextPageToken
                    if (nextPageToken != null && nextPageToken.isNotEmpty()) {
                        fetchMailList(userid, token, nextPageToken)
                    } else {
                        Log.d("meow", "모든 페이지 로딩 완료")
                        finish()
                    }
                }

                override fun onFailure(call: Call<gotGMailList>, t: Throwable) {
                    Log.d("meow", "Failed API call with call: $call + exception: $t")
                }
            })
        }


        btn.setOnClickListener(View.OnClickListener {
            var accesscode = tokenprefs.getString("access_code","")
            accesscode = URLDecoder.decode(accesscode, "UTF-8")
            Log.i("meow",accesscode)
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
                    Log.d("meow", response.body()?.refresh_token.toString())
                    TokenManager(applicationContext).GsaveRefreshToken(response.body()?.refresh_token.toString())


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

                        db.gmailDao().resetmails()  //------------------------------------------------------------------------------reset mail db
                        var pageTokenString = ""
                        var userid =  tokenprefs.getString("userid", "").toString()

                        fetchMailList(userid, tokenprefs.getString("accesstoken","").toString(), pageTokenString)


                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("meow", "Failed API call with call: " + call +
                            " + exception: " + t)
                }


            })




        })

        val webView:WebView = findViewById<WebView>(R.id.webweb)


        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = myWebViewClient()
//        webView.webChromeClient = WebChromeClient()
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"))
        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&response_type=code&scope=https://www.googleapis.com/auth/gmail.readonly https://www.googleapis.com/auth/userinfo.email&prompt=consent&access_type=offline")







    }

}