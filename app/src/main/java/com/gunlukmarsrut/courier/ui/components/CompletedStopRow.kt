package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.data.StopStatus
import com.gunlukmarsrut.courier.data.formatAzPhone
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** Compact row inside the collapsed "Verildi" / "Keçirildi" sections at the bottom of the stop list. */
@Composable
fun CompletedStopRow(
    stop: Stop,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    val isTransferred = stop.status == StopStatus.TRANSFERRED
    val bg = if (isTransferred) colors.transferContainer else colors.surface2
    val fg = if (isTransferred) colors.onTransferContainer else colors.textPrimary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StopNumberCircle(number = stop.orderNumber)
        Spacer(Modifier.width(Spacing.sm))

        Column(modifier = Modifier.weight(1f)) {
            Text(stop.fullName, style = AppType.bodyMedium, color = fg)
            Text(formatAzPhone(stop.phone), style = AppType.caption, color = colors.textMuted)
        }

        Spacer(Modifier.width(Spacing.xs))

        if (isTransferred) {
            Text("→ ${stop.transferredToDate}", style = AppType.captionMedium, color = colors.onTransferContainer)
        } else if (stop.photoCount > 0) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Filled.Photo, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(16.dp))
                Text(stop.photoCount.toString(), style = AppType.caption, color = colors.textMuted)
            }
        } else {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = colors.success, modifier = Modifier.size(20.dp))
        }
    }
}
