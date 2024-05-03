package com.melon.mailmoajo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Contactables
import android.util.Log
import androidx.room.Room
import com.melon.mailmoajo.Database.ContactDatabase
import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.ActivityAddContactBinding
import com.melon.mailmoajo.fragment.ContactFragment
import entities.contacts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


            var newContact = contacts(name, google, outlook)

//             싱글톤 패턴을 사용하지 않은 경우
            val db = Room.databaseBuilder(
                applicationContext,
                ContactDatabase::class.java,
                "contact-database"
            ).allowMainThreadQueries()
                .build()
//            allowMainThreadQueries() // 그냥 강제로 실행
//
            db.contactDao().insert(newContact)
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
            finish()
        }

    }
}