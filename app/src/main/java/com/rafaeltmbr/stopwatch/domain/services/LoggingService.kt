package com.rafaeltmbr.stopwatch.domain.services

interface LoggingService {
    fun debug(tag: String, message: String)
    fun info(tag: String, message: String)
    fun warn(tag: String, message: String)
    fun error(tag: String, message: String, exception: Exception? = null)
}
