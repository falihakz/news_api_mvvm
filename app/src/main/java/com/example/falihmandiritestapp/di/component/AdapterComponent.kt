package com.example.falihmandiritestapp.di.component

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.di.module.ArticleAdapterModule
import com.example.falihmandiritestapp.ui.main.ArticleAdapter
import com.example.falihmandiritestapp.ui.main.MainViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ArticleAdapterModule::class])
interface AdapterComponent {

    fun getViewHolder(): BindableAdapter<MainViewModel, Article>.ViewHolder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun binding(binding: ViewDataBinding): Builder

        fun build(): AdapterComponent
    }
}