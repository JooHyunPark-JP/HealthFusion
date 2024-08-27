package com.example.healthfusion.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault())

    fun getCurrentFormattedDateTime(): String {
        return dateFormat.format(Date())
    }

    fun parseDateTimeToMillis(dateTime: String): Long? {
        return try {
            val date = dateFormat.parse(dateTime)
            date?.time
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}