package com.melon.mailmoajo.dataclass

/**
 * 옵션 리스트뷰에 들어갈 커스텀 리스트용 데이터 클래스
 *
 * @property title : 두꺼운 글씨의 메인 제목
 * @property subtitle : 얇고 작은 글씨의 서브 제목
 */
data class optionItems(
    var title : String,
    var subtitle : String,
) {
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + subtitle.hashCode()
        return result
    }
}
