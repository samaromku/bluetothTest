package ru.savchenko.bloetoothtest.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import ru.savchenko.bloetoothtest.main.Logging.Companion.loge
import java.io.IOException
import java.util.*

class ServerThread(bluetoothAdapter: BluetoothAdapter,
                   private val manageConnectionSocket:(socket:BluetoothSocket)-> Unit) : Thread() {
    companion object {
        val NAME = "test"
        val TAG = ServerThread::class.java.simpleName
    }

    lateinit var serverSocket: BluetoothServerSocket

    init {
        var tmpSocket: BluetoothServerSocket? = null

        try {
            tmpSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.randomUUID())
        } catch (e: Exception) {
            loge(TAG, e)
        }
        tmpSocket?.let {
            serverSocket = it
        }
    }


    override fun run() {
        var socket :BluetoothSocket? = null
        while (true){
            println("start server socket connection")
            try {
                socket = serverSocket.accept()
            }catch (ex:IOException){
                loge(TAG, ex)
                break
            }

            if(socket!=null){
                manageConnectionSocket(socket)
                serverSocket.close()
                break
            }
        }
    }

    fun cancel(){
        try {
            serverSocket.close()
        }catch (ex:IOException){
            loge(TAG, ex)
        }
    }

}