package com.rujul.calendaractivity.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
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
        if (Util.isValidSlot(event, mList)) {
            var needToAdd = false
            for (calendarModel in mList) {
                val slotId = calendarModel.slotId!!
                val eventStartId = event.getStartTimeId()!! // 2300
                val eventEndId = event.getEndTimeId()!! // 0100

                if (eventEndId == slotId && needToAdd) {
                    needToAdd = false
                }

                if (eventStartId == slotId || needToAdd) {
                    calendarModel.event = event
                    needToAdd = true
                }
            }
            mAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(context, "Invalid event slot", Toast.LENGTH_LONG).show()
        }
    }

    override fun eventChange(sourceTag: String, targetTag: String) {

    }
}