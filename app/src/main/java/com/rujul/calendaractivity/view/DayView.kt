package com.rujul.calendaractivity.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rujul.calendaractivity.databinding.DayViewBinding
import com.rujul.calendaractivity.model.CalendarModel
import com.rujul.calendaractivity.model.Event
import com.rujul.calendaractivity.util.Util

class DayView : LinearLayoutCompat, EventDragHandler.EventDragListener {
    private lateinit var mAdapter: DayViewAdapter
    private var mList: ArrayList<CalendarModel> = arrayListOf()

    constructor(context: Context) : super(context) {
        if (!isInEditMode) {
            initView(context)
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) {
            initView(context, attrs)
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        if (!isInEditMode) {
            initView(context, attrs, defStyleAttr)
        }
    }


    private fun initView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = -1) {
        val binding = DayViewBinding.inflate(LayoutInflater.from(context), this, true)
        mList = Util.getDayList()

        mAdapter = DayViewAdapter()
        mAdapter.setItem(mList)

        binding.dayRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.dayRecyclerView.adapter = mAdapter
        binding.dayRecyclerView.setOnDragListener(EventDragHandler(this))
    }

    public fun addEvents(event: Event) {
        var needToAdd = false
        for (calendarModel in mList) {
            val slotId = calendarModel.slotId!!
            val eventStartId = event.getStartTimeId()!! // 2300
            val eventEndId = event.getEndTimeId()!! // 0100

//            if (slotId in (eventStartId) until eventEndId) {
//                calendarModel.event = event
//            }

            if (eventEndId == slotId && needToAdd) {
                needToAdd = false
            }

            if (eventStartId == slotId || needToAdd) {
                calendarModel.event = event
                needToAdd = true
            }

//            if (event.getEndTimeId() == calendarModel.slotId && needToAdd) {
//                needToAdd = false
//            }
//            // 1715 == 1730
//            if (event.getStartId() == calendarModel.slotId || needToAdd) {
//                calendarModel.event = event
//                needToAdd = true
//            }


        }
        mAdapter.notifyDataSetChanged()
    }

    override fun eventDragged(oldSlotId: String, newSlotId: String, event: Event) {

    }

    override fun eventChange(sourceTag: String, targetTag: String) {
        val calendarModel = Util.getEventFromSlotId(sourceTag)
        val eventString = Gson().toJson(calendarModel?.event)
        val oldEvent = Gson().fromJson<Event>(eventString, Event::class.java)
        // TODO: 18/10/20 validation of whether any event is already schedule at that time or not
        moveAndUpdateEvent(oldEvent, targetTag)
    }

    private fun moveAndUpdateEvent(oldEvent: Event?, newStartSlotId: String) {
        var nextSlotIsBooked = false
        var numberOfSlots = 0;
        // remove old event from object
        for (calendarModel in Util.getDayList()) {
            if (oldEvent?.getEndTimeId().equals(calendarModel.slotId)) {
                numberOfSlots += 1
                calendarModel.event = null
                nextSlotIsBooked = false
            }

            if (oldEvent?.getStartTimeId().equals(calendarModel.slotId) || nextSlotIsBooked) {
                numberOfSlots += 1
                calendarModel.event = null
                nextSlotIsBooked = true
            }
        }

        // update startTime and endTime of event
        val updateEvent = Util.updateEvent(newStartSlotId, numberOfSlots, oldEvent!!)
        addEvents(updateEvent)

    }

}