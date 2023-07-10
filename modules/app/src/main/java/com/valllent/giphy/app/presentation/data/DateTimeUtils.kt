package com.valllent.giphy.app.presentation.data

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateTimeUtils {

    fun format(serverDate: String): String {
        try {
            val time = LocalDateTime.parse(serverDate.replace(" ", "T"))
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            return time.format(formatter)
        } catch (parsingError: Throwable) {
            Log.e("DateTimeUtils", "Can't parse time: $serverDate")
        }

        return serverDate
    }

}