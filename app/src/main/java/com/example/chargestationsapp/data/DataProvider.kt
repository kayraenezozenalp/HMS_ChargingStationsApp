package com.example.chargestationsapp.data

data class DataProvider(
    val Comments: Any,
    val DataProviderStatusType: DataProviderStatusType,
    val DateLastImported: String,
    val ID: Int,
    val IsApprovedImport: Boolean,
    val IsOpenDataLicensed: Boolean,
    val IsRestrictedEdit: Boolean,
    val License: String,
    val Title: String,
    val WebsiteURL: String
)