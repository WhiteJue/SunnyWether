package com.sunnywether.android.ui.weather

import android.content.Context
import android.graphics.Color
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunnywether.android.R
import com.sunnywether.android.databinding.ActivityWeatherBinding
import com.sunnywether.android.logic.model.Location
import com.sunnywether.android.logic.model.Weather
import com.sunnywether.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    companion object {
        const val KEY_LNG = "location_lng"
        const val KEY_LAT = "location_lat"
        const val KEY_PLACE = "place_name"
    }
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private lateinit var binding : ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decor = window.decorView
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra(KEY_LNG) ?: ""
        }
        if(viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra(KEY_LAT) ?: ""
        }
        if(viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra(KEY_PLACE) ?: ""
        }
        viewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if(weather == null) {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            } else {
                showWeatherInfo(weather)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        binding.swipeRefreshLayout.setOnRefreshListener { refreshWeather() }

        binding.realtime.navButton.setOnClickListener {
            binding.placeFragment.findViewById<EditText>(R.id.searchPlaceEdit).text.clear()
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })
        refreshWeather()
    }

    fun refreshWeather() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.searchWeather(Location(viewModel.locationLng, viewModel.locationLat))
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.realtime.placeName.text = viewModel.placeName
        binding.realtime.currentTemp.text = "${weather.realtime.temperature.toInt()} °C"
        binding.realtime.currentAQI.text = "空气指数 ${weather.realtime.airQuality.aqi.chn.toInt()}"
        binding.realtime.currentHumidity.text = "相对湿度 ${(weather.realtime.humidity * 100).toInt()}%"
        getSky(weather.realtime.skycon).also {
            binding.realtime.currentSky.text = it.info
            binding.weatherLayout.setBackgroundResource(it.bg)
        }

        binding.daily.dailyLayout.removeAllViews()
        for(i in 0 until weather.daily.skycon.size) {
            val view = LayoutInflater.from(this).inflate(R.layout.daily_item, binding.daily.dailyLayout, false)
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val tempInfo = view.findViewById<TextView>(R.id.tempInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skycon = weather.daily.skycon[i]
            val temp = weather.daily.temperature[i]
            dateInfo.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(skycon.date)
            tempInfo.text = "${temp.min.toInt()} ~ ${temp.max.toInt()} °C"
            getSky(skycon.value).also {
                skyInfo.text = it.info
                skyIcon.setImageResource(it.icon)
            }
            binding.daily.dailyLayout.addView(view)
        }

        val lifeIndex = weather.daily.lifeIndex
        binding.lifeIndex.also {
            it.coldRiskText.text = lifeIndex.coldRisk[0].desc
            it.dressingText.text = lifeIndex.dressing[0].desc
            it.carWashingText.text = lifeIndex.carWashing[0].desc
            it.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        }
        binding.weatherLayout.visibility = View.VISIBLE

    }
}