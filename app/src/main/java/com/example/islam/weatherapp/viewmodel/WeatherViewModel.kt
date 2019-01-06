package com.example.islam.weatherapp.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.util.Log
import com.example.islam.weatherapp.model.dataclasses.DayWeatherAPIResponse
import com.example.islam.weatherapp.model.network.WeatherAPIService
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherViewModel(private val fusedLocationClient: FusedLocationProviderClient , private val weatherAPIAppId:String) : BaseViewModel() {
    @Inject
    lateinit var weatherAPIService: WeatherAPIService
    private lateinit var subscription: Disposable
    private val mutableLiveData: MutableLiveData<DayWeatherAPIResponse> = MutableLiveData()
    private var latitude:Double=0.0
    private var longitude:Double=0.0


    init {
      setLocationListener()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationListener(){
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    latitude = location?.latitude!!
                    longitude = location.longitude
                    loadWeahter()
                }
    }
    private fun loadWeahter() {
        subscription = weatherAPIService.getTwoDayWeather( latitude, longitude, id=weatherAPIAppId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { it -> mutableLiveData.value = it },
                        { it->Log.e("weather",it.stackTrace.toString())}
                )
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getWeather(): LiveData<DayWeatherAPIResponse> {
        return mutableLiveData
    }
}