package com.melon.mailmoajo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melon.mailmoajo.R
import com.melon.mailmoajo.adapter.ContactAdapter
import com.melon.mailmoajo.adapter.MailAdapter
import com.melon.mailmoajo.adapter.MailFolderAdapter
import com.melon.mailmoajo.adapter.MailFolderItemOnClickObject
import com.melon.mailmoajo.contactlistData
import com.melon.mailmoajo.databinding.FragmentContactBinding
import com.melon.mailmoajo.databinding.FragmentMailFolderBinding
import com.melon.mailmoajo.mailfolderlistData

class MailFolderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMailFolderBinding.inflate(layoutInflater)



        val activity = this.context as AppCompatActivity
        val toolbarBodyTemplate = activity.findViewById<Toolbar>(R.id.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarBodyTemplate.title="메일함"
        activity.setSupportActionBar(toolbarBodyTemplate)
//        val itemLongClickInterface : MailFolderAdapter.OnLongClickInterface = obj2

        binding!!.mailFolderRCV.adapter = MailFolderAdapter(mailfolderlistData,MailFolderItemOnClickObject())
        binding!!.mailFolderRCV.layoutManager= LinearLayoutManager(context)


//            allowMainThreadQueries() // 그냥 강제로 실행
//
//        binding!!.contactRcv.adapter!!.notifyItemChanged(listData.size)
        return binding.root
//        return inflater.inflate(R.layout.fragment_mail_folder, container, false)
    }
}