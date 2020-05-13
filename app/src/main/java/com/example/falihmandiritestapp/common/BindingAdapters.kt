package com.example.falihmandiritestapp.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.common.adapter.BindableAdapterFunctions

object BindingAdapters {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <SOME_LIST : List<Any>> setRecyclerViewProperties(recyclerView: RecyclerView, data: SOME_LIST?) {
        if (recyclerView.adapter is BindableAdapterFunctions<*>) {
            (recyclerView.adapter as BindableAdapterFunctions<SOME_LIST>).setItems(data)
        } else if (recyclerView.adapter is BindableAdapter<*, *>) {
            (recyclerView.adapter as BindableAdapter<*, Any>).setItems(data)
        }
    }

    @JvmStatic
    @Suppress("SpellCheckingInspection")
    @BindingAdapter("layoutres")
    fun setRecyclerViewAdapterLayoutRes(recyclerView: RecyclerView, layoutRes: Int) {
        if (recyclerView.adapter is BindableAdapter<*, *>) {
            (recyclerView.adapter as BindableAdapter<*, *>).setLayoutRes(layoutRes)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String) {
        if(url.isNotEmpty()) {
            Glide.with(view.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view)
        }
    }
}