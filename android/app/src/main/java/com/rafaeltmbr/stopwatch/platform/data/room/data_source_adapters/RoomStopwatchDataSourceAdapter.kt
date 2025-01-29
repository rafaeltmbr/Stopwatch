package com.rafaeltmbr.stopwatch.platform.data.room.data_source_adapters

import com.rafaeltmbr.stopwatch.core.data.data_source_ports.StopwatchDataSourcePort
import com.rafaeltmbr.stopwatch.core.entities.Lap
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.core.entities.StopwatchState
import com.rafaeltmbr.stopwatch.platform.data.room.dao.LapsDao
import com.rafaeltmbr.stopwatch.platform.data.room.dao.StopwatchStateDao
import com.rafaeltmbr.stopwatch.platform.data.room.entities.LapEntity
import com.rafaeltmbr.stopwatch.platform.data.room.entities.StopwatchStateEntity

class RoomStopwatchDataSourceAdapter(
    private val stopwatchStateDao: StopwatchStateDao,
    private val lapsDao: LapsDao
) : StopwatchDataSourcePort {
    override suspend fun load(): StopwatchState? {
        val stopwatchState = stopwatchStateDao.load() ?: return null
        val laps = lapsDao.load()

        return StopwatchState(
            status = stopwatchState.status.stopwatchStatus ?: return null,
            milliseconds = stopwatchState.milliseconds,
            completedLaps = laps.map {
                Lap(
                    index = it.index,
                    milliseconds = it.milliseconds,
                    status = it.status.lapStatus ?: return@load null
                )
            },
            completedLapsMilliseconds = laps.sumOf { it.milliseconds }
        )
    }

    override suspend fun save(state: StopwatchState) {
        stopwatchStateDao.clear()
        stopwatchStateDao.save(
            StopwatchStateEntity(
                id = 1,
                milliseconds = state.milliseconds,
                status = state.status.number
            )
        )

        lapsDao.clear()
        lapsDao.save(
            state.completedLaps.map {
                LapEntity(
                    index = it.index,
                    milliseconds = it.milliseconds,
                    status = it.status.number
                )
            }
        )
    }
}

private val Status.number: Int
    get() = when (this) {
        Status.INITIAL -> 1
        Status.RUNNING -> 2
        Status.PAUSED -> 3
    }

private val Lap.Status.number: Int
    get() = when (this) {
        Lap.Status.CURRENT -> 1
        Lap.Status.BEST -> 2
        Lap.Status.WORST -> 3
        Lap.Status.DONE -> 4
    }

private val Int.stopwatchStatus: Status?
    get() = when (this) {
        1 -> Status.INITIAL
        2 -> Status.RUNNING
        3 -> Status.PAUSED
        else -> null
    }

private val Int.lapStatus: Lap.Status?
    get() = when (this) {
        1 -> Lap.Status.CURRENT
        2 -> Lap.Status.BEST
        3 -> Lap.Status.WORST
        4 -> Lap.Status.DONE
        else -> null
    }
