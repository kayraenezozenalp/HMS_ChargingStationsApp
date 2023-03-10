package com.example.chargestationsapp.ui.main.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import com.huawei.hms.site.api.model.Coordinate
import com.huawei.hms.support.api.entity.location.geocoder.Address

data class ChargeStations(
    @SerializedName("AccessComments")
    val accessComments: String? = null,
    @SerializedName("AddressLine1")
    val addressLine1: String? = null,
    @SerializedName("AddressLine2")
    val addressLine2: String? = null,
    @SerializedName("ContactEmail")
    val contactEmail: String? = null,
    @SerializedName("ContactTelephone1")
    val contactTelephone1: String? = null,
    @SerializedName("ContactTelephone2")
    val contactTelephone2: String? = null,
    @SerializedName("Country")
    val country: String? = null,
    @SerializedName("CountryID")
    val countryID: Int? = null,
    @SerializedName("Distance")
    val distance: Double? = null,
    @SerializedName("DistanceUnit")
    val distanceUnit: Int = 0,
    @SerializedName("ID")
    val iD: Int = 0,
    @SerializedName("Latitude")
    val latitude: Double = 0.0,
    @SerializedName("Longitude")
    val longitude: Double = 0.0,
    @SerializedName("Postcode")
    val postcode: String? = null,
    @SerializedName("RelatedURL")
    val relatedURL: String? = null,
    @SerializedName("StateOrProvince")
    val stateOrProvince: String? = null,
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Town")
    val town: String? = null
)