package com.sunnywether.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit所有NetworkService的创建入口
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"
    //使用JSON
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //简化Service获取的方法
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}