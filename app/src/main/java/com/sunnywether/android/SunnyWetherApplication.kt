package com.sunnywether.android

import android.app.Application
import android.content.Context

class SunnyWetherApplication : Application() {
    companion object {
        const val TOKEN = "sXGSMEYLSUqMaCXD"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}