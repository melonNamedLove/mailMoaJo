package com.melon.mailmoajo

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder


class aaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aa)

        val webView:WebView = findViewById<WebView>(R.id.webweb)

        webView.webChromeClient = WebChromeClient()

        CoroutineScope(Dispatchers.Default).launch {
            val html_get = async(Dispatchers.IO) {
                val client: HttpClient = HttpClientBuilder.create().build()
                val request = HttpGet("https://accounts.google.com/o/oauth2/v2/auth?client_id=281381475185-ed4qlcvb6opietckobi32g0k9s36glvb.apps.googleusercontent.com&redirect_uri=http://localhost:5500/test.html&scope=https://www.googleapis.com/auth/gmail.readonly&response_type=code")
                var response: HttpResponse = client.execute(request)
                response.toString()
            }

            Log.d("meow", "${html_get.await()} ")
        }

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

//        var html = ""
//        val `in`: InputStream = response.getEntity().getContent()
//        val reader = BufferedReader(InputStreamReader(`in`))
//        val str = StringBuilder()
//        var line: String? = null
//        while (reader.readLine().also { line = it } != null) {
//            str.append(line)
//        }
//        `in`.close()
//        html = str.toString()
//
//        Log.d("meow",response.toString())
//        webView.settings.javaScriptEnabled = true
//        webView.webViewClient = WebViewClient()
//        webView.webChromeClient = WebChromeClient()

    }
    fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        view.loadUrl(url!!)
        return true
    }
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