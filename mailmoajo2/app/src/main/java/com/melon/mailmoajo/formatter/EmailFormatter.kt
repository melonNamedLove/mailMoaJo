package com.melon.mailmoajo.formatter

class EmailFormatter {
    fun extractEmail(input: String): String {
        val regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return regex.find(input)!!.value
    }
}