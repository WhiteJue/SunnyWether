package com.sunnywether.android.logic.dao

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sunnywether.android.SunnyWetherApplication
import com.sunnywether.android.logic.model.Place

object PlaceDao {
    private const val SP_PLACE = "place"
    private const val KEY_PLACE = "place"

    private val sp : SharedPreferences get() = SunnyWetherApplication.context.getSharedPreferences(
        SP_PLACE, Context.MODE_PRIVATE)

    fun savePlace(place: Place) {
        sp.edit().putString(KEY_PLACE, Gson().toJson(place)).apply()
    }

    fun getPlace() : Place {
        return Gson().fromJson(sp.getString(KEY_PLACE, ""), Place::class.java)
    }

    fun containsPlace() : Boolean {
        return sp.contains(KEY_PLACE)
    }
}