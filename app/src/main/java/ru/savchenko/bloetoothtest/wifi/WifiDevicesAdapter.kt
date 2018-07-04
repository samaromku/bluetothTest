package ru.savchenko.bloetoothtest.wifi

import android.net.wifi.p2p.WifiP2pDevice
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_wifi.*
import ru.savchenko.bloetoothtest.R
import ru.savchenko.bloetoothtest.base.adapter.BaseAdapter
import ru.savchenko.bloetoothtest.base.adapter.BaseViewHolder
import ru.savchenko.bloetoothtest.base.adapter.getViewForHolder

class WifiDevicesAdapter(itemClickListener: ItemClickListener<WifiP2pDevice>):BaseAdapter<WifiP2pDevice>(itemClickListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<WifiP2pDevice> {
        return WifiViewHolder(getViewForHolder(parent, R.layout.item_wifi))
    }

    class WifiViewHolder(itemView:View):BaseViewHolder<WifiP2pDevice>(itemView){
        override fun bind(t: WifiP2pDevice, clickListener: ItemClickListener<WifiP2pDevice>?) {
            super.bind(t, clickListener)
            tvName.text = t.deviceName
            tvAddress.text = t.deviceAddress
        }
    }
}