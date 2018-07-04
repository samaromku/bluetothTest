package ru.savchenko.bloetoothtest.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by savchenko on 18.02.18.
 */
/**
 * layoutcontainer for kotlin extentions
 */
open class BaseViewHolder<T>(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView),
        View.OnClickListener,
        LayoutContainer{
    private var clickListener: BaseAdapter.ItemClickListener<T>? = null
    var t:T? = null

    open fun bind(t:T, clickListener: BaseAdapter.ItemClickListener<T>?){
        this.clickListener = clickListener
        this.t=t
    }

    override fun onClick(p0: View?) {
        t?.let {
            clickListener?.onClick(it)
        }
    }
}