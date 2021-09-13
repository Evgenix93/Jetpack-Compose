package com.project.aperture.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.aperture.R
import com.project.aperture.databinding.StartFragmentBinding

class StartFragment: Fragment(R.layout.start_fragment) {
   private val binding: StartFragmentBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val strings = listOf(getString(R.string.startInfo1), getString(R.string.startInfo2), getString(
            R.string.startInfo3
        ))
        binding.viewPager.adapter = OnboardingAdapter(this, strings)
    }
}