package com.melon.mailmoajo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.melon.mailmoajo.R
import com.melon.mailmoajo.dataclass.optionItems

//https://kumgo1d.tistory.com/entry/Android-Custom-ListView-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BB%A4%EC%8A%A4%ED%85%80-%EB%A6%AC%EC%8A%A4%ED%8A%B8%EB%B7%B0-%EB%A7%8C%EB%93%A4%EA%B8%B0

class OptionListItemAdapter : BaseAdapter() {

    private var listViewItemList = ArrayList<optionItems>()

    override fun getCount(): Int {
        return listViewItemList.size
    }

    override fun getItem(position: Int): Any {
        return listViewItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView
        val context = parent!!.context

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.option_list_item, parent, false)
        }

        val item_title : TextView = view!!.findViewById(R.id.option_item_tv_title)
        val item_subtitle : TextView = view.findViewById(R.id.option_item_tv_subtitle)

        val listViewItem = listViewItemList[position]

        // 아이템에 데이터 반영
        item_title.text = listViewItem.title
        item_subtitle.text = listViewItem.subtitle

        return view
    }

    fun addItem(title: String, subtitle: String) {

        val item = optionItems(title,subtitle)
        listViewItemList.add(item)

    }

}