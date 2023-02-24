package com.kajileten.myapplication.bindingadapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import coil.load
import com.kajileten.myapplication.R
import com.kajileten.myapplication.data.GeofenceEntity
import com.kajileten.myapplication.util.ExtensionFunctions.disable
import com.kajileten.myapplication.util.ExtensionFunctions.enable

@BindingAdapter("setVisibility")
fun View.setVisibility(data: List<GeofenceEntity>){
    if(data.isNullOrEmpty()){
        this.visibility = View.VISIBLE
    }else{
        this.visibility = View.INVISIBLE
    }
}

@BindingAdapter("handleMotionTransition")
fun MotionLayout.handleMotionTransition(deleteImageView: ImageView){
    deleteImageView.disable()
    this.setTransitionListener(object : MotionLayout.TransitionListener{
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
        override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
        override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}

        override fun onTransitionCompleted(motionLayout: MotionLayout?, transition: Int) {
            if(motionLayout != null && transition == R.id.start){
                deleteImageView.disable()
            }else if(motionLayout != null && transition == R.id.end){
                deleteImageView.enable()
            }
        }

    })
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(bitmap: Bitmap){
    this.load(bitmap)
}

@BindingAdapter("parseCoordinates")
fun TextView.parseCoordinates(value: Double){
    val coordinate = String.format("%.4f", value)
    this.text = coordinate
}