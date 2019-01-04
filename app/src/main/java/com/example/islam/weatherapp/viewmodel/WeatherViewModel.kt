package com.example.islam.weatherapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.example.islam.weatherapp.model.dataclasses.DayWeatherAPIResponse
import com.example.islam.weatherapp.model.network.WeatherAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherViewModel(private val latitude:Double,private val longitude:Double,private val id:String): BaseViewModel() {
    @Inject
    lateinit var weatherAPIService: WeatherAPIService
    private lateinit var subscription: Disposable
    private val mutableLiveData: MutableLiveData<DayWeatherAPIResponse> = MutableLiveData()
    init {
        loadWeahter()
    }

    private fun loadWeahter() {
        subscription = weatherAPIService.getTwoDayWeather(latitude=latitude,longitude = longitude,id = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { it->mutableLiveData.value=it },
                        {  }
                )
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getWeahter():MutableLiveData<DayWeatherAPIResponse>{
        return mutableLiveData
    }
}