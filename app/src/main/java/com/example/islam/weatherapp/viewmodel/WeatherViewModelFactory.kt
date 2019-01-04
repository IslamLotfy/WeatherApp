package com.example.islam.weatherapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class WeatherViewModelFactory(private val latitude:Double, private val longitude:Double, private val id:String):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WeatherViewModel(latitude, longitude, this.id) as T
    }
}