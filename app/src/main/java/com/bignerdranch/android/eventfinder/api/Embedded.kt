package com.bignerdranch.android.eventfinder.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Embedded(
    val events: List<EventItem>
)