package ru.savchenko.bloetoothtest.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by savchenko on 24.05.18.
 */
fun getViewForHolder(parent: ViewGroup, resource:Int):View{
    return LayoutInflater.from(parent.context).inflate(resource, parent, false)
}

