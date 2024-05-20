package com.melon.mailmoajo.fragment

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.melon.mailmoajo.R
import com.melon.mailmoajo.adapter.ContactAdapter
import com.melon.mailmoajo.adapter.ContactItemOnClick
import com.melon.mailmoajo.adapter.MailFolderAdapter
//import com.melon.mailmoajo.GoogleSignInActivity.Companion.contactprefs
import com.melon.mailmoajo.databinding.FragmentContactBinding
import com.melon.mailmoajo.contactlistData
import com.melon.mailmoajo.databinding.FragmentContactDetailBinding

class ContactDetailFragment( position: Int) : Fragment() {
    val position = position
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentContactDetailBinding.inflate(layoutInflater)



        val activity = this.context as AppCompatActivity
        val toolbarBodyTemplate = activity.findViewById<Toolbar>(R.id.toolbar)
//        toolbarBodyTemplate.title= contactlistData[position].name
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.setSupportActionBar(toolbarBodyTemplate)
        toolbarBodyTemplate.title= contactlistData[position].name
        toolbarBodyTemplate.setNavigationOnClickListener(View.OnClickListener(){
            Log.d("ererer", "onclickreturn")
            activity.supportFragmentManager.beginTransaction().replace(R.id.nav_host, ContactFragment()).commit()
        })


//            allowMainThreadQueries() // 그냥 강제로 실행
//
//        binding!!.contactRcv.adapter!!.notifyItemChanged(listData.size)

        binding.contactnameTV.setText(contactlistData[position].name)
        binding.contactgmailTV.setText(contactlistData[position].gmail)
        binding.contactoutlookTV.setText(contactlistData[position].outlook)


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