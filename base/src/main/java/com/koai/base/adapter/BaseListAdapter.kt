/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

@SuppressLint("DiffUtilEquals")
class TComparator<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

}

abstract class BaseListAdapter<T : Any> : ListAdapter<T, BaseListAdapter.VH>(TComparator<T>()) {
    var listener: Action<T>? = null
    var observer: Observer<T>? = null

    class VH(val binding: ViewBinding) : ViewHolder(binding.root)

    abstract fun getLayoutId(): Int
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutId(),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.root.setOnClickListener {
            listener?.click(position, getItem(position))
        }
//        observer?.subData...
    }

    interface Action<T> {
        /**
         * @param position of viewItem
         * @param data of viewItem
         */
        fun click(position: Int, data: T, code: Int = 0)
    }

    interface Observer<T>{
        /**
         * @param root is viewItem (for visible handle)
         * @param childView can be child recycle_view or viewpager
         * @param data of item
         * @param code for handle
         */
        fun subData(root: View, childView: View, data: T, code: Int = 0)
    }
}

