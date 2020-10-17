package com.rujul.calendaractivity.view

import android.util.Log
import android.view.DragEvent
import android.view.View
import com.rujul.calendaractivity.model.Event

class EventDragHandler(val mEventDragListener: EventDragListener) : View.OnDragListener {

    override fun onDrag(targetView: View?, event: DragEvent?): Boolean {
        when (event?.action) {
            DragEvent.ACTION_DROP -> {
                targetView?.let {
                    val sourceView = event.localState as View
                    Log.d("target", sourceView.tag.toString())
                    Log.d("source", targetView.tag.toString())
                    mEventDragListener.eventChange(sourceView.tag.toString(), targetView.tag.toString())
                }
            }
        }
        return true
    }

    public interface EventDragListener {

        fun eventDragged(oldSlotId: String, newSlotId: String, event: Event)

        fun eventChange(sourceTag: String, targetTag: String)

    }
}