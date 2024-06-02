package com.melon.mailmoajo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.melon.mailmoajo.R
import com.melon.mailmoajo.contactlistData
import com.melon.mailmoajo.dataclass.mailId
import com.melon.mailmoajo.fragment.ContactDetailFragment
import com.melon.mailmoajo.fragment.MailFragment
import com.melon.mailmoajo.mailfolderlistData
import com.melon.mailmoajo.viewModel.MailViewModel
import entities.orderedMailFolders

fun MailFolderItemOnClickObject():
        MailFolderAdapter.OnClickInterface{
    val obj = object: MailFolderAdapter.OnClickInterface{
        override fun onClick(view: View, position: Int) {
            //실행코드
            val name = mailfolderlistData[position].name
            val activity = view!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.nav_host, MailFragment(position)).addToBackStack(null).commit()
            Log.d("meow",position.toString())
            val toolbarBodyTemplate = activity.findViewById<Toolbar>(R.id.toolbar)
            toolbarBodyTemplate.title= name

            activity.setSupportActionBar(toolbarBodyTemplate)
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