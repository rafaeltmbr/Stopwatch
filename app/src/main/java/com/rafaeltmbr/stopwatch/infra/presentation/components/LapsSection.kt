package com.rafaeltmbr.stopwatch.infra.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafaeltmbr.stopwatch.domain.entities.LapStatus
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme

@Composable
fun LapsSection(
    laps: List<ViewLap>,
    modifier: Modifier = Modifier,
    onSeeAll: (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Laps",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))

            if (onSeeAll != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.titleLarge
                            .copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
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
                .defaultMinSize(minHeight = 180.dp)
        ) {
            for (e in laps) {
                if (e.index > 0) {
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
                    val (color, weight) = when (e.status) {
                        LapStatus.CURRENT -> MaterialTheme.colorScheme.onSurface to FontWeight.SemiBold
                        LapStatus.BEST -> MaterialTheme.colorScheme.primary to FontWeight.Bold
                        LapStatus.WORST -> MaterialTheme.colorScheme.error to FontWeight.Bold
                        LapStatus.DONE -> MaterialTheme.colorScheme.onSurface to FontWeight.SemiBold
                    }

                    Text(
                        text = "#${e.index}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = e.milliseconds,
                        color = color,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = weight)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LapsSectionPreview() {
    val lapsSectionLaps = listOf(
        ViewLap(index = 2, milliseconds = "01:15:09", status = LapStatus.CURRENT),
        ViewLap(index = 1, milliseconds = "01:16:35", status = LapStatus.DONE),
    )

    StopwatchTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LapsSection(lapsSectionLaps)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LapsSectionSeeAllPreview() {
    val lapsSectionLaps = listOf(
        ViewLap(index = 3, milliseconds = "01:16:11", status = LapStatus.CURRENT),
        ViewLap(index = 2, milliseconds = "01:15:09", status = LapStatus.BEST),
        ViewLap(index = 1, milliseconds = "01:16:35", status = LapStatus.WORST)
    )

    StopwatchTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LapsSection(lapsSectionLaps) {}
        }
    }
}