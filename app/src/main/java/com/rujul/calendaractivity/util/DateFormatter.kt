package com.rujul.calendaractivity.util

import java.text.SimpleDateFormat
import java.util.*

public class DateFormatter {

    companion object{
        fun getFormattedString(time: Date, format: String): String {
            val simpleDateFormatter = SimpleDateFormat(format, Locale.US)
            return simpleDateFormatter.format(time)
        }
    }


}