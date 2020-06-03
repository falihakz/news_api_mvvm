package com.example.falihmandiritestapp.modules

import com.example.falihmandiritestapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModel = module {
    viewModel { MainViewModel(get()) }
}