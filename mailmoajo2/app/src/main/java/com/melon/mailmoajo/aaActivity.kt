package com.melon.mailmoajo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class aaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aa)

        val webView = findViewById<WebView>(R.id.webweb)
        webView.loadUrl("https://www.naver.com")
    }
}