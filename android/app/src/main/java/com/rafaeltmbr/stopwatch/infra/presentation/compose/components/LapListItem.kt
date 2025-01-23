package com.rafaeltmbr.stopwatch.infra.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafaeltmbr.stopwatch.R
import com.rafaeltmbr.stopwatch.domain.entities.Lap
import com.rafaeltmbr.stopwatch.infra.presentation.compose.theme.StopwatchTheme
import com.rafaeltmbr.stopwatch.infra.presentation.entities.ViewLap

@Composable
fun LapListItem(
    lap: ViewLap,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        if (lap.status != Lap.Status.CURRENT) {
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
            val (color, statusText) = when (lap.status) {
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
                    text = "#${lap.index}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Text(
                text = lap.time,
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

@Preview(showBackground = true)
@Composable
private fun LapsSectionPreview() {
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
            lapsSectionLaps.forEach {
                LapListItem(lap = it)
            }
        }
    }
}
