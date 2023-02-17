package com.kajileten.myapplication.ui.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kajileten.myapplication.R
import com.kajileten.myapplication.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding : FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentMapsBinding.inflate(layoutInflater, container, false)

        binding.floatingActionButton.setOnClickListener {

            findNavController().navigate(R.id.action_mapsFragment_to_add_geofence_graph)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap!!
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}