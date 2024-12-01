package com.sunnywether.android.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnywether.android.logic.Repository
import com.sunnywether.android.logic.model.Location
import com.sunnywether.android.logic.model.Weather

class WeatherViewModel : ViewModel() {
    companion object {
        const val DEFAULT_DAY_STEPS = 3
    }
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng : String = ""
    var locationLat : String = ""
    var placeName : String = ""

    val weatherLiveData : LiveData<Result<Weather>> = locationLiveData.switchMap { location ->
        Repository.getWeather(location.lng, location.lat, DEFAULT_DAY_STEPS)
    }

    fun searchWeather(location: Location) {
        locationLiveData.value = location
    }
}