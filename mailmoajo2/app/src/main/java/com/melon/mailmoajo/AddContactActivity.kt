package com.melon.mailmoajo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.ActivityAddContactBinding
import com.melon.mailmoajo.fragment.ContactFragment

class AddContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityAddContactBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addbtn.setOnClickListener{
            var contactprefset = contactprefs.getStringSet("contact", setOf<String>())

            var contactset = contactprefset!!.toMutableSet()
            var name = "-"
            var google = "-"
            var outlook = "-"

            if (!binding.nameET.text.toString().equals("")){
                name =binding.nameET.text.toString()
            }
            if (!binding.googleEmailET.text.toString().equals("")){
                google =binding.googleEmailET.text.toString()
            }
            if (!binding.outlookEmailET.text.toString().equals("")){
                outlook =binding.outlookEmailET.text.toString()
            }
            val concatContactString = name+"**"+google+"**"+outlook
            contactset!!.add(concatContactString)
            contactprefs.edit().putStringSet("contact", contactset.toSet()).apply()
            Log.w("meow","")

            finish()
        }

    }
}