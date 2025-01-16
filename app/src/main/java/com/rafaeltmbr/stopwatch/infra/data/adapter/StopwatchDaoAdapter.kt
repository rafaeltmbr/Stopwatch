package com.rafaeltmbr.stopwatch.infra.data.adapter

import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchState
import com.rafaeltmbr.stopwatch.domain.repositories.impl.StopwatchDao
import com.rafaeltmbr.stopwatch.infra.data.dao.LapsDaoRoom
import com.rafaeltmbr.stopwatch.infra.data.dao.StopwatchStateDaoRoom
import com.rafaeltmbr.stopwatch.infra.data.entities.LapEntity
import com.rafaeltmbr.stopwatch.infra.data.entities.StopwatchStateEntity

class StopwatchDaoAdapter(
    private val stopwatchStateDao: StopwatchStateDaoRoom,
    private val lapsDao: LapsDaoRoom
) : StopwatchDao {
    override suspend fun load(): StopwatchState? {
        val stopwatchState = stopwatchStateDao.load() ?: return null
        val laps = lapsDao.load()

        return StopwatchState(
            status = Status.PAUSED,
            milliseconds = stopwatchState.milliseconds,
            laps = laps.map {
                Lap(
                    index = it.index,
                    milliseconds = it.milliseconds,
                    status = Lap.Status.DONE
                )
            })
    }

    override suspend fun save(state: StopwatchState) {
        stopwatchStateDao.clear()
        stopwatchStateDao.save(
            StopwatchStateEntity(id = 0, milliseconds = state.milliseconds)
        )

        lapsDao.clear()
        lapsDao.save(
            state.laps.map { LapEntity(index = it.index, milliseconds = it.milliseconds) }
        )
    }
}