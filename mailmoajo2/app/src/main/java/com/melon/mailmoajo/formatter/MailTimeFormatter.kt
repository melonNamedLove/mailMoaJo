package com.melon.mailmoajo.formatter

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MailTimeFormatter {
    fun extractDateTime(input: String): String? {
        val regex = """\b\w{3},\s\d{1,2}\s\w{3}\s\d{4}\s\d{2}:\d{2}:\d{2}\s-\d{4}\b""".toRegex()
        val matchResult = regex.find(input)
        return matchResult?.value
    }

    fun convertToLocaleTime(pacificTime: String): ZonedDateTime {
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val pacificZonedDateTime = ZonedDateTime.parse(pacificTime, formatter)
        val localZoneId = ZoneId.systemDefault()
        return pacificZonedDateTime.withZoneSameInstant(localZoneId)
    }
}