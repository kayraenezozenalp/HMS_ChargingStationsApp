package com.example.chargestationsapp.data

import android.os.Parcelable
import com.huawei.hms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
@Parcelize
data class Detail(
    var nearestAddress: String?,
    var location: LatLng?,
    var phone: String?,
    var email: String?,
    var website: String?,
    var equipmentList: List<EquipmentItem>?):Parcelable
