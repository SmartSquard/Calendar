package com.rujul.calendaractivity.util

import androidx.annotation.Nullable
import com.rujul.calendaractivity.model.CalendarModel
import com.rujul.calendaractivity.model.Event
import com.rujul.calendaractivity.util.Constant.MINUTES_INTERVAL
import com.rujul.calendaractivity.util.Constant.MINUTES_INTERVAL_SLOT
import com.rujul.calendaractivity.util.Constant.YAXIS_TIME_FORMAT
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

object Util {

    fun getDayList(): ArrayList<CalendarModel> {
        val calendarList = arrayListOf<CalendarModel>()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val totalSlots = 24 * (60 / MINUTES_INTERVAL_SLOT)

        for (data in 0 until totalSlots) {
            val time = DateFormatter.getFormattedString(calendar.time, YAXIS_TIME_FORMAT)
            val minutes = calendar.get(Calendar.MINUTE)
            var showDivider = false
            if (minutes == 0 || minutes == MINUTES_INTERVAL) {
                showDivider = true
            }
            val calendarModel = CalendarModel(time, getId(calendar), showDivider)
            calendarList.add(calendarModel)
            calendar.add(Calendar.MINUTE, MINUTES_INTERVAL_SLOT)
        }
        return calendarList
    }

    fun getId(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return getId(calendar)
    }

    fun getId(calendar: Calendar): String {
        val numberFormat: NumberFormat = DecimalFormat("0000")
        val hour = (calendar.get(Calendar.HOUR_OF_DAY))
        val minutes = (calendar.get(Calendar.MINUTE))
        val result = (hour * 100) + minutes
        return numberFormat.format(result)
    }

    fun isValidSlot(event: Event) {
        val isValid = true
        val eventStartTime = event.getStartTimeId()
        val eventEndTime = event.getEndTimeId()

        val daysMap = getDaysMap();
        val startCalendarModel = daysMap.get(eventStartTime)
        for (calendarModel in daysMap.entries) {
            calendarModel.key
        }
//        val calendar = getDaysMap().get(eventStartTime)
//        for (calendarModel in getDayList()){
//            val startTimeId = calendarModel.event.getStartTimeId()
//            val endTimeId = calendarModel.event.getStartTimeId()
////            if(event.getStartTimeId() == event)
//        }
    }

    fun getDaysMap(): HashMap<String, CalendarModel> {
        val calendarMap = HashMap<String, CalendarModel>()
        for (calendarModel in getDayList()) {
            calendarMap.put(calendarModel.slotId!!, calendarModel)
        }
        return calendarMap
    }

    @Nullable
    public fun getEventFromSlotId(slotId: String): CalendarModel? {
        return getDaysMap()[slotId]
    }

    public fun updateEvent(startSlotId: String, numberOfSlots: Int, event: Event) : Event {

        val startHours = startSlotId.substring(0,2).toInt()
        val startMinutes = startSlotId.substring(2,4).toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, startHours)
        calendar.set(Calendar.MINUTE, startMinutes)

        event.startTime = calendar.time

        val minutesNeedsAdd = MINUTES_INTERVAL_SLOT * numberOfSlots
        calendar.add(Calendar.MINUTE, minutesNeedsAdd)

        event.endTime = calendar.time
        return event
    }



}