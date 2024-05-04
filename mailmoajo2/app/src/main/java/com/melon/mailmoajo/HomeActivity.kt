package com.melon.mailmoajo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.melon.mailmoajo.Database.ContactDatabase
import com.melon.mailmoajo.GoogleSignInActivity.Companion.tokenprefs
import com.melon.mailmoajo.databinding.ActivityAddContactBinding
import com.melon.mailmoajo.databinding.ActivityHomeBinding
import com.melon.mailmoajo.databinding.FragmentContactBinding
import com.melon.mailmoajo.fragment.ContactFragment
import com.melon.mailmoajo.fragment.MailFragment
import com.melon.mailmoajo.fragment.SettingsFragment
import entities.contacts

var listData = mutableListOf<contacts>()
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val db = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact-database"
        ).allowMainThreadQueries()
            .build()
        listData = db!!.contactDao().getAll().toMutableList()

//        contactprefs = this.getSharedPreferences("contact", MODE_PRIVATE)
        tokenprefs = this.getSharedPreferences("token", MODE_PRIVATE)
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

//            var contactprefset = contactprefs.getStringSet("contact", setOf<String>())
//            var contactset = contactprefset!!.toMutableSet()
//            if(contactset.size != 0){
//                for(data in contactset){
//                    var c:contact = contact(
//                        data.split("**")[0].toString(),
//                        data.split("**")[1].toString(),
//                        data.split("**")[2].toString()
//                    )
//                    Log.d("meow",data.split("**")[0])
//                    listData.add(c)
//                }
//            }
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



//        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))


    }
}