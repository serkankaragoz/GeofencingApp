package com.kajileten.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kajileten.myapplication.util.Constants.UNINITIALIZED_LONG

class GeofencesViewModel : ViewModel() {
    private val _switchEnabled = MutableLiveData(true)
    val switchEnabled : LiveData<Boolean> get() = _switchEnabled

    fun enableSwitch(boolean: Boolean){
        _switchEnabled.value = boolean
    }

    private val _enabledId = MutableLiveData(UNINITIALIZED_LONG)
    val enabledId : LiveData<Long> get() = _enabledId

    fun setEnabledId(enabledId: Long){
        _enabledId.value = enabledId
    }



}