package com.rafaeltmbr.stopwatch.infra.presentation.compose.views

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaeltmbr.stopwatch.R
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.compose.components.LapListItem
import com.rafaeltmbr.stopwatch.infra.presentation.compose.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.view_models.LapsViewModel


@Composable
fun LapsView(viewModelFactory: LapsViewModelFactory, modifier: Modifier = Modifier) {
    val viewModel = viewModelFactory.make()
    val state by viewModel.state.collectAsState()

    Content(
        state = state,
        onAction = viewModel::handleAction,
        getViewLapByReversedArrayIndex = viewModel::getViewLapByReversedArrayIndex,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LapsViewModel.State,
    onAction: (LapsViewModel.Action) -> Unit,
    getViewLapByReversedArrayIndex: (index: Int) -> ViewLap,
    modifier: Modifier = Modifier
) {
    val orientation = LocalConfiguration.current.orientation

    val toolbarColors = when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )

        else -> TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.laps_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(LapsViewModel.Action.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    val (icon, iconSize, action) = when (state.status) {
                        Status.RUNNING -> Triple(
                            Icons.Outlined.Add,
                            24.dp,
                            LapsViewModel.Action.Lap
                        )

                        else -> Triple(Icons.Outlined.PlayArrow, 26.dp, LapsViewModel.Action.Resume)
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onAction(action) },
                        color = toolbarColors.containerColor
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text(text = state.time, fontSize = 20.sp)

                            Spacer(modifier = Modifier.width(4.dp))

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(iconSize)
                                )
                            }
                        }

                    }
                },
                colors = toolbarColors
            )
        },
        modifier = modifier
    ) { innerPadding ->
        val topPadding = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 8.dp
            else -> 24.dp
        }

        LazyColumn(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .defaultMinSize(minHeight = 182.dp)
                .padding(innerPadding),
            contentPadding = PaddingValues(
                top = topPadding,
                bottom = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
        ) {
            items(state.lapsCount) {
                val lap = getViewLapByReversedArrayIndex(it)

                Box(
                    modifier = Modifier
                        .then(
                            when (it) {
                                0 -> Modifier.clip(
                                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                                )

                                state.lapsCount - 1 -> Modifier.clip(
                                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                                )

                                else -> Modifier
                            }
                        )
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    LapListItem(lap = lap)
                }
            }
        }
    }

    BackHandler {
        onAction(LapsViewModel.Action.NavigateBack)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ContentPreview() {
    StopwatchTheme {
        val laps = listOf(
            ViewLap(
                index = 4,
                time = "01:37.84",
                status = Lap.Status.WORST
            ),
            ViewLap(
                index = 3,
                time = "00:55.31",
                status = Lap.Status.BEST
            ),
            ViewLap(
                index = 4,
                time = "01:01.75",
                status = Lap.Status.DONE
            ),
            ViewLap(
                index = 1,
                time = "00:28.49",
                status = Lap.Status.CURRENT
            )
        )

        Content(
            state = LapsViewModel.State(
                status = Status.RUNNING,
                lapsCount = laps.size,
                time = "02:37.84",
            ),
            getViewLapByReversedArrayIndex = { laps[it] },
            onAction = {},
        )
    }
}