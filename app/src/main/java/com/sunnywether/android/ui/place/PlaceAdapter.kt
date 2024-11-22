package com.sunnywether.android.ui.place

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnywether.android.R
import com.sunnywether.android.logic.Repository
import com.sunnywether.android.logic.dao.PlaceDao
import com.sunnywether.android.logic.model.Place
import com.sunnywether.android.logic.model.Weather
import com.sunnywether.android.ui.MainActivity
import com.sunnywether.android.ui.weather.WeatherActivity
import kotlin.contracts.contract

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val placeName = view.findViewById<TextView>(R.id.placaName)
        val placeAddress = view.findViewById<TextView>(R.id.placeAddress)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            if(fragment.activity is MainActivity) {
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra(WeatherActivity.KEY_LNG, place.location.lng)
                    putExtra(WeatherActivity.KEY_LAT, place.location.lat)
                    putExtra(WeatherActivity.KEY_PLACE, place.name)
                }
                Repository.savePlace(place)
                fragment.startActivity(intent)
                fragment.activity?.finish()
            } else {
                (fragment.activity as? WeatherActivity)?.also {
                    it.findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawers()
                    it.viewModel.locationLng = place.location.lng
                    it.viewModel.locationLat = place.location.lat
                    it.viewModel.placeName = place.name
                    Repository.savePlace(place)
                    it.refreshWeather()
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }
}