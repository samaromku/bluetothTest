package ru.savchenko.bloetoothtest.wifi

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import ru.savchenko.bloetoothtest.R
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_wifi.*
import ru.savchenko.bloetoothtest.base.adapter.BaseAdapter


class WiFiActivity : AppCompatActivity(), WifiStateChange {
    lateinit var receiver:WiFiBroadCastReceiver
    lateinit var manager: WifiP2pManager
    lateinit var channel: WifiP2pManager.Channel
    val adapter = WifiDevicesAdapter(object :BaseAdapter.ItemClickListener<WifiP2pDevice>{
        override fun onClick(t: WifiP2pDevice) {
            println("click on $t")
            val config = WifiP2pConfig()
            config.deviceAddress = t.deviceAddress
            manager.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    println("wifi connect success")
                }
                override fun onFailure(reason: Int) {
                    println("wifi connect failure")
                }
            })
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, Looper.getMainLooper(), null)
        receiver = WiFiBroadCastReceiver(manager, channel, this)

        val intent = IntentFilter()
        intent.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intent.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intent.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intent.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        registerReceiver(receiver, intent)
        rvWifi.layoutManager = LinearLayoutManager(this)
        rvWifi.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onPeerListChanged() {
        manager.requestPeers(channel) { peers ->
            adapter.addAll(peers.deviceList.toMutableList())
        }
    }
}