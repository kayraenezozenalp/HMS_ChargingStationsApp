package com.example.chargestationsapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EquipmentItem(var amps: Int,
                         var voltage: Int,
                         var powerKw: Double,
                         var title: String):Parcelable
