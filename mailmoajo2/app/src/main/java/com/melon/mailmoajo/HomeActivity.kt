package com.melon.mailmoajo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    private val frame: RelativeLayout by lazy { // activity_main의 화면 부분
        findViewById(R.id.nav_host)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.nav_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().add(frame.id, MailFragment()).commit()

        // 하단 네비게이션 바 클릭 이벤트 설정
        bottomNagivationView.setOnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.mailfragItem -> {
                    replaceFragment(MailFragment())
                    true
                }
                R.id.contactfragItem -> {
                    replaceFragment(ContactFragment())
                    true
                }
                R.id.SettingsfragItem -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact-database"
        ).allowMainThreadQueries()
            .build()
        listData = db!!.contactDao().getAll().toMutableList()

//        contactprefs = this.getSharedPreferences("contact", MODE_PRIVATE)
        tokenprefs = this.getSharedPreferences("token", MODE_PRIVATE)
//        supportFragmentManager.beginTransaction().apply{
//            replace(R.id.fragArea, MailFragment())
//            commit()
//        }
        //mail fragment 버튼
//        binding.navBar
//        setOnClickListener {
//            supportFragmentManager.beginTransaction().apply{
//                replace(R.id.fragArea, MailFragment())
//                commit()
//            }
//        }


        //contact fragment 버튼
//        binding.contactFragBtn.setOnClickListener {
//
////            var contactprefset = contactprefs.getStringSet("contact", setOf<String>())
////            var contactset = contactprefset!!.toMutableSet()
////            if(contactset.size != 0){
////                for(data in contactset){
////                    var c:contact = contact(
////                        data.split("**")[0].toString(),
////                        data.split("**")[1].toString(),
////                        data.split("**")[2].toString()
////                    )
////                    Log.d("meow",data.split("**")[0])
////                    listData.add(c)
////                }
////            }
//            supportFragmentManager.beginTransaction().apply{
//                replace(R.id.fragArea, ContactFragment())
//                commit()
//            }
//        }

//        //settings fragment 버튼
//        binding.settingsFragBtn.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply{
//                replace(R.id.fragArea, SettingsFragment())
//                commit()
//            }
//        }



//        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))
        supportFragmentManager.beginTransaction().add(binding.navHost.id, MailFragment()).commit()
        binding.navBar.setOnClickListener{
            replaceFragment(
                when (it.id) {
                    R.id.mailfragItem -> MailFragment()
                    R.id.contactfragItem -> ContactFragment()
                    else -> SettingsFragment()
                }
            )
            true
        }
    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()
    }

}