package com.skillbox.github.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    const val CLIENT_ID = "072860f0bc7304f9b070"
    const val CLIENT_SECRET = "d6154ca1f9bf6ec823dfde5255276a9cc09536f6"
    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"
}