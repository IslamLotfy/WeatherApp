package com.example.islam.weatherapp.viewmodel

import com.example.islam.weatherapp.model.network.WeatherAPIModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(WeatherAPIModule::class)])
interface ViewModelInjector {
    fun inject(weatherViewModel: WeatherViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: WeatherAPIModule): Builder
    }
}