package com.bignerdranch.android.eventfinder.api

import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "6nKWFEpNYfOxhJFVIPsvd4fwuPqEhlF3"

interface TicketMasterApi {

    @GET(
        "discovery/v2/events.json?" +
                "apikey=$API_KEY" +
                "&size=25"
//                "&geoPoint=dr"
    )
    suspend fun fetchEvents(@Query("geoPoint") geoHash : String): TicketMasterResponse
}
