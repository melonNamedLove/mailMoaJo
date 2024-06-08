package com.melon.mailmoajo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.melon.mailmoajo.DAOs.GmailDao
import com.melon.mailmoajo.DAOs.OutlookDao
import com.melon.mailmoajo.DAOs.mailDao
import com.melon.mailmoajo.Database.MailMoaJoDatabase
import com.melon.mailmoajo.HomeActivity.Companion.database
//import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.ActivityAddContactBinding
import entities.contacts
import entities.orderedMailFolders

class AddContactActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityAddContactBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val toolbarBodyTemplate = binding.bodyToolbar.toolbar
        setSupportActionBar(toolbarBodyTemplate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarBodyTemplate.title="주소록 추가"
        toolbarBodyTemplate.setNavigationOnClickListener(View.OnClickListener(){    //appbar 뒤로가기 버튼 눌렀을 때
//            Log.d("ererer", "onclickreturn")
//            activity.supportFragmentManager.beginTransaction().remove(this).addToBackStack(null).commit()
//            toolbarBodyTemplate.title="주소록"
//            fab.setIconResource(R.drawable.person_add_24dp_fill0_wght400_grad0_opsz24)
//            fab.setText("추가")
//            contactlistData = db!!.contactDao().getAll().toMutableList()
            finish()
        })

        var i: Intent = Intent(this, HomeActivity::class.java)


        binding.addbtn.setOnClickListener{
//            var contactprefset = contactprefs.getStringSet("contact", setOf<String>())

//            var contactset = contactprefset!!.toMutableSet()
            var name = ""
            var mail1 = ""
            var mail2 = ""
            var mail3 = ""

            if (!binding.nameET.text.toString().equals("")){
                name =binding.nameET.text.toString()
            }
            if (!binding.mail1ET.text.toString().equals("")){
                mail1 =binding.mail1ET.text.toString()
            }
            if (!binding.mail2ET.text.toString().equals("")){
                mail2 =binding.mail2ET.text.toString()
            }
            if (!binding.mail3ET.text.toString().equals("")){
                mail3 =binding.mail3ET.text.toString()
            }
//            val concatContactString = name+"**"+google+"**"+outlook
//            contactset!!.add(concatContactString)
//            contactprefs.edit().putStringSet("contact", contactset.toSet()).apply()


            var newContact = contacts(name, mail1, mail2, mail3)

//             싱글톤 패턴을 사용하지 않은 경우
            val db = database(applicationContext)
//            allowMainThreadQueries() // 그냥 강제로 실행

            if( name==null || (mail1==null && mail2==null  && mail3==null ) || name.equals("") ||  (mail1=="" && mail2==""  && mail3=="" ) ){
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.alert_popup, null)
                view.findViewById<TextView>(R.id.alertTV).setText("이름 또는 이메일이 비어있습니다. \n 연락처가 저장되지 않습니다.")
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setPositiveButton("확인"){dialog, which->
                        setResult(0,i)
                        finish()
                    }
                    .setNeutralButton("취소",null)
                    .create()
                alertDialog.setView(view)
                alertDialog.show()
            }else{
                db.contactDao().insert(newContact)
                db.mailfolderDao().insert(
                    orderedMailFolders(name, newContact.toString())
                )
                val mailfolderIndex:Int = db.mailfolderDao().getMailfolderCount()-1//new given index

                Log.i("meow",mailfolderIndex.toString())
                Log.i("meow",newContact.toString())
                if(db.gmailDao().getGmailCount()!=0){
                    if(mail1.isNotBlank()){
                        val addressMatcingMails = db.gmailDao().getAddressMatchingMailsOrderedByTime(mail1)
                        Log.i("meow",mail1.toString())
                        Log.i("meow",addressMatcingMails.toString())
                        addressMatcingMails.forEach {
                            Log.i("meow",it.nId.toString() +  mailfolderIndex.toString())

                            db.gmailDao().updateMailBynId(it.nId, mailfolderIndex)
                        }
                    }
                    if(mail2.isNotBlank()){
                        val addressMatcingMails = db.gmailDao().getAddressMatchingMailsOrderedByTime(mail2)
                        Log.i("meow",addressMatcingMails.toString())
                        addressMatcingMails.forEach {
                            db.gmailDao().updateMailBynId(it.nId, mailfolderIndex)
                        }
                    }
                    if(mail3.isNotBlank()){
                        val addressMatcingMails = db.gmailDao().getAddressMatchingMailsOrderedByTime(mail3)
                        Log.i("meow",addressMatcingMails.toString())
                        addressMatcingMails.forEach {
                            db.gmailDao().updateMailBynId(it.nId, mailfolderIndex)
                        }
                    }
                }
                if(db.outlookDao().getOutlookCount()!=0){
                    if(mail1.isNotBlank()){
                        val addressMatcingMails = db.outlookDao().getAddressMatchingMailsOrderedByTime(mail1)
                        Log.i("meow",addressMatcingMails.toString())
                        addressMatcingMails.forEach {
                            db.outlookDao().updateMailBynId(it.nId, mailfolderIndex)
                        }
                    }
                    if(mail2.isNotBlank()){
                        val addressMatcingMails = db.outlookDao().getAddressMatchingMailsOrderedByTime(mail2)
                        Log.i("meow",addressMatcingMails.toString())
                        addressMatcingMails.forEach {
                            db.outlookDao().updateMailBynId(it.nId, mailfolderIndex)
                        }

                    }
                    if(mail3.isNotBlank()){

                        val addressMatcingMails = db.outlookDao().getAddressMatchingMailsOrderedByTime(mail3)
                        Log.i("meow",addressMatcingMails.toString())
                        addressMatcingMails.forEach {
                            db.outlookDao().updateMailBynId(it.nId, mailfolderIndex)
                        }

                    }
                }

                setResult(1,i)
                finish()

            }
//            for (jun in uhm) {
//            }

            // 싱글톤 패턴을 사용한 경우
//            val db = ContactDatabase.getInstance(applicationContext)
//            CoroutineScope(Dispatchers.IO).launch { // 다른애 한테 일 시키기
//                db!!.contactDao().insert(newContact)
//            Log.i("meow","oooooooooooooooooooooooooooooooooooooooooooo")
//                Log.w("meow",newContact.toString())
//            }
            var uhm = db!!.contactDao().getAll()
            Log.i("meow",uhm.toString())

//            val binding2 = FragmentContactBinding.inflate(layoutInflater)
//            listData = db!!.contactDao().getAll().toMutableList()
//            binding2!!.contactRcv.adapter = ContactAdapter(listData)
//            binding2!!.contactRcv.layoutManager= LinearLayoutManager(this)


        }
        binding.cancelbtn.setOnClickListener {


            setResult(0,i)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        findViewById<RecyclerView>(R.id.contactRcv).adapter?.notifyItemInserted(listData.size)
    }

}