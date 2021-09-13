package com.project.aperture.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.aperture.ui.InfoFragment
import com.project.aperture.ui.LoginFragment

class OnboardingAdapter(fragment: Fragment, private val strings: List<String>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return strings.size + 1
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 3)
            LoginFragment()
        else
            InfoFragment.newInstance(strings[position], position)
    }

}