package ru.savchenko.bloetoothtest.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager

class WiFiBroadCastReceiver(val manager: WifiP2pManager,
                            val channel: WifiP2pManager.Channel,
                            val activity: WiFiActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION == action) {
            val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                println("wifi enabled")
                manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        println("discover wifi success")
                    }

                    override fun onFailure(reason: Int) {
                        println("discover wifi failure")
                    }
                })
            } else {
                println("wifi disabled")
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION == action) {
            (activity as WifiStateChange).onPeerListChanged()
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION == action) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION == action) {
            // Respond to this device's wifi state changing
        }

    }
}