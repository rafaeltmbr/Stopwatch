package com.rafaeltmbr.stopwatch.core.use_cases.impl

import com.rafaeltmbr.stopwatch.core.services.LoggingService
import com.rafaeltmbr.stopwatch.core.use_cases.LoggingUseCase

class LoggingUseCaseImpl(
    private val loggingService: LoggingService
) : LoggingUseCase {
    override fun debug(tag: String, message: String) = loggingService.debug(tag, message)
    override fun info(tag: String, message: String) = loggingService.info(tag, message)
    override fun warn(tag: String, message: String) = loggingService.warn(tag, message)
    override fun error(tag: String, message: String, exception: Exception?) =
        loggingService.error(tag, message, exception)
}