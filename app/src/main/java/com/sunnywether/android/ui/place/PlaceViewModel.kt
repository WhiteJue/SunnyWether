package com.sunnywether.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnywether.android.logic.Repository
import com.sunnywether.android.logic.model.Place

class PlaceViewModel : ViewModel() {
    //搜索关键词LiveData
    private val searchQueryLiveData = MutableLiveData<String>()

    //搜索结果（城市列表）缓存
    val placeList = ArrayList<Place>()

    //搜索结果LiveData
    val placeLiveData : LiveData<Result<List<Place>>> = searchQueryLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    //根据关键字搜索附近城市
    fun searchPlaces(query: String) {
        searchQueryLiveData.value = query
    }
}