package com.rafaeltmbr.stopwatch.platform.services.external_resource_adapters

import android.util.Log
import com.rafaeltmbr.stopwatch.core.services.external_resource_ports.PlatformLoggerPort

class AndroidPlatformLoggerAdapter : PlatformLoggerPort {
    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun warn(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun error(tag: String, message: String, exception: Exception?) {
        Log.e(tag, message, exception)
    }
}