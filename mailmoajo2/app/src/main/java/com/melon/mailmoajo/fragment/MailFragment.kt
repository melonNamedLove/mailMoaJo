package com.melon.mailmoajo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.melon.mailmoajo.Database.MailMoaJoDatabase
import com.melon.mailmoajo.R
import com.melon.mailmoajo.adapter.MailAdapter
import com.melon.mailmoajo.contactlistData
import com.melon.mailmoajo.databinding.FragmentMailBinding
import com.melon.mailmoajo.mailfolderlistData

class MailFragment ( position: Int) : Fragment() {
    val position = position

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMailBinding.inflate(layoutInflater)


        val activity = this.context as AppCompatActivity


        val db = Room.databaseBuilder(
            activity.applicationContext,
            MailMoaJoDatabase::class.java,
            "mailmoajo-database"
        ).allowMainThreadQueries()
            .build()

//        var listData = mutableListOf<ItemData>()

//        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))
        val toolbarBodyTemplate = activity.findViewById<Toolbar>(R.id.toolbar)
//        toolbarBodyTemplate.title= contactlistData[position].name
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.setSupportActionBar(toolbarBodyTemplate)
        toolbarBodyTemplate.title= mailfolderlistData[position].name
        toolbarBodyTemplate.setNavigationOnClickListener(View.OnClickListener(){    //appbar 뒤로가기 버튼 눌렀을 때
//            Log.d("ererer", "onclickreturn")
//            activity.supportFragmentManager.beginTransaction().remove(this).addToBackStack(null).commit()
//            toolbarBodyTemplate.title="주소록"
//            fab.setIconResource(R.drawable.person_add_24dp_fill0_wght400_grad0_opsz24)
//            fab.setText("추가")
//            contactlistData = db!!.contactDao().getAll().toMutableList()
            activity.supportFragmentManager.popBackStack()
        })


        binding!!.mailRcv.adapter = MailAdapter(mailmail)
        binding!!.mailRcv.layoutManager= LinearLayoutManager(context)


        return binding.root
    }

}