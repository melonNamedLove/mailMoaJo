package com.melon.mailmoajo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity


private var gottenData:String = ""
class myWebViewClient: WebViewClient(){
    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        gottenData = ""
        var res = 0
        Log.d("meow", "your current url when webpage loading..$url")
        if(url.contains("localhost", ignoreCase = true)){
            Log.d("meow", url.toString())
            gottenData = url
            res = 1
        }
        if (res ==1 && gottenData!=""){
            view.setVisibility(View.GONE)
//            view?.goBack()
        }
    }
//    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//        super.onPageStarted(view, url, favicon)
//        Log.d("kkkk", "your current url when webpage loading..$url")
////                url.spl
//        if(url.equals("URL after user logged In")){
//            //start new activity
//        }
//    }

    override fun onPageFinished(view: WebView, url: String) {
        Log.d("kkkk", "your current url when webpage loading.. finish$url")
        super.onPageFinished(view, url)


    }

    override fun onLoadResource(view: WebView, url: String) {
        Log.d("kkkk", "여기")
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

class aaActivity : AppCompatActivity() {

    var currentUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aa)
        val btn: Button = findViewById<Button>(R.id.rrbtn)
        btn.setOnClickListener(View.OnClickListener {

            Log.d("meow", gottenData)
            if (gottenData !=""){
                var temp=gottenData.substring(37)
                Log.d("meow", temp)
                val access_code = temp.split("&scope")[0]
                Log.d("meow", access_code)
                findViewById<TextView>(R.id.accesscodeTV).setText(access_code)
            }
        })

        val webView:WebView = findViewById<WebView>(R.id.webweb)


        webView.settings.javaScriptEnabled = true
//        webView.settings.domStorageEnabled = true
        webView.webViewClient = myWebViewClient()
//        webView.webChromeClient = WebChromeClient()
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"))
        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&scope=https://www.googleapis.com/auth/gmail.readonly https://www.googleapis.com/auth/userinfo.email&response_type=code")


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