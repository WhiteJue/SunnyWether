package com.sunnywether.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//网络API集中管理类
object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()

    //根据关键词查询附近城市
    suspend fun searchPlace(query: String) = placeService.searchPlaces(query).await()

    //扩展方法：从Call<T>获取结果，需要从协程调用
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object: Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}