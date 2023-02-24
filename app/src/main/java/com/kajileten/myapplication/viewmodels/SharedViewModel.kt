package com.kajileten.myapplication.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.kajileten.myapplication.broadcastreceiver.GeofenceBroadcastReceiver
import com.kajileten.myapplication.data.DataStoreRepository
import com.kajileten.myapplication.data.GeofenceEntity
import com.kajileten.myapplication.data.GeofenceRepository
import com.kajileten.myapplication.util.Permissions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class SharedViewModel @Inject constructor(
    application : Application,
    private val dataStoreRepository: DataStoreRepository,
    private val geofenceRepository: GeofenceRepository
) : AndroidViewModel(application) {

    val app = application
    private var geofencingClient = LocationServices.getGeofencingClient(app.applicationContext)

    var geoId: Long = 0L
    var geoName: String = "Default"
    var geoCountryCode: String = ""
    var geoLocationName: String = "Search a City"
    var geoLatLng: LatLng = LatLng(0.0, 0.0)
    var geoRadius: Float = 500f
    var geoSnapshot: Bitmap? = null

    var geoCitySelected = false
    var geofenceReady = false
    var geofencePrepared = false

    //DataStore
    val readFirstLaunch = dataStoreRepository.readFirstLaunch.asLiveData()

    fun saveFirstLaunch(firstLaunch : Boolean) =
        viewModelScope.launch (Dispatchers.IO){
            dataStoreRepository.saveFirstLaunch(firstLaunch)
        }

    // Database

    val readGeofences = geofenceRepository.readGeofences.asLiveData()

    fun addGeofence(geofenceEntity: GeofenceEntity) =
        viewModelScope.launch (Dispatchers.IO){
            geofenceRepository.addGeofence(geofenceEntity)
        }

    fun removeGeofence(geofenceEntity: GeofenceEntity) =
        viewModelScope.launch (Dispatchers.IO){
            geofenceRepository.removeGeofence(geofenceEntity)
        }

    private fun setPendingIntent(geoId: Int) : PendingIntent{
        val intent = Intent(app, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            app,
            geoId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("MissingPermission")
    fun startGeofence(
        latitude: Double,
        longitude: Double
    ){
        if(Permissions.hasBackgroundLocationPermission(app)){
            val geofence = Geofence.Builder()
                .setRequestId(geoId.toString())
                .setCircularRegion(
                    latitude,
                    longitude,
                    geoRadius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(
                    Geofence.GEOFENCE_TRANSITION_ENTER
                            or Geofence.GEOFENCE_TRANSITION_EXIT
                            or Geofence.GEOFENCE_TRANSITION_DWELL
                )
                .setLoiteringDelay(5000)
                .build()
            val geofencingRequest = GeofencingRequest.Builder()
                .setInitialTrigger(
                    GeofencingRequest.INITIAL_TRIGGER_ENTER
                            or GeofencingRequest.INITIAL_TRIGGER_DWELL
                            or GeofencingRequest.INITIAL_TRIGGER_DWELL
                )
                .addGeofence(geofence)
                .build()

            geofencingClient.addGeofences(geofencingRequest, setPendingIntent(geoId.toInt())).run {
                addOnSuccessListener {
                    Log.d("Geofence", "Successfully added.")
                }
                addOnFailureListener{
                    Log.d("Geofence", it.message.toString())
                }
            }
        }else{
            Log.d("Geofence", "Permission not granted.")
        }
    }

    fun getBounds(center: LatLng, radius: Float): LatLngBounds{
        val distanceFromCenterToCorner = radius * sqrt(2.0)
        val soutWestCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0)
        val northEastCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0)
        return LatLngBounds(soutWestCorner, northEastCorner)
    }

    fun addGeofenceToDatabase(location: LatLng){
        val geofenceEntity = GeofenceEntity(
            geoId,
            geoName,
            geoLocationName,
            location.latitude,
            location.longitude,
            geoRadius,
            geoSnapshot!!
        )
        addGeofence(geofenceEntity)
    }

    fun checkDeviceLocationSettings(context: Context) : Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isLocationEnabled
        }else{
            val mode : Int = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

}