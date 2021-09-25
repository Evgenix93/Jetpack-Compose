package com.project.testapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.testapp.databinding.UserFragmentBinding

class UserFragment : Fragment(R.layout.user_fragment) {
    private val args: UserFragmentArgs by navArgs()
    private val binding: UserFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() {
        binding.nameTextView.text = getString(R.string.name, args.firstName, args.lastName)
        binding.emailTextView.text = getString(R.string.email, args.email)

        Glide.with(binding.root)
            .load(args.avatar)
            .into(binding.avatarImageView)
    }
}