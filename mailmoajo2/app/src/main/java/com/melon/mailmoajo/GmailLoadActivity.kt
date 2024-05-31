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
        //retrofit 구현체가 생성이 되서 retrofit이라는 변수에 할당이 된다.
        val db = database(applicationContext)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(AccessToken::class.java)

        fun fetchMailData(userid: String, mailId: String, tokenWithBearer: String) {
            service.getMailData(userid, mailId, tokenWithBearer).enqueue(object : Callback<gotGMailData> {
                override fun onResponse(call: Call<gotGMailData>, response: Response<gotGMailData>) {
                    response.body()?.let {
                        val subject = it.payload.headers.find { header -> header.name == "Subject" }?.value ?: "No Subject"
                        val from = EmailFormatter().extractEmail(it.payload.headers.find { header -> header.name == "From" }?.value ?: "Unknown Sender")
                        val receivedHeader = it.payload.headers.firstOrNull { header -> header.name == "Received" }?.value ?: "No Received Header"
                        val received = MailTimeFormatter().extractDateTime(receivedHeader)?.let { pacificTime ->
                            MailTimeFormatter().convertToLocaleTime(pacificTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        } ?: "No valid datetime found in the input string."

                        db.mailDao().insert(mails(received, from, subject, 0))
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

                        db.mailDao().resetmails()  //------------------------------------------------------------------------------reset mail db
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

//            var input = HashMap<String, String>()
//            input["code"] =  "4%2F0AeaYSHBYaXpqBjTXNc2yKRieyt_3TtZeCIUh0JxckUnBfaiRuRUjbRhGDWPXgrjjyOW70A"
//            input["client_id"] =  "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com"
//            input["scope"] =  "https://www.googleapis.com/auth/gmail.readonly"
//            input["client_secret"] =  "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"
//            input["redirect_uri"] =  "http://localhost:5500/test.html"
//            input["grant_type"] =  "authorization_code"
//            ServiceBuilder.api.postAccessToken(
//                ("code" to "4%2F0AeaYSHBJS3MPTGNi3JeLmD9_-mPotGOrK9OadvcY_FOp0jRxvAclsKtM4f1Q3kAKB1nJpQ",
//            "code" to "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
//            "code" to "https://www.googleapis.com/auth/gmail.readonly",
//                "code" to "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",
//                "code" to "http://localhost:5500/test.html",
//                "code" to "authorization_code").
//            )
//
//            enqueue(object :Callback<PostResult>{
//                override fun onResponse(call:Call<PostResult>, response: Response<PostResult>){
//
//                    if (response.isSuccessful){
//
//                        Log.d("meow", response.body().toString())
//                    }
//
//                }
//
//                override fun onFailure(call: Call<PostResult>, t: Throwable) {
//                    Log.d("meow", "failed")
//                }
//
//            })

//
//            val retrofittest2 = ServiceBuilder.buildService(AccessToken::class.java)
//
//            retrofittest2.postAccessToken(
//                "4%2F0AeaYSHBbgJuaG7FbSctk-TDAQLKTlvOZqETZqqlg4PXmq4fU1qOPoFA4TuJyq4rSVDt8Pg",
//                "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
//                "https://www.googleapis.com/auth/gmail.readonly",
//                "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",
//                "http://localhost:5500/test.html",
//                "authorization_code"
//                ).enqueue(object :Callback<PostResult>{
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
//                    Log.d("meow", "failed")
//                }
//                })



        })

        val webView:WebView = findViewById<WebView>(R.id.webweb)


        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = myWebViewClient()
//        webView.webChromeClient = WebChromeClient()
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"))
        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&scope=https://www.googleapis.com/auth/gmail.readonly https://www.googleapis.com/auth/userinfo.email&response_type=code")


//        val USER_AGENT:String = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19"
//        val webView:WebView = findViewById<WebView>(R.id.webweb)
////        webView.settings.setUserAgentString(System.getProperty("http.agent"))
//        webView.webChromeClient = WebChromeClient()
//        webView.getSettings().setUserAgentString(USER_AGENT);
//        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&response_type=code&scope=https://www.googleapis.com/auth/gmail.readonly&redirect_uri=http://localhost:5500/test.html")
//        var html = ""
//        try{
//            CoroutineScope(Dispatchers.Default).launch {
//                val html_get = async(Dispatchers.IO) {
//                    val client: HttpClient = HttpClientBuilder.create().build()
//                    val request = HttpGet("https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&scope=https://www.googleapis.com/auth/gmail.readonly&response_type=code")
//                    var response: HttpResponse = client.execute(request)
//                    response.toString()
//                    val `in`: InputStream = response.getEntity().getContent()
//                    val reader = BufferedReader(InputStreamReader(`in`))
//                    val str = StringBuilder()
//                    var line: String? = null
//                    while (reader.readLine().also { line = it } != null) {
//                        str.append(line)
//                    }
//                    `in`.close()
//                    html = str.toString()
//                }
//
//                webView.loadData(html_get.await().toString(),"text/html; charset=utf-8", "UTF-8")
////                Log.d("meow", "${html_get.await()} ")
//
//            }
//        }catch (e:Exception){
//            Log.d("meow", e.toString())
//        }finally {
////            shouldOverrideUrlLoading(webView, html_get.await)
//        }

//        shouldOverrideUrlLoading(webView,"https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&scope=https://www.googleapis.com/auth/gmail.readonly&response_type=code")
//        try {
//            val client: HttpClient = HttpClientBuilder.create().build()
//            val request = HttpGet("https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&scope=https://www.googleapis.com/auth/gmail.readonly&response_type=code")
//            val response: HttpResponse = client.execute(request)
//            Log.d("meow",response.toString())
//        }catch (e:Exception){
//            Log.d("meow","error")
//        }finally{
//            webView.loadUrl("https://www.naver.com")
//
//        }

//
//        Log.d("meow",response.toString())
//        webView.settings.javaScriptEnabled = true
//        webView.webViewClient = WebViewClient()
//        webView.webChromeClient = WebChromeClient()





    }

//    fun shouldOverrideUrlLoading(view: WebView, html: String): Boolean {
////        view.loadUrl(url!!)
//        return true
//    }
//    override fun onBackPressed() {
//        if(webView.canGoBack()){
//            // 웹싸이트에서 뒤로 갈 페이지가 존재 할 경우
//            webView.goBack()
//        }
//        else {
//            super.onBackPressed()
//        }
//    }
}