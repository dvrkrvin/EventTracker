package com.bignerdranch.android.eventfinder.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDates(
    @Json(name = "start") val startDate: StartDate
)
