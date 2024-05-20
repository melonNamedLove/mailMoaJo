package com.melon.mailmoajo

import android.app.ActionBar.DISPLAY_SHOW_CUSTOM
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.melon.mailmoajo.Database.MailMoaJoDatabase
import com.melon.mailmoajo.GoogleSignInActivity.Companion.tokenprefs
import com.melon.mailmoajo.adapter.ContactAdapter
import com.melon.mailmoajo.adapter.ContactItemOnClick
import com.melon.mailmoajo.adapter.MailFolderAdapter
import com.melon.mailmoajo.databinding.ActivityHomeBinding
import com.melon.mailmoajo.fragment.ContactDetailFragment
import com.melon.mailmoajo.fragment.ContactFragment
import com.melon.mailmoajo.fragment.MailFolderFragment
import com.melon.mailmoajo.fragment.MailFragment
import com.melon.mailmoajo.fragment.SettingsFragment
import entities.contacts
import entities.orderedMailFolders
import io.ktor.util.reflect.instanceOf

var contactlistData = mutableListOf<contacts>()
var mailfolderlistData = mutableListOf<orderedMailFolders>()
class HomeActivity : AppCompatActivity() {


    private val frame: RelativeLayout by lazy { // activity_main의 화면 부분
        findViewById(R.id.nav_host)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.nav_bar)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(
//            R.menu.template_toolbar_menu,
//            menu
//        )
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //db on
        val db = Room.databaseBuilder(
            applicationContext,
            MailMoaJoDatabase::class.java,
            "mailmoajo-database"
        ).allowMainThreadQueries()
            .build()
//        Room.databaseBuilder(
//            applicationContext,
//            MailMoaJoDatabase::class.java,
//            "contact-database"
//        ).addMigrations()
        contactlistData = db!!.contactDao().getAll().toMutableList()

        mailfolderlistData = db!!.mailfolderDao().getAll().toMutableList()
        if(mailfolderlistData.size==0){     //기본 메일함인 전체보기 생성
            db.mailfolderDao().init(
                orderedMailFolders(
                    "전체보기",
                    "ALL"
                )
            )
            mailfolderlistData = db!!.mailfolderDao().getAll().toMutableList()
        }
        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().add(frame.id, MailFolderFragment()).commit()

        val toolbarBodyTemplate = binding.bodyToolbar.toolbar
        setSupportActionBar(toolbarBodyTemplate)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarBodyTemplate.title="메일함"


        // 하단 네비게이션 바 클릭 이벤트 설정
        bottomNagivationView.setOnItemSelectedListener {item ->
            when(item.itemId) {
                R.id.mailfragItem -> {
                    replaceFragment(MailFolderFragment())
                    toolbarBodyTemplate.title="메일함"
//                    if(!binding.fab.isShown){
                        binding.fab.show()
//                    }
                    binding.fab.setText("동기화")
                    binding.fab.setIconResource(R.drawable.sync_24dp_fill0_wght400_grad0_opsz24)
                    mailfolderlistData = db.mailfolderDao().getAll().toMutableList()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    true
                }
                R.id.contactfragItem -> {
                    replaceFragment(ContactFragment())
                    toolbarBodyTemplate.title="주소록"
//                    if(!binding.fab.isShown){
                        binding.fab.show()
//                    }
                    binding.fab.setText("추가")
                    binding.fab.setIconResource(R.drawable.person_add_24dp_fill0_wght400_grad0_opsz24)
                    true
                }
                R.id.SettingsfragItem -> {
                    replaceFragment(SettingsFragment())
                    toolbarBodyTemplate.title="설정"
                    binding.fab.hide()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    true
                }
                else -> false
            }
        }

        //뒤로가기 버튼
        val onBackPressed = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val currentFrag = supportFragmentManager.fragments.last()
                if (currentFrag.instanceOf(ContactDetailFragment::class) ){
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host, ContactFragment()).commit()
                }else if (currentFrag.instanceOf(ContactFragment::class)){
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host, MailFolderFragment()).commit()
                    toolbarBodyTemplate.title="메일함"
//                    if(!binding.fab.isShown){
                    binding.fab.show()
//                    }
                    binding.fab.setText("동기화")
                    binding.fab.setIconResource(R.drawable.sync_24dp_fill0_wght400_grad0_opsz24)
                    mailfolderlistData = db.mailfolderDao().getAll().toMutableList()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    bottomNagivationView.menu.findItem(R.id.mailfragItem).setChecked(true)
                }else if(currentFrag.instanceOf(SettingsFragment::class)){
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host, ContactFragment()).commit()
                    toolbarBodyTemplate.title="주소록"
//                    if(!binding.fab.isShown){
                    binding.fab.show()
//                    }
                    binding.fab.setText("추가")
                    binding.fab.setIconResource(R.drawable.person_add_24dp_fill0_wght400_grad0_opsz24)
                    contactlistData = db.contactDao().getAll().toMutableList()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    bottomNagivationView.menu.findItem(R.id.contactfragItem).setChecked(true)
                }
            }
        }

        var re = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == 1){
                val db = Room.databaseBuilder(
                    this,
                    MailMoaJoDatabase::class.java,
                    "mailmoajo-database"
                ).allowMainThreadQueries()
                    .build()
                contactlistData = db.contactDao().getAll().toMutableList()
                findViewById<RecyclerView>(R.id.contactRcv).adapter = ContactAdapter(contactlistData,ContactItemOnClick())

//                binding!!.registText.text ="yes"
//                findViewById<RecyclerView>(R.id.contactRcv).adapter!!.notifyItemInserted(listData.size)
            }else if(result.resultCode == 0){
//                binding!!.registText.text="no"
            }else{
//                binding!!.registText.text="error"
            }

//            binding.contactRcv.adapter!!.notifyDataSetChanged()
        }

        //fab onclicklistener 설정
        binding.fab.setOnClickListener{
            if (binding.fab.text.equals("동기화")){

            }else if(binding.fab.text.equals("추가")){


                var ii: Intent = Intent(this, AddContactActivity::class.java)
                re.launch(ii)

            }else{
                Log.d("fabfab", "dd")
            }
        }

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
        this.onBackPressedDispatcher.addCallback(this, onBackPressed)

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
//        supportFragmentManager.beginTransaction().add(binding.navHost.id, MailFragment()).commit()
//        binding.navBar.setOnClickListener{
//            replaceFragment(
//                when (it.id) {
//                    R.id.mailfragItem -> MailFragment()
//                    R.id.contactfragItem -> ContactFragment()
//                    else -> SettingsFragment()
//                }
//            )
//            true
//        }
    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()

    }

}