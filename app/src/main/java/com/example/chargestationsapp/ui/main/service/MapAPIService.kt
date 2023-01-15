package com.example.chargestationsapp.ui.main.service

import com.example.chargestationsapp.data.ChargingResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MapAPIService {
    private val BASE_URL ="https://api.openchargemap.io/v3/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MapAPI::class.java)

     fun getData(key: String,distanceunit: Int,countrycode: String,latitude: Double, longtitude: Double,distance:Double):Single<ChargingResponse>{
         return api.getChargeStations(key,distanceunit,countrycode,latitude,longtitude,distance)
    }
}