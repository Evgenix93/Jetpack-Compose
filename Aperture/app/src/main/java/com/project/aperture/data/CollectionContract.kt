package com.project.aperture.data

object CollectionContract {
    const val TABLE_NAME = "collections"

    object Columns {
        const val COLLECTION_ID = "collection_id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val TOTAL_PHOTOS = "total_photos"
        const val COVER_PHOTO_PATH = "cover_photo_uri"
        const val AVATAR_BYTE_ARRAY = "avatar_uri"
        const val NAME = "name"
        const val USER_NAME = "user_name"
    }

}