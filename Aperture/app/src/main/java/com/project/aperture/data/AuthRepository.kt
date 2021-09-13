package com.project.aperture.data

import android.content.Context
import android.net.Uri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.TokenRequest

class AuthRepository(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("shared prefs", Context.MODE_PRIVATE)

    fun getAuthRequest(): AuthorizationRequest {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse(AuthConfig.AUTH_URI),
            Uri.parse(AuthConfig.TOKEN_URI)
        )

        val redirectUri = Uri.parse(AuthConfig.CALLBACK_URL)

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
        onComplete: (String) -> Unit,
        onError: () -> Unit
    ) {
        authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
            when {
                response != null -> {
                    val accessToken = response.accessToken.orEmpty()
                    Network.accessToken = accessToken
                    sharedPrefs.edit().putString(ACCESS_TOKEN, accessToken).apply()
                    onComplete(accessToken)
                }
                else -> {
                    onError()
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    fun getAccessToken(): String? {
       val accessToken = sharedPrefs.getString(ACCESS_TOKEN, null)
       if (accessToken != null)
           Network.accessToken = accessToken
       return accessToken
    }

    companion object {
        const val ACCESS_TOKEN = "access token"
    }
}