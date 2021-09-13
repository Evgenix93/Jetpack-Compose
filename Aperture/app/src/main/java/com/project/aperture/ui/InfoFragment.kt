package com.project.aperture.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.aperture.R
import com.project.aperture.databinding.InfoFragmentBinding
import com.project.aperture.utils.withArguments

class InfoFragment : Fragment(R.layout.info_fragment) {
    private val binding: InfoFragmentBinding by viewBinding()

    companion object {
        private const val KEY_TEXT = "key text"
        private const val KEY_POSITION = "key position"
        fun newInstance(text: String, position: Int): InfoFragment = InfoFragment().withArguments {
            putString(
                KEY_TEXT, text
            )
            putInt(KEY_POSITION, position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startText.text = requireArguments().getString(KEY_TEXT)
        val position = requireArguments().getInt(KEY_POSITION)
        if (position == 0)
            binding.arrowBackImageView.isVisible = false
        if (position == 2)
            binding.arrowNextImageView.isVisible = false
    }
}