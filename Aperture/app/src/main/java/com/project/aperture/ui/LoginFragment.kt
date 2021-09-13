package com.project.aperture.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.aperture.R
import com.project.aperture.databinding.LoginFragmentBinding
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest

class LoginFragment: Fragment(R.layout.login_fragment) {

    private val viewModel: AuthViewModel by viewModels()
    private val binding: LoginFragmentBinding by viewBinding()
    private val launcher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.data != null) {
            val authResponse = AuthorizationResponse.fromIntent(it.data!!)
            val exception = AuthorizationException.fromIntent(it.data)
            when {
                authResponse != null && exception == null ->
                    viewModel.onAuthCodeReceived(
                        TokenRequest.Builder(
                            authResponse.request.configuration,
                            authResponse.request.clientId
                        )
                            .setGrantType(GrantTypeValues.AUTHORIZATION_CODE)
                            .setRedirectUri(authResponse.request.redirectUri)
                            .setAuthorizationCode(authResponse.authorizationCode)
                            .setNonce(authResponse.request.nonce)
                            .build()
                    )
                exception != null ->
                    viewModel.onAuthCodeFailed(exception)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.loginButton.setOnClickListener { viewModel.openLoginPage() }
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
        viewModel.openAuthPageLiveData.observe(viewLifecycleOwner, ::openAuthPage)
        viewModel.toastLiveData.observe(viewLifecycleOwner){Toast.makeText(requireContext(), "Fail", Toast.LENGTH_LONG).show()}
        viewModel.authSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToPhotosFragment())
        }
    }

    private fun updateIsLoading(isLoading: Boolean) {
        binding.loginButton.isVisible = !isLoading
    }

    private fun openAuthPage(intent: Intent) {
        launcher.launch(intent)
    }
}