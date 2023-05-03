package com.bignerdranch.android.eventfinder.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartDate(
    val localDate: String,
//    val localTime: String,
//    val dateTime: String
)
