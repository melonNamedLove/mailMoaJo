package com.melon.mailmoajo.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.melon.mailmoajo.Database.MailMoaJoDatabase
import com.melon.mailmoajo.HomeActivity.Companion.database
import com.melon.mailmoajo.R
import com.melon.mailmoajo.adapter.ContactAdapter
import com.melon.mailmoajo.adapter.ContactItemOnClick
import com.melon.mailmoajo.adapter.MailFolderAdapter
//import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.FragmentContactBinding
import com.melon.mailmoajo.contactlistData
import com.melon.mailmoajo.databinding.FragmentContactDetailBinding
import entities.contacts
import entities.orderedMailFolders

class ContactDetailFragment( position: Int) : Fragment() {
    val position = position

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentContactDetailBinding.inflate(layoutInflater)

        val fab = requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fab)
        fab.setText("편집")
        fab.setIconResource(R.drawable.edit_square_24dp_fill0_wght400_grad0_opsz24)


        val activity = this.context as AppCompatActivity


        val db = database(activity)

        val toolbarBodyTemplate = activity.findViewById<Toolbar>(R.id.toolbar)
//        toolbarBodyTemplate.title= contactlistData[position].name
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.setSupportActionBar(toolbarBodyTemplate)
        toolbarBodyTemplate.title= contactlistData[position].name
        toolbarBodyTemplate.setNavigationOnClickListener(View.OnClickListener(){    //appbar 뒤로가기 버튼 눌렀을 때
//            Log.d("ererer", "onclickreturn")
//            activity.supportFragmentManager.beginTransaction().remove(this).addToBackStack(null).commit()
//            toolbarBodyTemplate.title="주소록"
//            fab.setIconResource(R.drawable.person_add_24dp_fill0_wght400_grad0_opsz24)
//            fab.setText("추가")
//            contactlistData = db!!.contactDao().getAll().toMutableList()
            activity.supportFragmentManager.popBackStack()
        })


//            allowMainThreadQueries() // 그냥 강제로 실행
//
//        binding!!.contactRcv.adapter!!.notifyItemChanged(listData.size)

        binding.contactnameTV.setText(contactlistData[position].name)
        binding.contactgmailTV.setText(contactlistData[position].mail_1)
        binding.contactoutlookTV.setText(contactlistData[position].mail_2)
        binding.contactoutlookTV.setText(contactlistData[position].mail_3)

        val nId = contactlistData[position].nId
        val delContact = contacts(
            contactlistData[position].name,
            contactlistData[position].mail_1,
            contactlistData[position].mail_2,
            contactlistData[position].mail_3
        )
        binding.deleteContactBtn.setOnClickListener{

            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.alert_popup, null)
            view.findViewById<TextView>(R.id.alertTV).setText("삭제하시겠습니까 \n 연락처가 삭제됩니다")
            val alertDialog = AlertDialog.Builder(activity)
                .setTitle("알림")
                .setNegativeButton("확인"){dialog, which->

                    db.contactDao().deleteUserByNId(nId)
                    db.mailfolderDao().deleteMailFolderByFolderId(
                        delContact.toString()
                    )
                    activity.supportFragmentManager.popBackStack()
                    toolbarBodyTemplate.title="주소록"
                    fab.setIconResource(R.drawable.person_add_24dp_fill0_wght400_grad0_opsz24)
                    fab.setText("추가")

                    contactlistData = db!!.contactDao().getAll().toMutableList()
                }
                .setPositiveButton("취소",null)
                .create()
            alertDialog.setView(view)
            alertDialog.show()
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPause() {
        super.onPause()

    }
}