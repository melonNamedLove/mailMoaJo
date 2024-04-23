package com.melon.mailmoajo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DaAdapter (val d:MutableList<ItemData>):RecyclerView.Adapter<DaAdapter.ViewHolder>(){
    class ViewHolder(var v: View):RecyclerView.ViewHolder(v){
        fun bind(item:ItemData){
//            v.findViewById<ImageView>(R.id.imgimg1)?.setImageResource(item.img)
//            v.findViewById<TextView>(R.id.namename1)?.text=(item.name)
            v.findViewById<TextView>(R.id.mailTitleTV)?.text=(item.mailID).toString()
        }
    }

    override fun getItemCount():Int{
        return d.count()
    }

    override fun onCreateViewHolder(p: ViewGroup, type:Int):DaAdapter.ViewHolder{
        return ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.view_holder, null))
    }

    override fun onBindViewHolder(v:DaAdapter.ViewHolder, p:Int){
        v.bind(d[p])

    }
}