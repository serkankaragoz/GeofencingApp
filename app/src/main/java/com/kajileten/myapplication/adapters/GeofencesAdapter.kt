package com.kajileten.myapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kajileten.myapplication.data.GeofenceEntity
import com.kajileten.myapplication.databinding.GeofencesRowLayoutBinding
import com.kajileten.myapplication.ui.geofences.GeofencesFragmentDirections
import com.kajileten.myapplication.util.MyDiffUtil
import com.kajileten.myapplication.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

class GeofencesAdapter(private val sharedViewModel: SharedViewModel) : RecyclerView.Adapter<GeofencesAdapter.MyViewHolder>() {

    private var geofenceEntity = mutableListOf<GeofenceEntity>()

    class MyViewHolder(val binding: GeofencesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(geofenceEntity: GeofenceEntity) {
            binding.geofencesEntity = geofenceEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GeofencesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentGeofence = geofenceEntity[position]
        holder.bind(currentGeofence)

        holder.binding.deleteImageView.setOnClickListener {
            removeItem(holder, position)
        }

        holder.binding.snapshotImageView.setOnClickListener {
            val action =
                GeofencesFragmentDirections.actionGeofencesFragmentToMapsFragment(currentGeofence)
            holder.itemView.findNavController().navigate(action)
        }

    }

    private fun removeItem(holder: MyViewHolder, position: Int) {
        sharedViewModel.viewModelScope.launch {
            val geofenceStopped =
                sharedViewModel.stopGeofence(listOf(geofenceEntity[position].geoId))
            if(geofenceStopped){
                sharedViewModel.removeGeofence(geofenceEntity[position])
                showSnackBar(holder, geofenceEntity[position])
            }else{
                Log.d("GeofencesAdapter", "Geofence NOT REMOVED!")
            }
        }
    }

    private fun showSnackBar(
        holder: MyViewHolder,
        removedItem: GeofenceEntity
    ) {
        Snackbar.make(
            holder.itemView,
            "Removed " + removedItem.name,
            Snackbar.LENGTH_LONG
        ).setAction("UNDO"){
            undoRemoval(holder, removedItem)
        }.show()
    }

    private fun undoRemoval(holder: GeofencesAdapter.MyViewHolder, removedItem: GeofenceEntity) {
        sharedViewModel.addGeofence(removedItem)
        sharedViewModel.startGeofence(
            removedItem.latitude,
            removedItem.longitude
        )
    }

    override fun getItemCount(): Int {
        return geofenceEntity.size
    }

    fun setData(newGeofenceEntity: MutableList<GeofenceEntity>) {
        val geofenceDiffUtil = MyDiffUtil(geofenceEntity, newGeofenceEntity)
        val diffUtilResult = DiffUtil.calculateDiff(geofenceDiffUtil)
        geofenceEntity = newGeofenceEntity
        diffUtilResult.dispatchUpdatesTo(this)
    }

}