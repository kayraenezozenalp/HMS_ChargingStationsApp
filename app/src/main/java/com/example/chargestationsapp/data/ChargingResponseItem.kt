package com.example.chargestationsapp.data

data class ChargingResponseItem(
    val AddressInfo: AddressInfo,
    val Connections: List<Connection>,
    val DataProvider: DataProvider,
    val DataProviderID: Int,
    val DataProvidersReference: String,
    val DataQualityLevel: Int,
    val DateCreated: String,
    val DateLastConfirmed: Any,
    val DateLastStatusUpdate: String,
    val DateLastVerified: String,
    val DatePlanned: Any,
    val GeneralComments: String,
    val ID: Int,
    val IsRecentlyVerified: Boolean,
    val MediaItems: List<MediaItem>,
    val MetadataValues: Any,
    val NumberOfPoints: Int,
    val OperatorID: Int,
    val OperatorInfo: OperatorInfo,
    val OperatorsReference: String,
    val ParentChargePointID: Any,
    val PercentageSimilarity: Any,
    val StatusType: StatusTypeX,
    val StatusTypeID: Int,
    val SubmissionStatus: SubmissionStatus,
    val SubmissionStatusTypeID: Int,
    val UUID: String,
    val UsageCost: String,
    val UsageType: UsageType,
    val UsageTypeID: Int,
    val UserComments: Any
)