package com.project.testapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.project.testapp.databinding.UserListFragmentBinding


class UserListFragment : Fragment(R.layout.user_list_fragment) {
    private val viewModel: UserListViewModel by viewModels()
    private var usersAdapter: UsersAdapter by AutoClearedValue()
    private val binding: UserListFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserList()
        subscribe()
    }

    private fun initUserList() {
        usersAdapter = UsersAdapter(requireContext()) {
            findNavController().navigate(
                UserListFragmentDirections.actionUserListFragmentToUserFragment(
                    it.firstName,
                    it.lastName,
                    it.email,
                    it.avatar
                )
            )
        }
        with(binding.userListRecyclerView) {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun subscribe() {
        viewModel.getUsers().subscribe({
            usersAdapter.updateList(it)
            viewModel.saveUsersBD(it).subscribe()
        }, {
            viewModel.getUsersBD()
            Snackbar.make(binding.root, "Ошибка загрузки", Snackbar.LENGTH_LONG).show()
        })

        viewModel.getUsersBD().subscribe({
            usersAdapter.updateList(it)
        }, {

        })
    }
}