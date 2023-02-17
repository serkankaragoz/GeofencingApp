package com.kajileten.myapplication.ui.permission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kajileten.myapplication.R
import com.kajileten.myapplication.databinding.FragmentPermissionBinding
import com.kajileten.myapplication.util.ExtensionFunctions.observeOnce
import com.kajileten.myapplication.util.Permissions
import com.kajileten.myapplication.viewmodels.SharedViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragment : Fragment(), EasyPermissions.PermissionCallbacks {


    private val permissionFragment : String = "PermissionFragment"

    private var _binding : FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : SharedViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPermissionBinding.inflate(layoutInflater, container, false)

        binding.continueButton.setOnClickListener {
            if(Permissions.hasLocationPermission(requireContext())){
                checkFirstLaunch()
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Location permission is required for working the app. Please enable location from settings.", Toast.LENGTH_LONG).show()

            Permissions.requestLocationPermission(this)

        }

        return  binding.root
    }

    private fun checkFirstLaunch() {
        sharedViewModel.readFirstLaunch.observeOnce(viewLifecycleOwner, { firstLaunch ->
            if(firstLaunch){
                findNavController().navigate(R.id.action_permissionFragment_to_add_geofence_graph)
                sharedViewModel.saveFirstLaunch(false)
            }else{
                findNavController().navigate(R.id.action_permissionFragment_to_mapsFragment)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionDenied(this, perms[0])){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            Permissions.requestLocationPermission(this)

        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(), "Permission granted! Tap on 'Continue' button to proceed.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}