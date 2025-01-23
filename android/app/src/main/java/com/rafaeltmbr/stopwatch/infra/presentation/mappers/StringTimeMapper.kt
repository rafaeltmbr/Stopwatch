package com.rafaeltmbr.stopwatch.infra.presentation.mappers

interface StringTimeMapper {
    fun map(milliseconds: Long): String
}