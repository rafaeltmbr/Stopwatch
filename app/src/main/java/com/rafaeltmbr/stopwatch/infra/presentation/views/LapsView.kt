package com.rafaeltmbr.stopwatch.infra.presentation.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaeltmbr.stopwatch.domain.entities.LapStatus
import com.rafaeltmbr.stopwatch.infra.presentation.components.LapsSection
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LapsView(modifier: Modifier = Modifier) {
    val currentTime = "04:17.98"
    val isRunning = true

    val laps = listOf(
        ViewLap(index = 3, time = "01:16:11", status = LapStatus.CURRENT),
        ViewLap(index = 2, time = "01:15:09", status = LapStatus.BEST),
        ViewLap(index = 1, time = "01:16:35", status = LapStatus.WORST)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Laps") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    val (text, icon, iconSize) = when (isRunning) {
                        true -> Triple(currentTime, Icons.Outlined.Add, 24.dp)
                        false -> Triple("Resume", Icons.Outlined.PlayArrow, 26.dp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable(onClick = {})
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )
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
                laps = laps,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LapsViewPreview() {
    StopwatchTheme {
        LapsView()
    }
}