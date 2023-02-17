package com.kajileten.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.kajileten.myapplication.R
import com.kajileten.myapplication.util.Permissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(Permissions.hasLocationPermission(this)){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_permissionFragment_to_mapsFragment)

            // This line caused IllegalStateException that's why I used the code above
            //findNavController(R.id.navHostFragment).navigate(R.id.action_permissionFragment_to_mapsFragment)
        }
    }
}