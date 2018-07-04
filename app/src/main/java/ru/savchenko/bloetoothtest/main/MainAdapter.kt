package ru.savchenko.bloetoothtest.main

import android.bluetooth.BluetoothDevice
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_device.*
import ru.savchenko.bloetoothtest.R
import ru.savchenko.bloetoothtest.base.adapter.BaseAdapter
import ru.savchenko.bloetoothtest.base.adapter.BaseViewHolder
import ru.savchenko.bloetoothtest.base.adapter.getViewForHolder

class MainAdapter(itemClickListener: ItemClickListener<BluetoothDevice>)
    :BaseAdapter<BluetoothDevice>(itemClickListener = itemClickListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BluetoothDevice> {
        return MainViewHolder(getViewForHolder(parent, R.layout.item_device))
    }

    fun add(device:BluetoothDevice){
        dataList.add(device)
        notifyItemInserted(dataList.size)
    }

    class MainViewHolder(val itemView: View):BaseViewHolder<BluetoothDevice>(itemView){


        override fun bind(t: BluetoothDevice, clickListener: ItemClickListener<BluetoothDevice>?) {
            super.bind(t, clickListener)
            tvAddress.text = t.address
            tvName.text = t.name
        }
    }
}