package com.skillbox.homework32_5

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis

class Fragment2: Fragment(R.layout.fragment2) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300.toLong()
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300.toLong()
        }
    }

}