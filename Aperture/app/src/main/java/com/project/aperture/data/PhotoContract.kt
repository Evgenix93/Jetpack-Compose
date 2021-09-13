package com.project.aperture.data

object PhotoContract {
    const val TABLE_NAME = "photos"

    object Columns {
        const val PHOTO_ID = "photo_id"
        const val COLLECTION_ID = "collection_id"
        const val LIKES = "likes"
        const val LIKED_BY_USER = "liked_by_user"
        const val NAME = "name"
        const val USER_NAME = "user_name"
        const val DESCRIPTION = "description"
        const val BIO = "bio"
        const val PHOTO_PATH = "image"
        const val AVATAR = "avatar"
    }
}