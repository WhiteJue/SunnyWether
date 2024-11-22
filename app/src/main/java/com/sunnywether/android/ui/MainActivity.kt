package com.sunnywether.android.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sunnywether.android.R
import com.sunnywether.android.logic.Repository
import com.sunnywether.android.ui.weather.WeatherActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Repository.isPlaceSaved()) {
            val place = Repository.getPlace()
            val intent = Intent(this, WeatherActivity::class.java).apply {
                putExtra(WeatherActivity.KEY_LNG, place.location.lng)
                putExtra(WeatherActivity.KEY_LAT, place.location.lat)
                putExtra(WeatherActivity.KEY_PLACE, place.name)
            }
            Repository.savePlace(place)
            startActivity(intent)
            finish()
        }
    }
}