package com.rujul.calendaractivity.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rujul.calendaractivity.R
import com.rujul.calendaractivity.model.Event
import com.rujul.calendaractivity.util.Util
import com.rujul.calendaractivity.view.DayView
import java.util.*

class DayViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_view)

        val dayView = findViewById<DayView>(R.id.day_view)
        val event = Event()

        var calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, 15)
        event.startTime = calendar.time

//        calendar.add(Calendar.HOUR_OF_DAY, 2)
        calendar.set(Calendar.MINUTE, 45)
        event.endTime = calendar.time
        event.name = "Event test"

        dayView.addEvents(event)

        calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, 30)
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        event.startTime = calendar.time
        calendar.add(Calendar.MINUTE, 60)
        event.endTime = calendar.time
        event.name = "Event 2"

        dayView.addEvents(event)


    }
}