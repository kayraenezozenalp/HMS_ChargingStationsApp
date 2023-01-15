package com.example.chargestationsapp.data

data class Connection(
    var Amps: Int,
    val Comments: Any,
    val ConnectionType: ConnectionType,
    val ConnectionTypeID: Int,
    val CurrentType: CurrentType,
    val CurrentTypeID: Int,
    val ID: Int,
    val Level: Level,
    val LevelID: Int,
    val PowerKW: Double,
    val Quantity: Int,
    val Reference: String,
    val StatusType: StatusTypeX,
    val StatusTypeID: Int,
    val Voltage: Int
)