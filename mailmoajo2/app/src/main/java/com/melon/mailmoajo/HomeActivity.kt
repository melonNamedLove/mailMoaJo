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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import entities.contacts
import entities.orderedMailFolders
import io.ktor.util.reflect.instanceOf

var contactlistData = mutableListOf<contacts>()
var mailfolderlistData = mutableListOf<orderedMailFolders>()
class HomeActivity : AppCompatActivity() {
    companion object {
        fun database(context: Context):MailMoaJoDatabase{

            val db = Room.databaseBuilder(
                context,
                MailMoaJoDatabase::class.java,
                "mailmoajo-database"
            ).allowMainThreadQueries()
                .build()
            return db
        }
    }
    private val frame: RelativeLayout by lazy { // activity_main의 화면 부분
        findViewById(R.id.nav_host)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.nav_bar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //db on
        val db = database(applicationContext)

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

                    toolbarBodyTemplate.title="주소록"
                    binding.fab.setText("추가")

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
                }else if(currentFrag.instanceOf(MailFragment::class)){
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host, MailFolderFragment()).commit()

                    toolbarBodyTemplate.title="메일함"
                }
            }
        }

        var re = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == 1){
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

                //gmail이 비어있을 경우
                //outlook이 비어있을 경우
                //아무것도 없을 경우
                //둘 다 있을 경우
                val gmailCount:Int = db.gmailDao().getGmailCount()
                val outlookCount:Int = db.outlookDao().getOutlookCount()
                if(gmailCount>0 && outlookCount>0){

                } else if(gmailCount ==0  || outlookCount==0){

                }else if(gmailCount ==0  && outlookCount==0){
                    Log.d("meow","로그인부터 하슈")
                }else{
                    Log.d("meow","뭔가 잘못됐다!")
                }



            }else if(binding.fab.text.equals("추가")){


                var ii: Intent = Intent(this, AddContactActivity::class.java)
                re.launch(ii)

            }else{
                Log.d("fabfab", "dd")
            }
        }

        tokenprefs = this.getSharedPreferences("token", MODE_PRIVATE)

        this.onBackPressedDispatcher.addCallback(this, onBackPressed)

    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()

    }

}