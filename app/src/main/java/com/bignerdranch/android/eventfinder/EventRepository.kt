package com.bignerdranch.android.eventfinder

import com.bignerdranch.android.eventfinder.api.EventItem
import com.bignerdranch.android.eventfinder.api.TicketMasterApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class EventRepository {
    private val ticketMasterApi: TicketMasterApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://app.ticketmaster.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        ticketMasterApi = retrofit.create()
    }

    suspend fun fetchEvents(geoHash : String): List<EventItem> = ticketMasterApi.fetchEvents(geoHash).embedded.events
}