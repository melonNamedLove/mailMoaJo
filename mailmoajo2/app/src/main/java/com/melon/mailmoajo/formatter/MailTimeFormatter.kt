package com.melon.mailmoajo.formatter

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MailTimeFormatter {
    fun extractDateTime(input: String): String? {
        val isoRegex = """\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z""".toRegex()
        val gmailTZRegex = """\b\w{3},\s\d{1,2}\s\w{3}\s\d{4}\s\d{2}:\d{2}:\d{2}\s-\d{4}\b""".toRegex()

        val isoMatch = isoRegex.find(input)
        val gmailTZMatch = gmailTZRegex.find(input)

        return isoMatch?.value ?: gmailTZMatch?.value
    }

    fun convertToLocaleTimeAndFormat(dateTime: String): String  {
        val OFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val Gformatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)

        val zonedDateTime = try {
            ZonedDateTime.parse(dateTime, OFormatter)
        } catch (e: Exception) {
            ZonedDateTime.parse(dateTime, Gformatter)
        }

        val localZoneId = ZoneId.systemDefault()
        val localZonedDateTime = zonedDateTime.withZoneSameInstant(localZoneId)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return localZonedDateTime.format(outputFormatter)
    }
}