package com.rafaeltmbr.stopwatch.platform.presentation.compose.views

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaeltmbr.stopwatch.R
import com.rafaeltmbr.stopwatch.core.entities.Lap
import com.rafaeltmbr.stopwatch.core.entities.Status
import com.rafaeltmbr.stopwatch.platform.di.HomeViewModelFactory
import com.rafaeltmbr.stopwatch.platform.presentation.compose.components.LapListItem
import com.rafaeltmbr.stopwatch.platform.presentation.compose.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.platform.presentation.entities.ViewTime
import com.rafaeltmbr.stopwatch.platform.presentation.view_models.HomeViewModel


@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    viewModelFactory: HomeViewModelFactory,
) {
    val viewModel = viewModelFactory.make()
    val state by viewModel.state.collectAsState()

    Content(
        state = state,
        onAction = viewModel::handleAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: HomeViewModel.State,
    onAction: (HomeViewModel.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    val orientation = LocalConfiguration.current.orientation

    val toolbarColors = when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )

        else -> TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = toolbarColors
            )
        }, modifier = modifier
    ) { innerPadding ->

        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                PortraitContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun LandscapeContent(
    state: HomeViewModel.State,
    onAction: (HomeViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(0.5f)
        ) {
            Count(
                time = state.time,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Buttons(state, onAction, modifier = Modifier.fillMaxWidth())
        }

        AnimatedVisibility(state.showLapsSection) {
            LapsContainer(state, onAction)
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun LandscapeContentPreview() {
    StopwatchTheme {
        LandscapeContent(
            state = HomeViewModel.State(
                status = Status.INITIAL,
                time = ViewTime(
                    minutes = listOf("0", "3"),
                    seconds = listOf("4", "1"),
                    fraction = listOf("9", "3")
                ),
                laps = listOf(
                    ViewLap(index = 3, time = "01:16:11", status = Lap.Status.CURRENT),
                    ViewLap(index = 2, time = "01:15:09", status = Lap.Status.BEST),
                    ViewLap(index = 1, time = "01:16:35", status = Lap.Status.WORST)
                ),
                showSeeAllLaps = true,
                showLapsSection = true
            ),
            onAction = {},
        )
    }
}

@Composable
private fun PortraitContent(
    state: HomeViewModel.State,
    onAction: (HomeViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
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

            Buttons(state, onAction, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(state.showLapsSection) {
            LapsContainer(state, onAction)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PortraitContentPreview() {
    StopwatchTheme {
        PortraitContent(
            state = HomeViewModel.State(
                status = Status.INITIAL,
                time = ViewTime(
                    minutes = listOf("0", "3"),
                    seconds = listOf("4", "1"),
                    fraction = listOf("9", "3")
                ),
                laps = listOf(
                    ViewLap(index = 3, time = "01:16:11", status = Lap.Status.CURRENT),
                    ViewLap(index = 2, time = "01:15:09", status = Lap.Status.BEST),
                    ViewLap(index = 1, time = "01:16:35", status = Lap.Status.WORST)
                ),
                showSeeAllLaps = true,
                showLapsSection = true
            ),
            onAction = {},
        )
    }
}

@Composable
private fun Buttons(
    state: HomeViewModel.State,
    onAction: (HomeViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.status) {
        Status.INITIAL ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
            ) {
                AppButton(
                    type = AppButtonType.START,
                    onClick = { onAction(HomeViewModel.Action.Start) }
                )
            }

        Status.PAUSED ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
            ) {
                AppButton(
                    type = AppButtonType.RESUME,
                    onClick = { onAction(HomeViewModel.Action.Resume) }
                )
                AppButton(
                    type = AppButtonType.RESET,
                    onClick = { onAction(HomeViewModel.Action.Reset) }
                )
            }

        Status.RUNNING ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
            ) {
                AppButton(
                    type = AppButtonType.PAUSE,
                    onClick = { onAction(HomeViewModel.Action.Pause) }
                )
                AppButton(
                    type = AppButtonType.LAP,
                    onClick = { onAction(HomeViewModel.Action.Lap) }
                )
            }
    }
}

@Composable
private fun LapsContainer(
    state: HomeViewModel.State,
    onAction: (HomeViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val onSeeAll = { onAction(HomeViewModel.Action.SeeAll) }

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.laps_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (state.showSeeAllLaps) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onSeeAll),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.see_all_button),
                            style = MaterialTheme.typography.titleLarge
                                .copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 182.dp)
        ) {
            for (lap in state.laps) {
                LapListItem(lap = lap)
            }
        }
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
            MaterialTheme.colorScheme.surface,
            60.dp
        )

        AppButtonType.RESET -> Triple(
            Icons.Outlined.Refresh,
            MaterialTheme.colorScheme.tertiaryContainer,
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
