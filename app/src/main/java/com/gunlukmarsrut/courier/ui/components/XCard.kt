package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.data.formatAzPhone
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** A pulled-out address on the "22.09.26 X" screen. */
@Composable
fun XCard(
    stop: Stop,
    canReturn: Boolean,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
    onReturn: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.border),
    ) {
        Column(modifier = Modifier.padding(Spacing.cardPadding)) {
            Row(verticalAlignment = Alignment.Top) {
                StopNumberCircle(number = stop.orderNumber)
                Spacer(Modifier.width(Spacing.sm))
                Column(modifier = Modifier.weight(1f)) {
                    Text(stop.fullName, style = AppType.title, color = colors.textPrimary)
                    Spacer(Modifier.height(Spacing.xxs))
                    Text(formatAzPhone(stop.phone), style = AppType.caption, color = colors.textSecondary)
                }
            }

            if (!stop.xReason.isNullOrBlank()) {
                Spacer(Modifier.height(Spacing.sm))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.dangerContainer, RoundedCornerShape(10.dp))
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                ) {
                    Text(stop.xReason, style = AppType.body, color = colors.onDangerContainer)
                }
            }

            Spacer(Modifier.height(Spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                SecondaryButton(text = "Redaktə et", onClick = onEdit, modifier = Modifier.weight(1f))
                if (canReturn) {
                    TransferButton(text = "Geri qaytar", onClick = onReturn, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
