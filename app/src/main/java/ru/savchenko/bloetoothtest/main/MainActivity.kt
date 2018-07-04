package ru.savchenko.bloetoothtest.main

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.savchenko.bloetoothtest.R
import ru.savchenko.bloetoothtest.base.adapter.BaseAdapter
import ru.savchenko.bloetoothtest.main.Logging.Companion.logd
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var blAdapter: BluetoothAdapter
    val TAG = MainActivity::class.java.simpleName
    var socket :BluetoothSocket?=null
    val mHandler:Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
//                Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
//                    BluetoothChatService.STATE_CONNECTED -> {
//                        setStatus(getString(R.string.title_connected_to, mConnectedDeviceName))
//                        mConversationArrayAdapter.clear()
//                    }
//                    BluetoothChatService.STATE_CONNECTING -> setStatus(R.string.title_connecting)
//                    BluetoothChatService.STATE_LISTEN, BluetoothChatService.STATE_NONE -> setStatus(R.string.title_not_connected)
//                }
//                Constants.MESSAGE_WRITE -> {
//                    val writeBuf = msg.obj as ByteArray
//                    // construct a string from the buffer
//                    val writeMessage = String(writeBuf)
//                    mConversationArrayAdapter.add("Me:  $writeMessage")
//                }
//                Constants.MESSAGE_READ -> {
//                    val readBuf = msg.obj as ByteArray
//                    // construct a string from the valid bytes in the buffer
//                    val readMessage = String(readBuf, 0, msg.arg1)
//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage)
//                }
//                Constants.MESSAGE_DEVICE_NAME -> {
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME)
//                    if (null != activity) {
//                        Toast.makeText(activity, "Connected to $mConnectedDeviceName", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                Constants.MESSAGE_TOAST -> if (null != activity) {
//                    Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
//                            Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }
    val adapter = MainAdapter(object :BaseAdapter.ItemClickListener<BluetoothDevice>{
        override fun onClick(t: BluetoothDevice) {
            println("click on ${t.name} ${t.address}")
            val service=BluetoothChatService(mHandler)
            service.start()
            service.connect(blAdapter.getRemoteDevice(t.address), true)
//            connectToDevice(t.address)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        blAdapter = BluetoothAdapter.getDefaultAdapter()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(findDevicesReceiver, filter)
        val changeStateFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(changeBlStateReceiver, changeStateFilter)

        rvDevices.layoutManager = LinearLayoutManager(this)
        rvDevices.adapter = adapter

        try {
            turnOnBluetooth()
        } catch (ex: Exception) {
            toast("bluetooth is disabled on your device")
            ex.printStackTrace()
        }
        btnStartDiscovering.setOnClickListener {
            logd(TAG, "start discovering")
            blAdapter.startDiscovery()
        }
        btnFindPair.setOnClickListener {
            logd(TAG, "try to find pair")
            getBondedDevices()
        }
        btnMakeDeviceDiscoverable.setOnClickListener {
            makeDeviceDiscoverable()
        }
        btnServer.setOnClickListener {
            toast("i'm server")
            ServerThread(blAdapter, { socket ->
                println("socket $socket")
            }).start()
        }
        btnClient.setOnClickListener {
            toast("i'm client")
        }
    }

    private fun makeDeviceDiscoverable() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(intent)
    }

    private val changeBlStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED == action) {
                val extraCurrent = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0)
                val extraPrevious = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0)
                when (extraCurrent) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                        toast("The device is in discoverable mode.")
                    }
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                        toast("The device isn't in discoverable mode but can still receive connections.")
                    }
                    BluetoothAdapter.SCAN_MODE_NONE -> {
                        toast("The device isn't in discoverable mode and cannot receive connections.")
                    }
                }
            }
        }
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private val findDevicesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val name = device.name
                val address = device.address
                adapter.add(device)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterReceiver(findDevicesReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            logd(TAG, "bl on")
        } else if (resultCode == Activity.RESULT_CANCELED) {
            turnOnBluetooth()
        }
    }

    private fun turnOnBluetooth() {
        if (!blAdapter.isEnabled) {
            val turnOnBlueTooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOnBlueTooth, 1)
        }
    }

    private fun getBondedDevices() {
        val pairedDevices = blAdapter.bondedDevices
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                val name = device.name
                val address = device.address
                logd(TAG, "device $name $address")
            }
        } else {
            logd(TAG, "not paired devices")
        }
    }
}
