package com.example.chargestationsapp.ui.main.service

import com.example.chargestationsapp.data.ChargingResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MapAPI {

     @GET("poi")
     fun getChargeStations(
        @Query("key") key: String,
        @Query("distanceunit") distanceunit: Int,
        @Query("countrycode") countrycode: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longtitude: Double,
        @Query("distance") distance: Double
    ): Single<ChargingResponse>

}