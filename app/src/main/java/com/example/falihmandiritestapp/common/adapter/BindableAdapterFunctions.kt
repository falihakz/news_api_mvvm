package com.example.falihmandiritestapp.common.adapter

interface BindableAdapterFunctions<T> {
    fun setItems(data: T?)
    fun addItems(data: T)
}