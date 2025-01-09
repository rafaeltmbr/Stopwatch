package com.rafaeltmbr.stopwatch.presentation.views

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaeltmbr.stopwatch.presentation.components.Lap
import com.rafaeltmbr.stopwatch.presentation.components.LapsSection
import com.rafaeltmbr.stopwatch.presentation.theme.StopwatchTheme

private data class Time(
    val minutes: List<String>,
    val seconds: List<String>,
    val fraction: List<String>,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(modifier: Modifier = Modifier) {
    val time = Time(
        minutes = listOf("0", "2"),
        seconds = listOf("3", "7"),
        fraction = listOf("9", "1"),
    )

    val laps = listOf(
        Lap(index = 1, time = "01:16:35", diff = ""),
        Lap(index = 2, time = "02:15:09", diff = "+0:58.34"),
        Lap(index = 3, time = "02:16:11", diff = "+1:01.02"),
    )

    val showSeeMore = laps.isNotEmpty()

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
                    minutes = time.minutes,
                    seconds = time.seconds,
                    fraction = time.fraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppButton(type = AppButtonType.RESET) {}
                    AppButton(type = AppButtonType.START) {}
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showSeeMore) {
                LapsSection(
                    laps,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {}
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    StopwatchTheme {
        HomeView()
    }
}

@Composable
private fun Count(
    minutes: List<String>,
    seconds: List<String>,
    fraction: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        for (e in minutes) {
            CountLargeText(text = e, modifier = Modifier.width(54.dp))
        }

        CountLargeText(text = ":")

        for (e in seconds) {
            CountLargeText(text = e, modifier = Modifier.width(54.dp))
        }

        CountSmallText(text = ".")

        for (e in fraction) {
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
            minutes = listOf("0", "2"),
            seconds = listOf("4", "1"),
            fraction = listOf("9", "3")
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
