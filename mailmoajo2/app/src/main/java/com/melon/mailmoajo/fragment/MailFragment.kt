package com.melon.mailmoajo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melon.mailmoajo.adapter.MailAdapter
import com.melon.mailmoajo.databinding.FragmentMailBinding

class MailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMailBinding.inflate(layoutInflater)


//        var listData = mutableListOf<ItemData>()

//        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))



        binding!!.mailRcv.adapter = MailAdapter(mailmail)
        binding!!.mailRcv.layoutManager= LinearLayoutManager(context)


        return binding.root
    }

}