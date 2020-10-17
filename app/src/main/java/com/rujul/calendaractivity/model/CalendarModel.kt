package com.rujul.calendaractivity.model

public class CalendarModel {
    var displayTime: String? = null
    var slotId: String? = null
    var event: Event? = null
    var showDivider: Boolean = true

    constructor(displayTime: String?, slotId: String, mainSlot: Boolean = true) {
        this.displayTime = displayTime
        this.slotId = slotId
        this.showDivider = mainSlot
    }



}