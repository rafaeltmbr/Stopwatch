package com.rafaeltmbr.stopwatch.infra.presentation.mappers

interface StringTimeMapper {
    fun mapToStringTime(seconds: Float): String
}