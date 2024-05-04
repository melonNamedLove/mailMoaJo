package com.melon.mailmoajo.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melon.mailmoajo.AddContactActivity
import com.melon.mailmoajo.ContactAdapter
import com.melon.mailmoajo.DaAdapter
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.GoogleSignInActivity
//import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.R
import com.melon.mailmoajo.contact
import com.melon.mailmoajo.contactName
import com.melon.mailmoajo.databinding.FragmentContactBinding
import com.melon.mailmoajo.databinding.FragmentMailBinding
import com.melon.mailmoajo.listData

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
        binding.addContactbtn.setOnClickListener{
            var i: Intent = Intent(context, AddContactActivity::class.java)
            startActivity(i)
        }
        binding!!.contactRcv.adapter = ContactAdapter(listData)
        binding!!.contactRcv.layoutManager= LinearLayoutManager(context)


        return binding.root
    }

}