package com.kajileten.myapplication.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.kajileten.myapplication.util.ExtensionFunctions.hide
import com.kajileten.myapplication.util.ExtensionFunctions.show

@BindingAdapter("handleNetworkConnection", "handleRecyclerView", requireAll = true)
fun TextInputLayout.handleNetworkConnection(networkAvailable : Boolean, recyclerView: RecyclerView){
    if(!networkAvailable){
        this.isErrorEnabled = true
        this.error = "No Internet Connection"
        recyclerView.hide()
    }else{
        this.isErrorEnabled = false
        this.error = null
        recyclerView.show()
    }
}