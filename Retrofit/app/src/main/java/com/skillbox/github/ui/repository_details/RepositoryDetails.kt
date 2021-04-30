package com.skillbox.github.ui.repository_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.skillbox.github.R
import kotlinx.android.synthetic.main.repository_details.*

class RepositoryDetails: Fragment(R.layout.repository_details) {
    private val viewModel: RepositoryDetailsViewModel by viewModels()
    private val args: RepositoryDetailsArgs by navArgs()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailsTextView.text = """
            ID: ${args.repository.id}
            Name: ${args.repository.name}
            Owner: ${args.repository.owner.login}
        """.trimIndent()

        starCheckBox.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                Log.d("Test", "Put star")
                viewModel.putStar(args.repository.owner.login, args.repository.name)
            }else {
                Log.d("Test", "Remove star")

                viewModel.removeStar(args.repository.owner.login, args.repository.name)

            }
        }
        viewModel.checkStar(args.repository.owner.login, args.repository.name)

        viewModel.isChecked.observe(viewLifecycleOwner) {isChecked ->
            starCheckBox.isChecked = isChecked
            Log.d("Test", isChecked.toString())
        }
    }


}