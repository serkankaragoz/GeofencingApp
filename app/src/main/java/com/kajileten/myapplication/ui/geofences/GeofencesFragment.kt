package com.kajileten.myapplication.ui.geofences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kajileten.myapplication.R
import com.kajileten.myapplication.adapters.GeofencesAdapter
import com.kajileten.myapplication.databinding.FragmentGeofencesBinding
import com.kajileten.myapplication.viewmodels.SharedViewModel


class GeofencesFragment : Fragment() {

    private var _binding: FragmentGeofencesBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val geofencesAdapter by lazy { GeofencesAdapter(sharedViewModel)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGeofencesBinding.inflate(inflater, container, false)
        binding.sharedViewModel = sharedViewModel

        setupToolbar()
        setupRecyclerView()
        observeDatabase()

        return binding.root
    }



    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observeDatabase() {
        sharedViewModel.readGeofences.observe(viewLifecycleOwner, {
            geofencesAdapter.setData(it)
            binding.geofencesRecyclerView.scheduleLayoutAnimation()
        })
    }

    private fun setupRecyclerView() {
        binding.geofencesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.geofencesRecyclerView.adapter = geofencesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}