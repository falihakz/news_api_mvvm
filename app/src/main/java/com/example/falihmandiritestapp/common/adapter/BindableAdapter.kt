package com.example.falihmandiritestapp.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.falihmandiritestapp.BR

/**
 * doc created by Falih on 17/10/2019
 *
 * Class ini digunakan sebagai generic adapter untuk recyclerview yang menggunakan databinding.
 */
open class BindableAdapter<VM: ViewModel, T>(
    private val viewModel: VM,
    private var layoutRes: Int? = null
): RecyclerView.Adapter<BindableAdapter<VM, T>.ViewHolder>() {

    private val items: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        layoutRes?.let {
            return it
        } ?: throw NullPointerException("layoutRes is not set in BindableAdapter")
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    fun setLayoutRes(layoutRes: Int){
        this.layoutRes = layoutRes
    }

    fun <LIST_T: List<T>> setItems(data: LIST_T?){
        /*val oldData = items
        val diffCallback = RatingDiffCallback(oldData, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)*/
        items.clear()
        items.addAll(data?: mutableListOf())
        notifyDataSetChanged()
        //diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: VM, position: Int?) {
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}