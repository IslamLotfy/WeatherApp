package com.example.islam.weatherapp.model.network

import com.example.islam.weatherapp.model.dataclasses.DayWeatherAPIResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("weather")
    fun getTwoDayWeather(@Query("lat") latitude: Double,
                         @Query("lon")longitude:Double,
                         @Query("units") unitType:String = "metric",
                         @Query("appid")id:String):Single<DayWeatherAPIResponse>

}