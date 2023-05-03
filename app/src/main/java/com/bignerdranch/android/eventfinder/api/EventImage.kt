package com.bignerdranch.android.eventfinder.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventImage (
    val url: String
)
