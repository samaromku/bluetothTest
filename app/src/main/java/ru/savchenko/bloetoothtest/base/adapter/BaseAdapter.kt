package ru.savchenko.bloetoothtest.base.adapter

import android.support.v7.widget.RecyclerView

/**
 * Created by savchenko on 04.06.18.
 */
abstract class BaseAdapter<T>(
        private val itemClickListener: ItemClickListener<T>? = null,
        private val itemDeleteListener: ItemDeleteListener? = null) :
        RecyclerView.Adapter<BaseViewHolder<T>>() {
    protected var dataList: MutableList<T> = mutableListOf()

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addAll(data: MutableList<T>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.itemView.setOnClickListener(holder)
        holder.bind(dataList[position], itemClickListener)
    }

    interface ItemClickListener<T> {
        fun onClick(t:T)
    }

    interface ItemDeleteListener {
        fun onDelete(position: Int)
    }
}