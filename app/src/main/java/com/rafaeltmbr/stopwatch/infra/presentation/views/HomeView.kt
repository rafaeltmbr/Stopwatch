package com.rafaeltmbr.stopwatch.infra.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaeltmbr.stopwatch.domain.entities.StopwatchStatus
import com.rafaeltmbr.stopwatch.infra.di.ViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.components.LapsSection
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewAction
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.HomeViewState


@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelFactory,
) {
    val viewModel = viewModelFactory.makeHomeViewModel()
    val state by viewModel.state.collectAsState()

    HomeViewContent(
        state = state,
        handleAction = viewModel::handleAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeViewContent(
    state: HomeViewState,
    handleAction: (HomeViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Stopwatch") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }, modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .defaultMinSize(minHeight = LocalConfiguration.current.screenHeightDp.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .padding(vertical = 48.dp)
                    .padding(top = 24.dp)
            ) {
                Count(
                    time = state.time,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                when (state.status) {
                    StopwatchStatus.INITIAL ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppButton(
                                type = AppButtonType.START,
                                onClick = { handleAction(HomeViewAction.Start) }
                            )
                        }

                    StopwatchStatus.PAUSED ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppButton(
                                type = AppButtonType.RESET,
                                onClick = { handleAction(HomeViewAction.Reset) }
                            )
                            AppButton(
                                type = AppButtonType.RESUME,
                                onClick = { handleAction(HomeViewAction.Resume) }
                            )
                        }

                    StopwatchStatus.RUNNING ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppButton(
                                type = AppButtonType.PAUSE,
                                onClick = { handleAction(HomeViewAction.Pause) }
                            )
                            AppButton(
                                type = AppButtonType.LAP,
                                onClick = { handleAction(HomeViewAction.Lap) }
                            )
                        }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (state.showSeeMore) {
                LapsSection(
                    laps = state.laps,
                    modifier = Modifier.padding(bottom = 24.dp),
                    onSeeAll = { handleAction(HomeViewAction.SeeAll) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeViewContentPreview() {
    StopwatchTheme {
        HomeViewContent(
            state = HomeViewState(
                status = StopwatchStatus.INITIAL,
                time = ViewTime(
                    minutes = listOf("0", "3"),
                    seconds = listOf("4", "1"),
                    fraction = listOf("9", "3")
                ),
                laps = listOf(
                    ViewLap(index = 1, time = "01:16.35", diff = ""),
                    ViewLap(index = 2, time = "02:21.52", diff = "+01:05.17"),
                    ViewLap(index = 2, time = "03:20.11", diff = "+00:58.59"),
                ),
                showSeeMore = true,
            ),
            handleAction = {},
        )
    }
}

@Composable
private fun Count(
    time: ViewTime,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        for (e in time.minutes) {
            CountLargeText(text = e, modifier = Modifier.width(54.dp))
        }

        CountLargeText(text = ":")

        for (e in time.seconds) {
            CountLargeText(text = e, modifier = Modifier.width(54.dp))
        }

        CountSmallText(text = ".")

        for (e in time.fraction) {
            CountSmallText(text = e, modifier = Modifier.width(21.dp))
        }
    }
}

@Composable
private fun CountLargeText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 96.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@Composable
private fun CountSmallText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 36.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CounterPreview() {
    StopwatchTheme {
        Count(
            time = ViewTime(
                minutes = listOf("0", "2"),
                seconds = listOf("4", "1"),
                fraction = listOf("9", "3")
            )
        )
    }
}

private enum class AppButtonType {
    START, PAUSE, RESUME, RESET, LAP
}

@Composable
private fun AppButton(type: AppButtonType, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val (icon, color, size) = when (type) {
        AppButtonType.START -> Triple(
            Icons.Outlined.PlayArrow,
            MaterialTheme.colorScheme.surfaceVariant,
            60.dp
        )

        AppButtonType.PAUSE -> Triple(
            Icons.Filled.Pause,
            MaterialTheme.colorScheme.surface,
            54.dp
        )

        AppButtonType.RESUME -> Triple(
            Icons.Outlined.PlayArrow,
            MaterialTheme.colorScheme.surfaceVariant,
            60.dp
        )

        AppButtonType.RESET -> Triple(
            Icons.Outlined.Refresh,
            MaterialTheme.colorScheme.surface,
            48.dp
        )

        AppButtonType.LAP -> Triple(
            Icons.Outlined.Add,
            MaterialTheme.colorScheme.surfaceVariant,
            54.dp
        )
    }

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(40.dp))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.background(color = color)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonStartPreview() {
    StopwatchTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            AppButton(type = AppButtonType.START) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPausePreview() {
    StopwatchTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            AppButton(type = AppButtonType.PAUSE) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonResumePreview() {
    StopwatchTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            AppButton(type = AppButtonType.RESUME) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonResetPreview() {
    StopwatchTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            AppButton(type = AppButtonType.RESET) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonLapPreview() {
    StopwatchTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            AppButton(type = AppButtonType.LAP) {}
        }
    }
}
