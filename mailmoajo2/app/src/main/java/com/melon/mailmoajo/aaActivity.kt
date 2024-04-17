package com.melon.mailmoajo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class aaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aa)
        val webView:WebView = findViewById<WebView>(R.id.webweb)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
//        webView.webChromeClient = WebChromeClient()
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"))
        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&response_type=code&scope=https://www.googleapis.com/auth/gmail.readonly&redirect_uri=http://localhost:5500/test.html")

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