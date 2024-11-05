package com.sunnywether.android.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnywether.android.R
import com.sunnywether.android.databinding.FragmentPlaceBinding
import com.sunnywether.android.logic.model.Place
import java.io.LineNumberReader

class PlaceFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter
    private lateinit var binding : FragmentPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity != null && binding != null) {
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            //adapter绑定ViewModel中的数据
            adapter = PlaceAdapter(this, viewModel.placeList)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = layoutManager

            //搜索框内容发生变化时触发ViewModel执行搜索
            binding.searchPlaceEdit.addTextChangedListener { editable ->
                val content = editable.toString()
                if(content.isNotEmpty()) {
                    //触发ViewModel搜索
                    viewModel.searchPlaces(content)
                } else {
                    //搜索关键词为空，隐藏RecyclerView，显示背景
                    binding.recyclerView.visibility = View.GONE
                    binding.bgImageView.visibility = View.VISIBLE
                    viewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            //观察ViewModel中的搜索结果LiveData
            viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
              val places = result.getOrNull()
              if(places != null) {
                  //搜索结果不为空，显示RecyclerView，隐藏背景
                  binding.recyclerView.visibility = View.VISIBLE
                  binding.bgImageView.visibility = View.VISIBLE
                  //更新ViewModel缓存，触发RecyclerView更新数据
                  viewModel.placeList.clear()
                  viewModel.placeList.addAll(places)
                  adapter.notifyDataSetChanged()
              } else {
                  //搜索结果为空
                  Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                  result.exceptionOrNull()?.printStackTrace()
              }
            })
        }


    }
}