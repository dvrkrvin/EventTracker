package com.bignerdranch.android.eventfinder.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketMasterResponse(
    @Json(name = "_embedded")
    val embedded: Embedded
)
