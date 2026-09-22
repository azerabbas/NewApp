package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.PillShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

@Composable
private fun Pill(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(containerColor, PillShape)
            .padding(horizontal = Spacing.sm, vertical = 4.dp),
    ) {
        Text(text, style = AppType.captionMedium, color = contentColor)
    }
}

@Composable
fun DayStateBadge(state: DayState, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    val (bg, fg, label) = when (state) {
        DayState.ACTIVE -> Triple(colors.primary, colors.onPrimary, "Aktiv")
        DayState.UPCOMING -> Triple(colors.transfer, colors.onTransfer, "Gələcək")
        DayState.FINISHED -> Triple(colors.finished, colors.onFinished, "Bitib")
    }
    Pill(text = label, containerColor = bg, contentColor = fg, modifier = modifier)
}

/** "22.09.26-dan keçib" — sits under the customer name on a carried-over stop. */
@Composable
fun TransferredFromChip(date: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier
            .background(colors.transferContainer, PillShape)
            .padding(horizontal = Spacing.sm, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = colors.onTransferContainer, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(4.dp))
        Text("$date-dan keçib", style = AppType.captionMedium, color = colors.onTransferContainer)
    }
}

/** Red badge with a count — the X-list link, hidden by the caller when count is 0. */
@Composable
fun XCountBadge(count: Int, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    Pill(text = "X · $count", containerColor = colors.dangerContainer, contentColor = colors.onDangerContainer, modifier = modifier)
}

/** Amber-tinted block with a note icon — always visible when a stop has a note. */
@Composable
fun NoteBanner(text: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier
            .background(colors.warningContainer, RoundedCornerShape(10.dp))
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            Icons.Filled.Notes,
            contentDescription = null,
            tint = colors.onWarningContainer,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(Spacing.xs))
        Text(text, style = AppType.body.copy(fontSize = 14.sp, lineHeight = 20.sp), color = colors.onWarningContainer)
    }
}
