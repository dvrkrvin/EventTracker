package com.bignerdranch.android.eventfinder.ui.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ch.hsr.geohash.GeoHash
import com.bignerdranch.android.eventfinder.EventRepository
import com.bignerdranch.android.eventfinder.api.EventItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "EventsListViewModel"

class EventsListViewModel(application: Application) : AndroidViewModel(application) {
    private val eventRepository = EventRepository()

    private val _eventItems: MutableStateFlow<List<EventItem>> = MutableStateFlow(emptyList())
    val eventItems: StateFlow<List<EventItem>>
        get() = _eventItems.asStateFlow()

    fun getGeohashFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("geohash", null)
    }

    fun setRecyclerViewContent() {
        val geoHash = getGeohashFromSharedPreferences(getApplication<Application>().applicationContext) ?: ""
        if (geoHash.isNotEmpty()) {
            viewModelScope.launch {
                try {
//                delay(5000)
                    Log.d(TAG, "Fetching events for geohash: $geoHash")
                    val items = eventRepository.fetchEvents(geoHash)
//                val items = eventRepository.fetchEvents("dr5reg")
                    Log.d(TAG, "Items received: $items")
                    _eventItems.value = items
                } catch (ex: Exception) {
                    Log.e(TAG, "Failed to fetch event items", ex)
                }
            }
        } else {
            Log.d(TAG, "No geohash found in shared preferences")
        }
    }

    fun saveGeohashToSharedPreferences(context: Context, geohash: String) {
        Log.d(TAG, "saveGeohashToSharedPreferences() called")
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("geohash", geohash)
        editor.apply()
    }

    fun convertToGeohash(latitude: Double, longitude: Double): String {
        Log.d(TAG, "convertToGeohash() called")
        return GeoHash.withCharacterPrecision(latitude, longitude, 9).toBase32()
    }
}