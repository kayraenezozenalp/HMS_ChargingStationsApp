package com.example.chargestationsapp.data

data class UsageType(
    val ID: Int,
    val IsAccessKeyRequired: Boolean,
    val IsMembershipRequired: Boolean,
    val IsPayAtLocation: Boolean,
    val Title: String
)