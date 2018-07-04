package ru.savchenko.bloetoothtest.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import ru.savchenko.bloetoothtest.main.Logging.Companion.loge
import java.io.IOException
import java.util.*

class ClientThread(device:BluetoothDevice,
                   val bluetoothAdapter: BluetoothAdapter,
                   val manageConnectedSocket:(socket:BluetoothSocket)->Unit):Thread() {
    val TAG = ClientThread::class.java.simpleName
    lateinit var socket :BluetoothSocket

    init {
        var tmpSocket :BluetoothSocket? = null
        try {
            tmpSocket = device.createRfcommSocketToServiceRecord(UUID.randomUUID())
        } catch (e: Exception) {
            loge(TAG, e)
        }
        tmpSocket?.let {
            socket = it
        }
    }

    override fun run() {
        bluetoothAdapter.cancelDiscovery()
        println("start client socket connection")
        try {
            socket.connect()
        } catch (e: IOException) {
            loge(TAG, e)
            try {
                socket.close()
            } catch (e: IOException) {
                loge(TAG, e)
            }
            return
        }
        manageConnectedSocket(socket)
    }

    fun cancel(){
        try {
            socket.close()
        }catch (ex:IOException){
            loge(TAG, ex)
        }
    }


}