package com.rafaeltmbr.stopwatch.infra.presentation.view_models

import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.impl.HomeViewModelImpl
import org.junit.Assert
import org.junit.Test

class HomeViewModelImplTest {
    @Test
    fun state_verifyInitialState() {
        val viewModel = HomeViewModelImpl()
        val expected = HomeViewState(
            status = Status.INITIAL,
            time = ViewTime(
                minutes = listOf("0", "0"),
                seconds = listOf("0", "0"),
                fraction = listOf("0", "0"),
            ),
            laps = emptyList(),
            showSeeMore = false
        )

        Assert.assertEquals(expected, viewModel.state.value)
    }
}
