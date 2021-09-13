package com.project.aperture.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.aperture.R
import com.project.aperture.databinding.ActivityMainBinding
import com.project.aperture.utils.AppStatus

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    fun showBottomNavigation(show: Boolean){
        binding.bottomNavigationView.isVisible = show
    }

    override fun onStop() {
        super.onStop()
        AppStatus.isStopped = true
    }
    
}