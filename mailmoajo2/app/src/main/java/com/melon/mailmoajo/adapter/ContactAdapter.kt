package com.melon.mailmoajo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import com.melon.mailmoajo.R
import com.melon.mailmoajo.contactlistData
import com.melon.mailmoajo.fragment.ContactDetailFragment
import com.melon.mailmoajo.fragment.ContactFragment
import com.melon.mailmoajo.fragment.MailFolderFragment
import entities.contacts
fun ContactItemOnClick():ContactAdapter.OnClickInterface{

    val obj = object: ContactAdapter.OnClickInterface{
        override fun onClick(view: View, position: Int) {
            //실행코드
            val name = contactlistData[position].name
            val activity = view!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.nav_host, ContactDetailFragment(position)).addToBackStack(null).commit()
            val toolbarBodyTemplate = activity.findViewById<Toolbar>(R.id.toolbar)
            toolbarBodyTemplate.title= name

            activity.setSupportActionBar(toolbarBodyTemplate)
        }
    }
    val itemClickInterface : ContactAdapter.OnClickInterface = obj


    return itemClickInterface
}
class ContactAdapter (val d:MutableList<contacts>, onClick: ContactAdapter.OnClickInterface): RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    interface OnClickInterface{
        fun onClick(view: View,position:Int)    //viewbinding 안했을 때
    }
    var onClickInterface: ContactAdapter.OnClickInterface = onClick
    class ViewHolder(var v: View): RecyclerView.ViewHolder(v){
        fun bind(item: contacts){
//            v.findViewById<ImageView>(R.id.imgimg1)?.setImageResource(item.img)
//            v.findViewById<TextView>(R.id.namename1)?.text=(item.name)

            v.findViewById<TextView>(R.id.contactNameTV)?.text=(item.name)
        }
    }

    override fun getItemCount():Int{
        return d.count()
    }

    override fun onCreateViewHolder(p: ViewGroup, type:Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.contact_view_holder, null))
    }

    override fun onBindViewHolder(v: ViewHolder, p:Int){
        v.bind(d[p])

        //OnClickListener
        v.itemView.setOnClickListener(View.OnClickListener {
            onClickInterface.onClick(it,p)

        })

    }
}