package com.melon.mailmoajo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melon.mailmoajo.R
import com.melon.mailmoajo.dataclass.mailId
import entities.orderedMailFolders

fun MailFolderItemOnClickObject():
        MailFolderAdapter.OnClickInterface{
    val obj = object: MailFolderAdapter.OnClickInterface{
        override fun onClick(view: View, position: Int) {
            //실행코드
        }
    }
    val itemClickInterface : MailFolderAdapter.OnClickInterface = obj
    return itemClickInterface
}
class MailFolderAdapter (val d:MutableList<orderedMailFolders>, onClick:OnClickInterface):RecyclerView.Adapter<MailFolderAdapter.ViewHolder>(){

    interface OnClickInterface{
        fun onClick(view: View,position:Int)    //viewbinding 안했을 때
    }
    interface OnLongClickInterface{
        fun onLongClick(view: View,position:Int)    //viewbinding 안했을 때
    }

    var onClickInterface:OnClickInterface = onClick
//    var onLongClickInterface:OnLongClickInterface = onLongClick

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

        //OnClickListener
        v.itemView.setOnClickListener(View.OnClickListener {
            onClickInterface.onClick(it,p)

            Log.i("ererer", "onclick"+p.toString())
            Log.i("ererer", "onclick"+d[p].toString())
        })

    }
}