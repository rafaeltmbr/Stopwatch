package com.rafaeltmbr.stopwatch.infra.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafaeltmbr.stopwatch.R
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme

@Composable
fun LapsSection(
    laps: List<ViewLap>,
    modifier: Modifier = Modifier,
    showTitle: Boolean = false,
    onSeeAll: (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (showTitle) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.laps_title),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                if (onSeeAll != null) {
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
        }

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 182.dp)
        ) {
            for (e in laps) {
                if (e != laps.first()) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(60.dp)
                ) {
                    val (color, statusText) = when (e.status) {
                        Lap.Status.CURRENT ->
                            MaterialTheme.colorScheme.onSurface to null

                        Lap.Status.BEST ->
                            MaterialTheme.colorScheme.primary to stringResource(R.string.best)

                        Lap.Status.WORST ->
                            MaterialTheme.colorScheme.error to stringResource(R.string.worst)

                        Lap.Status.DONE ->
                            MaterialTheme.colorScheme.onSurface to null
                    }

                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.defaultMinSize(minWidth = 44.dp)
                    ) {
                        Text(
                            text = "#${e.index}",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }

                    Text(
                        text = e.time,
                        color = color,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )

                    if (statusText != null) {
                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = statusText,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LapsSectionPreview() {
    val lapsSectionLaps = listOf(
        ViewLap(index = 2, time = "01:15:09", status = Lap.Status.CURRENT),
        ViewLap(index = 1, time = "01:16:35", status = Lap.Status.DONE),
    )

    StopwatchTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LapsSection(lapsSectionLaps, showTitle = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LapsSectionSeeAllPreview() {
    val lapsSectionLaps = listOf(
        ViewLap(index = 48, time = "01:16:11", status = Lap.Status.CURRENT),
        ViewLap(index = 47, time = "01:15:09", status = Lap.Status.BEST),
        ViewLap(index = 46, time = "01:16:35", status = Lap.Status.WORST)
    )

    StopwatchTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LapsSection(lapsSectionLaps, showTitle = true) {}
        }
    }
}