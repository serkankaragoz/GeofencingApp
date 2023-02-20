package com.kajileten.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Step2ViewModel : ViewModel() {
    private val _nextButtonEnabled = MutableLiveData(false)
    val nextButtonEnabled : LiveData<Boolean> get() = _nextButtonEnabled

    private val _internetAvailable = MutableLiveData(true)
    val internetAvailable : LiveData<Boolean> get() = _internetAvailable

    fun enableNextButton(boolean: Boolean){
        _nextButtonEnabled.value = boolean
    }

    fun setInternetAvailable(online : Boolean){
        _internetAvailable.value = online
    }

}