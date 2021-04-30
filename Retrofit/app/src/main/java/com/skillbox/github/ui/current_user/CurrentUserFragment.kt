package com.skillbox.github.ui.current_user

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.skillbox.github.R
import kotlinx.android.synthetic.main.current_user.*

class CurrentUserFragment: Fragment(R.layout.current_user) {
    private val viewModel: CurrentUserViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getCurrentUser()
        viewModel.currentUser.observe(viewLifecycleOwner) {user ->
            Glide.with(requireView())
                .load(user.avatar)
                .into(avatarImageView)
            infoTextView.text = """
                ID: ${user.id} 
                Name: ${user.login}
            """.trimIndent()
        }
    }
}