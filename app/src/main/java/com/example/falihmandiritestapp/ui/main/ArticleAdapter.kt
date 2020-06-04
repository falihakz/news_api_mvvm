package com.example.falihmandiritestapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.di.component.AdapterComponent
import com.example.falihmandiritestapp.di.component.DaggerAdapterComponent

class ArticleAdapter(private val viewModel: MainViewModel, layoutRes: Int? = null):
    BindableAdapter<MainViewModel, Article>(viewModel, layoutRes) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val component: AdapterComponent = DaggerAdapterComponent.builder()
            .binding(binding)
            .build()
        return component.getViewHolder()
    }
}