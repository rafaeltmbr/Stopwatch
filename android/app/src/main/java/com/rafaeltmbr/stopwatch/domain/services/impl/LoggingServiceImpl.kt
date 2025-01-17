package com.rafaeltmbr.stopwatch.domain.services.impl

import com.rafaeltmbr.stopwatch.domain.services.LoggingService
import com.rafaeltmbr.stopwatch.domain.services.external_resources.PlatformLoggerFacade


class LoggingServiceImpl(
    private val platformLogger: PlatformLoggerFacade
) : LoggingService {
    override fun debug(tag: String, message: String) = platformLogger.debug(tag, message)

    override fun info(tag: String, message: String) = platformLogger.info(tag, message)

    override fun warn(tag: String, message: String) = platformLogger.warn(tag, message)

    override fun error(tag: String, message: String, exception: Exception?) =
        platformLogger.error(tag, message, exception)
}