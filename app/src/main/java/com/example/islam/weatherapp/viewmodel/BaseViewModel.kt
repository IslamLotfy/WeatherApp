package com.example.islam.weatherapp.viewmodel

import android.arch.lifecycle.ViewModel
import com.example.islam.weatherapp.model.network.WeatherAPIModule

abstract class BaseViewModel:ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(WeatherAPIModule)
            .build()

    init {
        inject()
    }


    private fun inject() {
        when (this) {
            is WeatherViewModel -> injector.inject(this)
        }
    }
}