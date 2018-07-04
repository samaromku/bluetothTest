package ru.savchenko.bloetoothtest.main

import android.util.Log

class Logging {
    companion object {
        fun logd(tag:String, message:String){
            Log.d(tag, message)
        }
        fun logi(tag:String, message:String){
            Log.i(tag, message)
        }
        fun loge(tag:String, t:Throwable){
            Log.e(tag, "error", t)
        }
        fun loge(tag:String, message:String, t:Throwable){
            Log.e(tag, "error", t)
        }
    }
}