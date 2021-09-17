package com.skillbox.github.ui.repository_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.github.R
import com.skillbox.github.ui.main.MainFragmentDirections
import kotlinx.android.synthetic.main.repository_list.*

class RepositoryListFragment : Fragment(R.layout.repository_list) {
    private var repositoryAdapter: RepositoryAdapter? = null
    private val viewModel: RepositoryListViewModel by viewModels()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        repositoryAdapter = RepositoryAdapter { position ->
            findNavController().navigate(
                RepositoryListFragmentDirections.actionRepositoryListFragmentToRepositoryDetails(
                    viewModel.repositoryList.value!![position]
                )
            )
        }
        with(recyclerView) {
            adapter = repositoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.getRepositoryList()
        viewModel.repositoryList.observe(viewLifecycleOwner) { list ->
            repositoryAdapter?.update(list)
            repositoryAdapter?.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repositoryAdapter = null
    }
}