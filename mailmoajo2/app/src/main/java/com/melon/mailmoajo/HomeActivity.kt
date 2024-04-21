package com.melon.mailmoajo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.melon.mailmoajo.databinding.ActivityHomeBinding
import com.melon.mailmoajo.fragment.ContactFragment
import com.melon.mailmoajo.fragment.MailFragment
import com.melon.mailmoajo.fragment.SettingsFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragArea, MailFragment())
            commit()
        }
        //mail fragment 버튼
        binding.mailFragBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragArea, MailFragment())
                commit()
            }
        }


        //contact fragment 버튼
        binding.contactFragBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragArea, ContactFragment())
                commit()
            }
        }

        //settings fragment 버튼
        binding.settingsFragBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragArea, SettingsFragment())
                commit()
            }
        }

    }
}