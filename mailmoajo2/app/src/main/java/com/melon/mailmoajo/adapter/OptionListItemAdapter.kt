package com.melon.mailmoajo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.melon.mailmoajo.R
import com.melon.mailmoajo.dataclass.optionItems

//https://odomm.tistory.com/entry/Android-studio-Custom-Listview-%EB%A7%8C%EB%93%A4%EA%B8%B0

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

    /**
     * option_list_item.xml 형태의 커스텀 뷰를 띄웁니다.
     *
     * @param title 주 제목으로, 굵은 글씨로 보여집니다. 현재로써는 10글자가 최대입니다.
     * @param subtitle 부제목으로, 얇고 작은 글씨로 보여집니다. 20글자가 최대입니다.
     */
    fun addItem(title: String, subtitle: String) {
        // #####1 로고 사진 추가 기능도 추가해야함

        // #####2 title이 10글자, subtitle이 20글자일시 끝 부분을 자르고 "..."으로 처리해야함

        val item = optionItems(title,subtitle)
        listViewItemList.add(item)

    }

}