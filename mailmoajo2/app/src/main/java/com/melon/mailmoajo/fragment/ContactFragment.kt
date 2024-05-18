package com.melon.mailmoajo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melon.mailmoajo.adapter.ContactAdapter
//import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.FragmentContactBinding
import com.melon.mailmoajo.contactlistData

class ContactFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentContactBinding.inflate(layoutInflater)

        binding.contactResetbtn.setOnClickListener {
//            contactprefs.edit().remove("contact").commit()
        }



        binding!!.contactRcv.adapter = ContactAdapter(contactlistData)
        binding!!.contactRcv.layoutManager= LinearLayoutManager(context)


//            allowMainThreadQueries() // 그냥 강제로 실행
//
//        binding!!.contactRcv.adapter!!.notifyItemChanged(listData.size)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

}