package com.example.chargestationsapp.ui.main.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chargestationsapp.data.ChargingResponse
import com.example.chargestationsapp.ui.main.service.MapAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MapViewModel : ViewModel() {

    private val MapAPIService = MapAPIService()
    private val disposable = CompositeDisposable()

    var stationList = MutableLiveData<ChargingResponse>()
    val ErrorAPI = MutableLiveData<Boolean>()
    val MapLoading = MutableLiveData<Boolean>()

    fun getFromDataAPI(
        key: String,
        distanceunit: Int,
        countrycode: String,
        latitude: Double,
        longtitude: Double,
        distance: Double
    ) {
        disposable.add(
            MapAPIService.getData(key, distanceunit, countrycode, latitude, longtitude, distance)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChargingResponse>() {
                    override fun onSuccess(t: ChargingResponse) {
                        stationList.value = t
                    }

                    override fun onError(e: Throwable) {
                        error("Network error")
                    }

                })
        )
    }


}



