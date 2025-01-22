package com.rafaeltmbr.stopwatch.infra.presentation.compose.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaeltmbr.stopwatch.R
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.domain.entities.Status
import com.rafaeltmbr.stopwatch.infra.di.LapsViewModelFactory
import com.rafaeltmbr.stopwatch.infra.presentation.compose.components.LapsSection
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
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LapsViewModel.State,
    onAction: (LapsViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = state.time,
                                fontSize = 20.sp
                            )

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LapsSection(
                laps = state.laps,
                modifier = Modifier.padding(vertical = 24.dp)
            )
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
        Content(
            state = LapsViewModel.State(
                status = Status.RUNNING,
                time = "02:37.84",
                laps = listOf(
                    ViewLap(
                        index = 1,
                        time = "00:28.49",
                        status = Lap.Status.CURRENT
                    ),
                    ViewLap(
                        index = 1,
                        time = "01:01.75",
                        status = Lap.Status.DONE
                    ),
                    ViewLap(
                        index = 2,
                        time = "00:55.31",
                        status = Lap.Status.BEST
                    ),
                    ViewLap(
                        index = 1,
                        time = "01:37.84",
                        status = Lap.Status.WORST
                    )
                )
            ),
            onAction = {},
        )
    }
}