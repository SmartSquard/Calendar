package com.rujul.calendaractivity.view

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rujul.calendaractivity.base.BaseBindingAdapter
import com.rujul.calendaractivity.base.BaseBindingViewHolder
import com.rujul.calendaractivity.databinding.ItemDayViewBinding
import com.rujul.calendaractivity.model.CalendarModel
import com.rujul.calendaractivity.model.Event
import com.rujul.calendaractivity.util.Util
import java.lang.reflect.Type

public class DayViewAdapter : BaseBindingAdapter<CalendarModel>(),
    EventDragHandler.EventDragListener {

    private var mContext: Context? = null
    private lateinit var map : HashMap<String,CalendarModel>
    private lateinit var duplicateItems : ArrayList<CalendarModel>
    private val mOnLogClickListener = View.OnLongClickListener {
        val view = it
        val data = ClipData.newPlainText("", "")
        val shadowBuilder = DragShadowBuilder(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(data, shadowBuilder, view, 0)
        } else {
            view.startDrag(data, shadowBuilder, view, 0)
        }
        return@OnLongClickListener true
    }

    override fun duplicateItems(items: ArrayList<CalendarModel>) {
        duplicateItems = arrayListOf<CalendarModel>()
        val itemsStr = Gson().toJson(items)
        val type = object : TypeToken<List<CalendarModel>>() {}.type
        duplicateItems = Gson().fromJson<ArrayList<CalendarModel>>(itemsStr, type)
    }

    override fun duplicateMaps(items: ArrayList<CalendarModel>) {
        map = hashMapOf()
        for(calendarModel in items){
            map[calendarModel.slotId] = calendarModel
        }
    }

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ItemDayViewBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = holder.binding as ItemDayViewBinding
        mContext = holder.binding.root.context
        val calendarModel = items[holder.adapterPosition]
        updateUI(binding, calendarModel)

        binding.eventName.tag = calendarModel.slotId
        binding.root.tag = calendarModel.slotId
        binding.eventName.tag = calendarModel.slotId

        binding.eventName.setOnLongClickListener(mOnLogClickListener)
        binding.eventName.setOnDragListener(EventDragHandler(this))
        binding.root.setOnDragListener(EventDragHandler(this))
    }

    private fun updateUI(binding: ItemDayViewBinding, calendarModel: CalendarModel) {
        binding.startTime.text = calendarModel.displayTime
        binding.topDivider.visibility =
            if (calendarModel.showDivider) View.VISIBLE else View.INVISIBLE
        binding.startTime.visibility =
            if (calendarModel.showDivider) View.VISIBLE else View.INVISIBLE

        if (calendarModel.event != null) {
            binding.eventName.visibility = View.VISIBLE
            binding.eventName.text = calendarModel.event?.name
            calendarModel.event?.color?.let { binding.eventName.setBackgroundColor(it) }
        } else {
            binding.eventName.visibility = View.GONE
        }
    }

    override fun eventChange(sourceTag: String, targetTag: String) {
        val calendarModel = getCalendarModel(sourceTag) ?: return
        val eventString = Gson().toJson(calendarModel.event)
        val oldEvent = Gson().fromJson<Event>(eventString, Event::class.java)
        // TODO: 18/10/20 validation of whether any event is already schedule at that time or not
        moveAndUpdateEvent(oldEvent, targetTag)
    }

    private fun getCalendarModel(sourceTag: String): CalendarModel? {
        return map.get(sourceTag)
    }

    private fun moveAndUpdateEvent(oldEvent: Event?, newStartSlotId: String) {
        var nextSlotIsBooked = false
        var numberOfSlots = 0
        val temo = map.get(oldEvent?.getStartTimeId())


        // remove old event from object
        for (calendarModel in duplicateItems) {

            if (oldEvent?.getEndTimeId().equals(calendarModel.slotId)) {
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
        addEvents(updateEvent, duplicateItems)
    }


    private fun addEvents(event: Event, customItems: ArrayList<CalendarModel>) {
        if (Util.isValidSlot(event, items)) {
            items = customItems

            var needToAdd = false
            for (calendarModel in items) {
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
            notifyDataSetChanged()
        } else {
            Toast.makeText(mContext, "Invalid Event", Toast.LENGTH_LONG).show()
            notifyDataSetChanged()
        }
    }

}