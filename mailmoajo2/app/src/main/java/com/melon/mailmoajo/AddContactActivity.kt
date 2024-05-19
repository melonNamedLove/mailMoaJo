package com.melon.mailmoajo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.melon.mailmoajo.Database.MailMoaJoDatabase
//import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.ActivityAddContactBinding
import entities.contacts
import entities.orderedMailFolders

class AddContactActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityAddContactBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var i: Intent = Intent(this, HomeActivity::class.java)

        binding.addbtn.setOnClickListener{
//            var contactprefset = contactprefs.getStringSet("contact", setOf<String>())

//            var contactset = contactprefset!!.toMutableSet()
            var name = ""
            var google = ""
            var outlook = ""

            if (!binding.nameET.text.toString().equals("")){
                name =binding.nameET.text.toString()
            }
            if (!binding.googleEmailET.text.toString().equals("")){
                google =binding.googleEmailET.text.toString()
            }
            if (!binding.outlookEmailET.text.toString().equals("")){
                outlook =binding.outlookEmailET.text.toString()
            }
//            val concatContactString = name+"**"+google+"**"+outlook
//            contactset!!.add(concatContactString)
//            contactprefs.edit().putStringSet("contact", contactset.toSet()).apply()


            var newContact = contacts(name, google, outlook)

//             싱글톤 패턴을 사용하지 않은 경우
            val db = Room.databaseBuilder(
                applicationContext,
                MailMoaJoDatabase::class.java,
                "mailmoajo-database"
            ).allowMainThreadQueries()
                .build()
//            allowMainThreadQueries() // 그냥 강제로 실행
//

            if( name==null || (google==null && outlook==null ) || name.equals("") || (google.equals("") && outlook.equals("") ) ){
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