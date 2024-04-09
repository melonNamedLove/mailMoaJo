package com.melon.mailmoajo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.melon.mailmoajo.databinding.ActivityMailgogoBinding


class MailgogoActivity : AppCompatActivity() {
    val binding = ActivityMailgogoBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}