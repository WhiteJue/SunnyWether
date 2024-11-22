package com.sunnywether.android.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sunnywether.android.logic.dao.PlaceDao
import com.sunnywether.android.logic.model.Place
import com.sunnywether.android.logic.model.Weather
import com.sunnywether.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

//统一的Repository
//向外界返回LiveData，以便响应式编程
object Repository {
    //liveData方法用于构建LiveData，同时开启协程
    fun searchPlaces(query: String): LiveData<Result<List<Place>>> = liveData(Dispatchers.IO) {
        //子线程中执行
        val result = try {
            //调用API
            val placeResponse = SunnyWeatherNetwork.searchPlace(query)
            if(placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        //emit方法构建LiveData
        emit(result)
    }

    fun getWeather(lng: String, lat: String): LiveData<Result<Weather>> = liveData(Dispatchers.IO) {
        val result = try {
            //创建协程作用域以便使用async{}
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if(realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    Result.success(Weather(realtimeResponse.result.realtime, dailyResponse.result.daily))
                } else {
                    Result.failure(RuntimeException("realtime response status is ${realtimeResponse.status}" +
                    "daily response status is ${dailyResponse.status}"))
                }
            }

        } catch (e: Exception) {
            Result.failure<Weather>(e)
        }
        emit(result)
    }

    fun getPlace() : Place = PlaceDao.getPlace()

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun isPlaceSaved() : Boolean = PlaceDao.containsPlace()
}