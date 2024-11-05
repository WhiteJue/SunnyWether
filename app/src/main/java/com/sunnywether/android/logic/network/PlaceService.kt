package com.sunnywether.android.logic.network

import com.sunnywether.android.SunnyWetherApplication
import com.sunnywether.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//用于Retrofit构建动态代理
interface PlaceService {
    //根据关键词查询附近城市
    @GET("v2/place?token=${SunnyWetherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String) : Call<PlaceResponse>
}