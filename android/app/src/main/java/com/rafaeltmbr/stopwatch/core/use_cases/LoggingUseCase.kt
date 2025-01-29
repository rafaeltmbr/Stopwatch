package com.rafaeltmbr.stopwatch.core.use_cases

interface LoggingUseCase {
    fun debug(tag: String, message: String)
    fun info(tag: String, message: String)
    fun warn(tag: String, message: String)
    fun error(tag: String, message: String, exception: Exception? = null)
}