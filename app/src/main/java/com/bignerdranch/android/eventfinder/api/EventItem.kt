package com.bignerdranch.android.eventfinder.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventItem (
    val id: String,
    val name: String,
    val url: String,
    val images: List<EventImage>,
    val dates: EventDates
    )