package com.kajileten.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Step1ViewModel : ViewModel() {

    private val _nextButtonEnabled = MutableLiveData(false)

    val nextButtonEnabled : LiveData<Boolean> get() = _nextButtonEnabled

    fun enableNextButton(boolean: Boolean){
        _nextButtonEnabled.value = boolean
    }


}