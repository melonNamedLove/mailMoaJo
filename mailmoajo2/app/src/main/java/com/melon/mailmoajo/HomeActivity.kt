package com.melon.mailmoajo

import android.app.ActionBar.DISPLAY_SHOW_CUSTOM
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.melon.mailmoajo.Database.MailMoaJoDatabase
import com.melon.mailmoajo.GoogleSignInActivity.Companion.tokenprefs
import com.melon.mailmoajo.adapter.ContactAdapter
import com.melon.mailmoajo.adapter.ContactItemOnClick
import com.melon.mailmoajo.adapter.MailFolderAdapter
import com.melon.mailmoajo.databinding.ActivityHomeBinding
import com.melon.mailmoajo.dataclass.gotGMailData
import com.melon.mailmoajo.dataclass.gotGMailList
import com.melon.mailmoajo.dataclass.mailData
import com.melon.mailmoajo.formatter.EmailFormatter
import com.melon.mailmoajo.formatter.MailTimeFormatter
import com.melon.mailmoajo.fragment.ContactDetailFragment
import com.melon.mailmoajo.fragment.ContactFragment
import com.melon.mailmoajo.fragment.MailFolderFragment
import com.melon.mailmoajo.fragment.MailFragment
import com.melon.mailmoajo.fragment.SettingsFragment
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import entities.Gmails
import entities.contacts
import entities.orderedMailFolders
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        fun getEncryptedSharedPreferences(): SharedPreferences {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            return EncryptedSharedPreferences.create(
                "encrypted_prefs",
                masterKeyAlias,
                this@HomeActivity,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        //fab onclicklistener 설정
        binding.fab.setOnClickListener{
            if (binding.fab.text.equals("동기화")){
                CoroutineScope(Dispatchers.IO).launch {
                    val gmailCount:Int = db.gmailDao().getGmailCount()
                    val outlookCount:Int = db.outlookDao().getOutlookCount()
                        if(gmailCount>0 && outlookCount>0){//둘 다 있을 경우
                            Log.d("wow", TokenManager(this@HomeActivity).getRefreshToken().toString())

                            val tokenRefreshed = TokenManager(this@HomeActivity).refreshAccessTokenSuspend()
                            withContext(Dispatchers.Main){
                                if (tokenRefreshed) {
                                    val sharedPreferences = TokenManager(this@HomeActivity).getEncryptedSharedPreferences()
                                    val token = sharedPreferences.getString("access_token", null)
                                    val latestGmail: Gmails = db.gmailDao().getMostRecentMail() ?: Gmails("0000-00-00 00:00:00", "not Found", "not Found", 0)
                                    Log.d("wow", latestGmail.toString())
                                    fetchMailList_toUpdate(token = token!!, userid = tokenprefs.getString("userid", "").toString(), latestMail = latestGmail, pageToken = "")
                                } else {
                                    Log.d("wow", "토큰 갱신 실패")
                                }
                            }

                        } else if(gmailCount>0  || outlookCount==0) {//gmail만 load됐을 때
                            Log.d("wow", TokenManager(this@HomeActivity).getRefreshToken().toString())
//                            TokenManager(this@HomeActivity).refreshAccessToken()
//                            val latestGmail:Gmails = db.gmailDao().getMostRecentMail()?:Gmails("0000-00-00 00:00:00","not Found","not Found",0)

                        }else if(gmailCount==0  || outlookCount>0){//outlook만 load됐을 때


                        }else if(gmailCount ==0  && outlookCount==0){//아무것도 없을 경우
                            Log.d("meow","로그인부터 하슈")
                        }else{//이상한놈
                            Log.d("meow","뭔가 잘못됐다!")
                        }



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
    var sync_count = 0



    //gmail reload로직
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ApiService::class.java)

    fun fetchMailData(userid: String, mailId: String, tokenWithBearer: String, latestMail: Gmails, onNewMailFetched: (Boolean) -> Unit) {
        service.getMailData(userid, mailId, tokenWithBearer).enqueue(object :
            Callback<gotGMailData> {
            override fun onResponse(call: Call<gotGMailData>, response: Response<gotGMailData>) {
                response.body()?.let {
                    val subject = it.payload.headers.find { header -> header.name == "Subject" }?.value ?: "No Subject"
                    val from = EmailFormatter().extractEmail(it.payload.headers.find { header -> header.name == "From" }?.value ?: "Unknown Sender")
                    val receivedHeader = it.payload.headers.firstOrNull { header -> header.name == "Received" }?.value ?: "No Received Header"
                    val received = MailTimeFormatter().extractDateTime(receivedHeader)?.let { pacificTime ->
                        MailTimeFormatter().convertToLocaleTimeAndFormat(pacificTime)
                    } ?: "0000-00-00 00:00:00"

                    Log.w("meow", "Subject: $subject, From: $from, Received: $received")
                    if(subject == latestMail.title && from == latestMail.sender && received == latestMail.receivedTime){
                        Log.d("wow","no new mail")
                        onNewMailFetched(false) // 새로운 메일이 없음
                    }else{
                        database(this@HomeActivity).gmailDao().insert(Gmails(received, from, subject, 0))
                        Log.d("wow"," new mail!")
                        onNewMailFetched(true) // 새로운 메일이 있음
                    }

                }
            }

            override fun onFailure(call: Call<gotGMailData>, t: Throwable) {
                Log.d("meow", "Failed API call with call: $call + exception: $t")
            }
        })
    }
    fun fetchMailList_toUpdate(userid: String, token: String, pageToken: String?, latestMail:Gmails) {
        val tokenWithBearer = "Bearer $token"
        service.getMailList(userid, tokenWithBearer, pageToken).enqueue(object :
            Callback<gotGMailList> {
            override fun onResponse(call: Call<gotGMailList>, response: Response<gotGMailList>) {
                if (!response.isSuccessful) {
                    Log.d("meow", "nope")
                    response.errorBody()?.string()?.let { Log.d("meow", it) }
                    return
                }


                val mailList = response.body()
                var stopFetching = false // Fetch를 멈출지 여부를 결정하는 플래그
                var pendingResponses = mailList?.messages?.size ?: 0 // 비동기 작업 수

                mailList?.messages?.forEach { message ->
                    val gson = Gson()
                    val stringToDataClass = gson.fromJson(message.toString(), mailData::class.java)
                    fetchMailData(userid, stringToDataClass.id, tokenWithBearer, latestMail) { newMailFetched ->
                        pendingResponses--
                        if (!newMailFetched) {
                            stopFetching = true
                        }

                        if (pendingResponses == 0) {
                            if (stopFetching) {
                                Log.d("meow", "Fetching stopped")
                            } else {
                                // 모든 비동기 작업이 완료된 후 재귀 호출 결정
                                val nextPageToken = mailList.nextPageToken
                                if (nextPageToken != null && nextPageToken.isNotEmpty()) {
                                    fetchMailList_toUpdate(userid, token, nextPageToken, latestMail)
                                } else {
                                    Log.d("meow", "모든 페이지 로딩 완료")
                                }
                            }
                        }
                    }

                    if (stopFetching) {
                        return@forEach
                    }
                }

                if (mailList?.messages.isNullOrEmpty()) {
                    Log.d("meow", "메일 없음")
                }
            }
            override fun onFailure(call: Call<gotGMailList>, t: Throwable) {
                Log.d("meow", "Failed API call with call: $call + exception: $t")
            }
        })
    }

}