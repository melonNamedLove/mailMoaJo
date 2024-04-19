package com.melon.mailmoajo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.daelim.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : AppCompatActivity() {
    var binding: ActivityRecyclerViewBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var listData = mutableListOf<ItemData>()
        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))
        listData.add(ItemData(R.drawable.img2,"오승목","11111111111", 2))
        listData.add(ItemData(R.drawable.img3,"오항목","22222222222",3))
        listData.add(ItemData(R.drawable.img4,"오목목","33333333333",4))
        listData.add(ItemData(R.drawable.img5,"전동환","44444444444",5))
        listData.add(ItemData(R.drawable.img1,"김정민","55555555555",6))
        listData.add(ItemData(R.drawable.img2,"박준한","66666666666",7))
        listData.add(ItemData(R.drawable.img3,"이원도","77777777777",8))
        listData.add(ItemData(R.drawable.img4,"지현수","88888888888",9))
        listData.add(ItemData(R.drawable.img5,"신호진","99999999999",10))
        listData.add(ItemData(R.drawable.img1,"박관우","10101010101",11))
        listData.add(ItemData(R.drawable.img2,"박준한","66666666666",12))
        listData.add(ItemData(R.drawable.img3,"이원도","77777777777",13))
        listData.add(ItemData(R.drawable.img4,"지현수","88888888888",14))
        listData.add(ItemData(R.drawable.img5,"신호진","99999999999",15))
        listData.add(ItemData(R.drawable.img1,"정석현","01077585738",16))
        listData.add(ItemData(R.drawable.img2,"오승목","11111111111",17))
        listData.add(ItemData(R.drawable.img3,"오항목","22222222222",18))
        listData.add(ItemData(R.drawable.img4,"오목목","33333333333",19))
        listData.add(ItemData(R.drawable.img5,"전동환","44444444444",20))
        listData.add(ItemData(R.drawable.img1,"김정민","55555555555",21))
        listData.add(ItemData(R.drawable.img2,"박준한","66666666666",22))
        listData.add(ItemData(R.drawable.img3,"이원도","77777777777",23))
        listData.add(ItemData(R.drawable.img4,"지현수","88888888888",24))
        listData.add(ItemData(R.drawable.img5,"신호진","99999999999",25))
        listData.add(ItemData(R.drawable.img1,"박관우","10101010101",26))
        listData.add(ItemData(R.drawable.img2,"박준한","66666666666",27))
        listData.add(ItemData(R.drawable.img3,"이원도","77777777777",28))
        listData.add(ItemData(R.drawable.img4,"지현수","88888888888",29))
        listData.add(ItemData(R.drawable.img5,"신호진","99999999999",30))


        binding!!.rcv.adapter = DaAdapter(listData)
        binding!!.rcv.layoutManager= LinearLayoutManager(this)
    }
}