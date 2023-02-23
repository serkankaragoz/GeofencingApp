package com.kajileten.myapplication.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.slider.Slider
import com.kajileten.myapplication.R
import com.kajileten.myapplication.viewmodels.SharedViewModel

@BindingAdapter("updateSliderValueTextView", "getGeoRadius", requireAll = true)
fun Slider.updateSliderValue(textView : TextView, sharedViewModel: SharedViewModel){
    updateSliderValueTextView(sharedViewModel.geoRadius, textView)
    this.addOnChangeListener { _, value, _ ->
        sharedViewModel.geoRadius = value
        updateSliderValueTextView(sharedViewModel.geoRadius, textView)
    }
}

fun Slider.updateSliderValueTextView(geoRadius : Float, textView: TextView){

    if(geoRadius >= 1000f){
        val kilometers = geoRadius / 1000
        textView.text = context.getString(R.string.display_kilometers, kilometers.toString())
    }else{
        textView.text = context.getString(R.string.display_meters, geoRadius.toString())
    }
    this.value = geoRadius
}