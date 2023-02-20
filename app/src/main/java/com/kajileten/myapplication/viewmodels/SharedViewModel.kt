package com.kajileten.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kajileten.myapplication.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    application : Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    val app = application

    var geoId = 0L
    var geoName = "Default"
    var geoCountryCode = ""

    //DataStore
    val readFirstLaunch = dataStoreRepository.readFirstLaunch.asLiveData()

    fun saveFirstLaunch(firstLaunch : Boolean) =
        viewModelScope.launch (Dispatchers.IO){
            dataStoreRepository.saveFirstLaunch(firstLaunch)
        }


}