package com.rafaeltmbr.stopwatch.infra.services.external_resources

import android.util.Log
import com.rafaeltmbr.stopwatch.domain.services.external_resources.PlatformLoggerFacade

class AndroidLoggerFacade : PlatformLoggerFacade {
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