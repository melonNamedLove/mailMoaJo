package com.melon.mailmoajo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melon.mailmoajo.R
import com.melon.mailmoajo.dataclass.mailId
import entities.orderedMailFolders

class MailFolderAdapter (val d:MutableList<orderedMailFolders>):RecyclerView.Adapter<MailFolderAdapter.ViewHolder>(){
    class ViewHolder(var v: View):RecyclerView.ViewHolder(v){
        fun bind(item:orderedMailFolders){
//            v.findViewById<ImageView>(R.id.imgimg1)?.setImageResource(item.img)
//            v.findViewById<TextView>(R.id.namename1)?.text=(item.name)

            v.findViewById<TextView>(R.id.mailFolderTV)?.text=(item.name).toString()
        }
    }

    override fun getItemCount():Int{
        return d.count()
    }

    override fun onCreateViewHolder(p: ViewGroup, type:Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.mailfolder_view_holder, null))
    }

    override fun onBindViewHolder(v: ViewHolder, p:Int){
        v.bind(d[p])

    }
}