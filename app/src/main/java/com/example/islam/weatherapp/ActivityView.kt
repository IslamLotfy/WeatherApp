package com.example.islam.weatherapp

import io.reactivex.Observable

interface ActivityView {
    fun takeImage(): Observable<String>
}