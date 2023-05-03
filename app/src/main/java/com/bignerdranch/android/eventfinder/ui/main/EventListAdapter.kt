package com.bignerdranch.android.eventfinder.ui.main

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bignerdranch.android.eventfinder.api.EventItem
import com.bignerdranch.android.eventfinder.databinding.ListItemEventBinding

private const val TAG = "EventListAdapter"

class EventViewHolder(
    private val binding: ListItemEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    // Bind event data members to respective views here
    fun bind(eventItem: EventItem, onItemClicked: (Uri) -> Unit) {

        binding.eventName.text = eventItem.name
        binding.eventImage.load(eventItem.images[0].url)
        binding.eventDate.text = eventItem.dates.startDate.localDate

        binding.root.setOnClickListener {
            val uri = Uri.parse(eventItem.url)
            onItemClicked(uri)
        }
    }
}

class EventListAdapter(
    private val eventItems: List<EventItem>,
    private val onItemClicked: (Uri) -> Unit
) : RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemEventBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventItem = eventItems[position]
        holder.bind(eventItem, onItemClicked)
    }

    override fun getItemCount(): Int = eventItems.size

}