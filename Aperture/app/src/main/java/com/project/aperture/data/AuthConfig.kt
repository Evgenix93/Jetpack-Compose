package com.project.aperture.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://unsplash.com/oauth/authorize"
    const val TOKEN_URI = "https://unsplash.com/oauth/token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE =
        "public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections"

    const val CLIENT_ID = "xE4uWvQJnCDYdeIJ9gKHwvAUN9MzhYCHDoz35mk2kx8"
    const val CLIENT_SECRET = "UkABV3_Dvx9w8mOYj_GOURAVVbvzl-UwKiMBWU4jZgs"
    const val CALLBACK_URL = "aperture://aperture.ru/callback"
}