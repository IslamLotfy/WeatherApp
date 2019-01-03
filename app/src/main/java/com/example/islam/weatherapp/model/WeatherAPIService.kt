package com.example.islam.weatherapp.model

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("weather")
    fun getTwoDayWeather(@Query("lat") latitude: Double,
                         @Query("lon")longitude:Double,
                         @Query("units") unitType:String = "metric",
                         @Query("appid")id:String):Observable<DayWeatherAPIResponse>

    companion object {
        fun create(): WeatherAPIService{
            val retrofit= Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .build()
            return retrofit.create(WeatherAPIService::class.java)

        }
    }
}