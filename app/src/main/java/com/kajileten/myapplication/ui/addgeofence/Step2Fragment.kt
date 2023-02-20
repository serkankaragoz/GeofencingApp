package com.kajileten.myapplication.ui.addgeofence

import android.content.pm.PackageManager.Property
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.kajileten.myapplication.BuildConfig
import com.kajileten.myapplication.R
import com.kajileten.myapplication.adapters.PredictionsAdapter
import com.kajileten.myapplication.databinding.FragmentStep2Binding
import com.kajileten.myapplication.viewmodels.SharedViewModel
import kotlinx.coroutines.launch


class Step2Fragment : Fragment() {

    private val step2Fragment = "Step2Fragment"

    private var _binding : FragmentStep2Binding? = null
    private val binding get() = _binding!!

    private val predictionsAdapter by lazy { PredictionsAdapter()}

    private val sharedViewModel : SharedViewModel by activityViewModels()

    private lateinit var placesClient : PlacesClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentStep2Binding.inflate(layoutInflater, container, false)

        binding.predictionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.predictionsRecyclerView.adapter = predictionsAdapter
        
        binding.geofenceLocationEt.doOnTextChanged { text, _, _, _ ->  
            getPlaces(text)
        }

        binding.step2Back.setOnClickListener {
            findNavController().navigate(R.id.action_step2Fragment_to_step1Fragment)
        }

        binding.step2Next.setOnClickListener {
            findNavController().navigate(R.id.action_step2Fragment_to_step3Fragment)
        }

        return  binding.root
    }

    private fun getPlaces(text: CharSequence?) {
        if(sharedViewModel.checkDeviceLocationServices(requireContext())) {
            lifecycleScope.launch {
                if(text.isNullOrEmpty()){
                    predictionsAdapter.setData(emptyList())
                }else{
                    val token = AutocompleteSessionToken.newInstance()

                    val request =
                        FindAutocompletePredictionsRequest.builder()
                            .setCountries(sharedViewModel.geoCountryCode)
                            .setTypeFilter(TypeFilter.CITIES)
                            .setSessionToken(token)
                            .setQuery(text.toString())
                            .build()
                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->
                            //response.autocompletePredictions
                            predictionsAdapter.setData(response.autocompletePredictions)
                        }
                        .addOnFailureListener{ exception : Exception? ->
                            if(exception is ApiException){
                                Log.e(step2Fragment, exception.statusCode.toString())
                            }
                        }

                }
            }
        }else{
            Toast.makeText(
                requireContext(),
                "Please Enable Location Settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}