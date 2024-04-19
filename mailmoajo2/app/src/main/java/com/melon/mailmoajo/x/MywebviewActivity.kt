package com.melon.mailmoajo.x

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.melon.mailmoajo.databinding.ActivityMywebviewBinding

class MywebviewActivity : AppCompatActivity() {

    val binding = ActivityMywebviewBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//
        val webView1 = binding.mailWebView
        webView1.loadUrl("https://www.google.com")

//        val webView: WebView by lazy{
//            binding.mailWebView
//        }
//
//        webView.settings.apply {
//            javaScriptEnabled = true
//        }
//
//        webView.webViewClient = WebViewClient()
//
//        webView.loadUrl("https://www.google.com")

    }
}