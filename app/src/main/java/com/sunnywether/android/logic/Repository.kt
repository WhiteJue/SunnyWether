package com.sunnywether.android.logic

import androidx.lifecycle.liveData
import com.sunnywether.android.logic.model.Place
import com.sunnywether.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

//统一的Repository
//向外界返回LiveData，以便响应式编程
object Repository {
    //liveData方法用于构建LiveData，同时开启协程
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
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
}