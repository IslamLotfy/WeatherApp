package com.example.islam.weatherapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient

class WeatherViewModelFactory(private val fusedLocationClient: FusedLocationProviderClient,
                              private val weatherAPIAppId:String):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WeatherViewModel(fusedLocationClient,weatherAPIAppId) as T
    }
}