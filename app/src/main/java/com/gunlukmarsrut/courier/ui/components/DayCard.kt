package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.Day
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.data.completedCount
import com.gunlukmarsrut.courier.data.incomingTransferCount
import com.gunlukmarsrut.courier.data.progress
import com.gunlukmarsrut.courier.data.totalCount
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

@Composable
fun DayCard(
    day: Day,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    val isActive = day.state == DayState.ACTIVE
    val isFinished = day.state == DayState.FINISHED

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = CardShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) colors.primaryContainer else colors.surface,
        ),
        border = when {
            isActive -> BorderStroke(2.dp, colors.primary)
            else -> BorderStroke(1.dp, colors.border)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFinished) 0.dp else 1.dp),
    ) {
        Column(modifier = Modifier.padding(Spacing.cardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    day.title,
                    style = AppType.title,
                    color = if (isFinished) colors.textSecondary else colors.textPrimary,
                    modifier = Modifier.weight(1f),
                )
                DayStateBadge(state = day.state)
            }

            Spacer(Modifier.height(Spacing.xs))

            val stopsLabel = if (day.totalCount == 1) "1 ünvan" else "${day.totalCount} ünvan"
            Text(stopsLabel, style = AppType.caption, color = colors.textMuted)

            if (day.state == DayState.UPCOMING && day.incomingTransferCount > 0) {
                Spacer(Modifier.height(Spacing.xxs))
                Text(
                    "${day.incomingTransferCount} keçirilmiş bağlama",
                    style = AppType.captionMedium,
                    color = colors.transfer,
                )
            }

            Spacer(Modifier.height(Spacing.sm))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${day.completedCount} / ${day.totalCount} tamamlandı",
                    style = AppType.caption,
                    color = colors.textSecondary,
                )
            }
            Spacer(Modifier.height(Spacing.xxs))
            LinearProgressIndicator(
                progress = { day.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isFinished) colors.finished else colors.primary,
                trackColor = colors.surface2,
            )
        }
    }
}

/** The small red-tinted card attached under a day that has an X list. */
@Composable
fun DayXLinkCard(
    dayTitle: String,
    xCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.dangerContainer)
            .clickable { onClick() }
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "$dayTitle X · $xCount ünvan",
            style = AppType.label,
            color = colors.onDangerContainer,
            modifier = Modifier.weight(1f),
        )
    }
}
