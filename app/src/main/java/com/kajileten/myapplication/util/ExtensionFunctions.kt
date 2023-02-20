package com.kajileten.myapplication.util

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

object ExtensionFunctions {

    fun View.show(){
        this.visibility = View.VISIBLE
    }

    fun View.hide(){
        this.visibility = View.GONE
    }

    fun<T> LiveData<T>.observeOnce(lifecycleOwner : LifecycleOwner, observer : Observer<T>){
        observe(lifecycleOwner, object : Observer<T>{
            override fun onChanged(t: T) {
               observer.onChanged(t)
               removeObserver(this)
            }

        })
    }


    fun Long.test(){
        Log.d("asef", "VAY ANASINI")
    }

}