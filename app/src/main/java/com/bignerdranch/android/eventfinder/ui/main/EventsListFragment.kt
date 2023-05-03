package com.bignerdranch.android.eventfinder.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.eventfinder.R
import com.bignerdranch.android.eventfinder.databinding.FragmentEventsListBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "EventsListFragment"

class EventsListFragment : Fragment() {
    private var _binding: FragmentEventsListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val eventsListViewModel: EventsListViewModel by viewModels()

    companion object {
        fun newInstance() = EventsListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.fragment_event_list_bar, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        lifecycleScope.launch {
            while (!getUserLocationPermission()) {
                getLastKnownLocation()
                delay(500)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //region Location Retrieval
    private val LOCATION_PERMISSION_REQUEST_CODE = 111
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geoHash: String

    // Get user location, convert to geohash, and save to SharedPreferences
    private fun getUserLocationPermission() : Boolean{
        Log.d(TAG, "getUserLocationPermission() called")
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted, request it
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return false
        } else {
            // Permission already granted, get the last known location
            getLastKnownLocation()
            return true
        }
    }

    private fun getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation() called")
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.d(TAG, "getLocationPermission: Location: $location")

            val locationAsGeohash = location?.let { eventsListViewModel.convertToGeohash(it.latitude, location.longitude) }
            Log.d(TAG, "getLocationPermission: Location as geohash: $locationAsGeohash")
            eventsListViewModel.setRecyclerViewContent()

            if (locationAsGeohash != null) {
                eventsListViewModel.saveGeohashToSharedPreferences(requireActivity(), locationAsGeohash)
            }
        }
        getSetEventItems()
    }

    private fun getSetEventItems() {
        geoHash = eventsListViewModel.getGeohashFromSharedPreferences(requireContext()) ?: ""
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsListViewModel.eventItems.collect { eventItems ->
                    binding.eventRecyclerView.adapter = EventListAdapter(eventItems) {
                            eventPageUri ->
                        findNavController().navigate(
                            EventsListFragmentDirections.showEvent(
                                eventPageUri
                            )
                        )
                    }
                }
            }
        }
    }
    //endregion

}