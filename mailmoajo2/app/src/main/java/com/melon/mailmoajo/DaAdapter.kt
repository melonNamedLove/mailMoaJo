package com.melon.mailmoajo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class DaAdapter (val d:MutableList<mailId>):RecyclerView.Adapter<DaAdapter.ViewHolder>(){
    class ViewHolder(var v: View):RecyclerView.ViewHolder(v){
        fun bind(item:mailId){
//            v.findViewById<ImageView>(R.id.imgimg1)?.setImageResource(item.img)
//            v.findViewById<TextView>(R.id.namename1)?.text=(item.name)

            v.findViewById<TextView>(R.id.mailTitleTV)?.text=(item.muteMail).toString()
        }
    }

    override fun getItemCount():Int{
        return d.count()
    }

    override fun onCreateViewHolder(p: ViewGroup, type:Int):DaAdapter.ViewHolder{
        return ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.mail_view_holder, null))
    }

    override fun onBindViewHolder(v:DaAdapter.ViewHolder, p:Int){
        v.bind(d[p])

    }
}
class ContactAdapter (val d:MutableList<contact>):RecyclerView.Adapter<ContactAdapter.ViewHolder>(){
    class ViewHolder(var v: View):RecyclerView.ViewHolder(v){
        fun bind(item:contact){
//            v.findViewById<ImageView>(R.id.imgimg1)?.setImageResource(item.img)
//            v.findViewById<TextView>(R.id.namename1)?.text=(item.name)

            v.findViewById<TextView>(R.id.contactNameTV)?.text=(item.name)
        }
    }

    override fun getItemCount():Int{
        return d.count()
    }

    override fun onCreateViewHolder(p: ViewGroup, type:Int):ContactAdapter.ViewHolder{
        return ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.contact_view_holder, null))
    }

    override fun onBindViewHolder(v:ContactAdapter.ViewHolder, p:Int){
        v.bind(d[p])

    }
}