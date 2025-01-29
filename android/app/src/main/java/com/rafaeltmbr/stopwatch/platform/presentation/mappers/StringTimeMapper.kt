package com.rafaeltmbr.stopwatch.platform.presentation.mappers

interface StringTimeMapper {
    fun map(milliseconds: Long): String
}