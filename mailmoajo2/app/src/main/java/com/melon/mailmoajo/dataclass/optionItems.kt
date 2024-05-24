package com.melon.mailmoajo.dataclass

import android.content.Context
import com.melon.mailmoajo.databinding.FragmentSettingsBinding
import javax.security.auth.callback.Callback

/**
 * 옵션 리스트뷰에 들어갈 커스텀 리스트용 데이터 클래스
 *
 * @property logoid drawable 내부의 id
 * @property title 두꺼운 글씨의 메인 제목
 * @property subtitle 얇고 작은 글씨의 서브 제목
 * @property context 쓰이는 곳에서의 context
 * @property code 함수 연결용 Int형 code (Adapter 참조)
 */
data class optionItems(
    var logoid : Int,
    var title : String,
    var subtitle : String,
    var context : Context,
    var code : Int,
) {
//    override fun hashCode(): Int {
//        var result = title.hashCode()
//        result = 31 * result + subtitle.hashCode()
//        return result
//    }
}
