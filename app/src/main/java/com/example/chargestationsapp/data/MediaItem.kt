package com.example.chargestationsapp.data

data class MediaItem(
    val ChargePointID: Int,
    val Comment: String,
    val DateCreated: String,
    val ID: Int,
    val IsEnabled: Boolean,
    val IsExternalResource: Boolean,
    val IsFeaturedItem: Boolean,
    val IsVideo: Boolean,
    val ItemThumbnailURL: String,
    val ItemURL: String,
    val MetadataValue: Any,
    val User: User
)