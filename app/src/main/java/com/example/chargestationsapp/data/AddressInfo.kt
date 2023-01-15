package com.example.chargestationsapp.data

data class AddressInfo(
    val AccessComments: String,
    val AddressLine1: String,
    val AddressLine2: String,
    val ContactEmail: String,
    val ContactTelephone1: String,
    val ContactTelephone2: String,
    val Country: Country,
    val CountryID: Int,
    val Distance: Double,
    val DistanceUnit: Int,
    val ID: Int,
    val Latitude: Double,
    val Longitude: Double,
    val Postcode: String,
    val RelatedURL: String,
    val StateOrProvince: String,
    val Title: String,
    val Town: String
)