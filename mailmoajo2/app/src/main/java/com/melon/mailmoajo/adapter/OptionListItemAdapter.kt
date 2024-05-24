package com.melon.mailmoajo.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.R
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import com.melon.mailmoajo.dataclass.optionItems
import com.melon.mailmoajo.fragment.SettingsFragment
import com.melon.mailmoajo.gmailLoadBtn
import javax.security.auth.callback.Callback

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

        //레이아웃 연결 (setonCLickListener 구현용
        val item_layout : View = view!!.findViewById(R.id.itemLayout)

        //레이아웃 내부의 로고, 타이틀, 서브타이틀 연결
        val item_logo : ImageView = view!!.findViewById(R.id.option_item_iv_logo)
        val item_title : TextView = view!!.findViewById(R.id.option_item_tv_title)
        val item_subtitle : TextView = view.findViewById(R.id.option_item_tv_subtitle)

        val listViewItem = listViewItemList[position]

        // 아이템에 데이터 반영
        item_title.text = listViewItem.title
        item_subtitle.text = listViewItem.subtitle
        item_logo.setImageResource(listViewItem.logoid)

        // 받아온 code에 따라 layout에 설정되는 클릭리스너가 달라집니다. 0번은 테스트용 로그입니다.
        when (listViewItem.code){
            0 -> {
                item_layout.setOnClickListener {
                    Log.wtf("from adapter","when code is 0")
                }
            }
            1 -> {
                item_layout.setOnClickListener {
                    gmailLoadBtn().gmailLoadFun(listViewItem.context)
                }
            }
            2 -> {
                item_layout.setOnClickListener {
                    gmailLoadBtn().retroFun()
                }
            }
            3 -> {
                item_layout.setOnClickListener {
                    gmailLoadBtn().retro2Fun()
                }
            }
            else -> {
                Log.wtf("!!!!!!!","maybe code param is wrong.")
            }
        } // when (listViewItem.code) END

        return view

    } // getView END

    /**
     * option_list_item.xml 형태의 커스텀 뷰를 띄웁니다.
     *
     * @param logoid 로고 아이디로, R.drawable에서 가져옵니다. Int 형태의 아이디입니다.
     *
     * @param title 주 제목으로, 굵은 글씨로 보여집니다. 현재로써는 10글자가 최대입니다.
     *
     * @param subtitle 부제목으로, 얇고 작은 글씨로 보여집니다. 20글자가 최대입니다.
     *
     * @param context 어댑터를 쓰는 곳에서 context를 받아옵니다.
     *
     * @param code 리스트마다 setOnCLickListener를 설정해줄때 when을 실행하기 위한 Int형 코드입니다. 함수들은 gmailLoadBtn.kt에 구현해두었습니다.
     */
    fun addItem(logoid : Int, title: String, subtitle: String, context: Context, code:Int) {
        // #####2 title이 10글자, subtitle이 20글자일시 끝 부분을 자르고 "..."으로 처리해야함
        val item = optionItems(logoid,title,subtitle,context, code)
        listViewItemList.add(item)

    }

}