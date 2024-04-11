package com.melon.mailmoajo

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import com.melon.mailmoajo.databinding.ActivityMailgogoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MailgogoActivity : AppCompatActivity() {
    val binding = ActivityMailgogoBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding.mailWebView.webViewClient = WebViewClient()
//        binding.mailWebView.webChromeClient = WebChromeClient()
//
//        val settings = binding.mailWebView.settings
//        settings.javaScriptEnabled = true
//        settings.domStorageEnabled = true
//        binding.mailWebView.loadUrl("https://www.naver.com");

        setContentView(binding.root)

    }
}
