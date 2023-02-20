package com.kajileten.myapplication.ui.addgeofence

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.kajileten.myapplication.BuildConfig
import com.kajileten.myapplication.R
import com.kajileten.myapplication.databinding.FragmentStep1Binding
import com.kajileten.myapplication.viewmodels.SharedViewModel
import com.kajileten.myapplication.viewmodels.Step1ViewModel
import kotlinx.coroutines.launch
import java.util.Properties


class Step1Fragment : Fragment() {

    private val step1Fragment = "Step1Fragment"

    private var _binding : FragmentStep1Binding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val step1ViewModel : Step1ViewModel by viewModels()

    private lateinit var geoCoder : Geocoder
    private lateinit var placesClient : PlacesClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())
        geoCoder = Geocoder(requireContext())

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStep1Binding.inflate(layoutInflater, container, false)
        binding.sharedViewModel = sharedViewModel
        binding.step1ViewModel = step1ViewModel
        binding.lifecycleOwner = this

        binding.step1Back.setOnClickListener {
            onStep1BackClicked()
        }

        getCountryCodeFromCurrentLocation()


        return binding.root
    }

    private fun onStep1BackClicked() {
        findNavController().navigate(R.id.action_step1Fragment_to_mapsFragment )
    }

    @SuppressLint("MissingPermission")
    private fun getCountryCodeFromCurrentLocation() {
        lifecycleScope.launch{
            val placeFields = listOf(Place.Field.LAT_LNG)
            val request : FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val response = task.result
                    val latLng = response.placeLikelihoods[0].place.latLng!!
                    val address = geoCoder.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                    )
                    sharedViewModel.geoCountryCode = address!![0].countryCode
                    Log.d(step1Fragment, sharedViewModel.geoCountryCode)
                    enableNextButton()
                }else{
                    val exception = task.exception
                    if(exception is ApiException){
                        Log.e(step1Fragment, exception.statusCode.toString())
                    }
                    enableNextButton()
                }
            }
        }
    }

    private fun enableNextButton(){
        if(sharedViewModel.geoName.isNotEmpty()){
            step1ViewModel.enableNextButton(true)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}