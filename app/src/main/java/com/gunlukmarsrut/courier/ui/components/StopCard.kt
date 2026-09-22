package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.data.StopStatus
import com.gunlukmarsrut.courier.data.formatAzPhone
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.PillShape
import com.gunlukmarsrut.courier.ui.theme.Sizing
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/**
 * The main working unit of the Day screen: name, note, phone, call/WhatsApp,
 * photo/deliver/fail actions. [isNext] renders the larger, highlighted
 * "Növbəti" treatment. Actions that mutate the day (deliver, fail, transfer,
 * add photo) only show when [dayState] is ACTIVE.
 */
@Composable
fun StopCard(
    stop: Stop,
    dayState: DayState,
    modifier: Modifier = Modifier,
    isNext: Boolean = false,
    onCardClick: () -> Unit = {},
    onCall: () -> Unit = {},
    onWhatsApp: () -> Unit = {},
    onAddPhoto: () -> Unit = {},
    onDeliver: () -> Unit = {},
    onDeliverBlocked: () -> Unit = {},
    onCannotDeliver: () -> Unit = {},
    onEdit: () -> Unit = {},
    onTransfer: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    val canAct = dayState == DayState.ACTIVE
    var menuOpen by remember { mutableStateOf(false) }
    val canDeliver = stop.photoCount > 0 || stop.transferredFromDate != null

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = if (isNext) BorderStroke(2.dp, colors.primary) else BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 3.dp else 0.dp),
    ) {
        Box {
            Column(modifier = Modifier.padding(Spacing.cardPadding)) {
                if (isNext) {
                    Text("NÖVBƏTİ", style = AppType.captionMedium, color = colors.primary)
                    Spacer(Modifier.height(Spacing.xs))
                }

                Row(verticalAlignment = Alignment.Top) {
                    StopNumberCircle(number = stop.orderNumber, emphasized = isNext)
                    Spacer(Modifier.width(Spacing.sm))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(stop.fullName, style = AppType.title, color = colors.textPrimary)

                        if (stop.transferredFromDate != null) {
                            Spacer(Modifier.height(Spacing.xxs))
                            TransferredFromChip(date = stop.transferredFromDate)
                        }

                        if (!stop.note.isNullOrBlank()) {
                            Spacer(Modifier.height(Spacing.xs))
                            NoteBanner(text = stop.note)
                        }

                        Spacer(Modifier.height(Spacing.xs))
                        Text(formatAzPhone(stop.phone), style = AppType.caption, color = colors.textSecondary)
                    }

                    Spacer(Modifier.width(Spacing.xs))

                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        RoundIconButton(
                            icon = Icons.Filled.Call,
                            contentDescription = "Zəng et",
                            onClick = onCall,
                            containerColor = colors.primaryContainer,
                            contentColor = colors.primary,
                        )
                        RoundIconButton(
                            icon = Icons.Filled.Chat,
                            contentDescription = "WhatsApp",
                            onClick = onWhatsApp,
                            containerColor = colors.whatsapp,
                            contentColor = colors.onWhatsapp,
                        )
                    }
                }

                if (canAct) {
                    Spacer(Modifier.height(Spacing.sm))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        PhotoPickerChip(count = stop.photoCount, onClick = onAddPhoto, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(Spacing.xs))
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        CompactSolidButton(
                            text = "Verildi",
                            onClick = { if (canDeliver) onDeliver() else onDeliverBlocked() },
                            containerColor = if (canDeliver) colors.success else colors.surface2,
                            contentColor = if (canDeliver) colors.onSuccess else colors.textMuted,
                            modifier = Modifier.weight(1f),
                        )
                        DangerOutlinedButton(
                            text = "Verilə bilmədi",
                            onClick = onCannotDeliver,
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else if (stop.photoCount > 0) {
                    Spacer(Modifier.height(Spacing.sm))
                    Text("${stop.photoCount} şəkil əlavə edilib", style = AppType.caption, color = colors.textMuted)
                }
            }

            Row(modifier = Modifier.align(Alignment.TopEnd).padding(6.dp)) {
                if (canAct) {
                    IconButton(onClick = onCannotDeliver, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "X et", tint = colors.textMuted, modifier = Modifier.size(18.dp))
                    }
                }
                Box {
                    IconButton(onClick = { menuOpen = true }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Daha çox", tint = colors.textMuted, modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        DropdownMenuItem(
                            text = { Text("Redaktə et", style = AppType.body) },
                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                            onClick = { menuOpen = false; onEdit() },
                        )
                        if (canAct) {
                            DropdownMenuItem(
                                text = { Text("Başqa günə keçir", style = AppType.body, color = colors.transfer) },
                                leadingIcon = { Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = colors.transfer) },
                                onClick = { menuOpen = false; onTransfer() },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StopNumberCircle(number: Int, modifier: Modifier = Modifier, emphasized: Boolean = false) {
    val colors = MaterialTheme.appColors
    Box(
        modifier = modifier
            .size(Sizing.stopNumberCircle)
            .background(if (emphasized) colors.primary else colors.primary.copy(alpha = 0.92f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(number.toString(), style = AppType.stopNumber, color = colors.onPrimary)
    }
}

@Composable
private fun PhotoPickerChip(count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier
            .clip(PillShape)
            .background(if (count > 0) colors.successContainer else colors.surface2)
            .clickable { onClick() }
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Icon(
            Icons.Filled.AddAPhoto,
            contentDescription = null,
            tint = if (count > 0) colors.onSuccessContainer else colors.textSecondary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = if (count > 0) "Şəkil · $count" else "+ Şəkil",
            style = AppType.label,
            color = if (count > 0) colors.onSuccessContainer else colors.textSecondary,
        )
    }
}
