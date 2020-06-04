package com.example.falihmandiritestapp.di.module

import androidx.databinding.ViewDataBinding
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.ui.main.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class ArticleAdapterModule {

    @Provides
    internal fun provideViewViewHolder(binding: ViewDataBinding):
            BindableAdapter<MainViewModel, Article>.ViewHolder {
        return BindableAdapter<MainViewModel, Article>().ViewHolder(binding)
    }
}