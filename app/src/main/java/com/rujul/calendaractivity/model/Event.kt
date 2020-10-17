package com.rujul.calendaractivity.model

import android.graphics.Color
import com.rujul.calendaractivity.util.Util
import java.util.*

public class Event {

    var name: String? = null
    var startTime: Date? = null
    var endTime: Date? = null
    var color: Int = Color.RED

    fun getStartTimeId(): String? {
        startTime?.let {
            return Util.getId(it)
        }
        return null
    }

    fun getEndTimeId(): String? {
        endTime?.let {
            return Util.getId(it)
        }
        return null
    }

}