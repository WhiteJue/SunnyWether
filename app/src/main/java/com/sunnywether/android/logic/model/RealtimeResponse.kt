package com.sunnywether.android.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result) {
    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float, val humidity: Float,
        @SerializedName("air_quality") val airQuality: AirQuality,
        @SerializedName("wind") val wind: Wind)

    data class AirQuality(val aqi: AQI)
    data class AQI(val chn: Float)
}