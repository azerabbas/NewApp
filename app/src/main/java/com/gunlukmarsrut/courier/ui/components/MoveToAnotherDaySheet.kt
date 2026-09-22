package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors
import java.text.SimpleDateFormat
import java.util.Locale

/** g) Move to another day: quick chips, a photo-required guard, then "Keçir". */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveToAnotherDaySheet(
    customerName: String,
    tomorrowDateLabel: String,
    dayAfterDateLabel: String,
    hasPhoto: Boolean,
    onDismissRequest: () -> Unit,
    onAddPhoto: () -> Unit,
    onConfirm: (targetDateLabel: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    var selected by remember { mutableStateOf<String?>(null) }
    var showCalendar by remember { mutableStateOf(false) }

    AppBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Text("Başqa günə keçir", style = AppType.title, color = colors.textPrimary)
        Spacer(Modifier.height(Spacing.xxs))
        Text(customerName, style = AppType.body, color = colors.textSecondary)
        Spacer(Modifier.height(Spacing.md))

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            SelectableChip(
                text = "Sabah ($tomorrowDateLabel)",
                selected = selected == tomorrowDateLabel,
                onClick = { selected = tomorrowDateLabel },
            )
            SelectableChip(
                text = "Birigün ($dayAfterDateLabel)",
                selected = selected == dayAfterDateLabel,
                onClick = { selected = dayAfterDateLabel },
            )
            SelectableChip(
                text = "Başqa tarix",
                selected = selected != null && selected != tomorrowDateLabel && selected != dayAfterDateLabel,
                onClick = { showCalendar = true },
            )
        }

        if (!hasPhoto) {
            Spacer(Modifier.height(Spacing.md))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.warningContainer, RoundedCornerShape(12.dp))
                    .padding(Spacing.md),
            ) {
                Text(
                    "Keçirmək üçün əvvəlcə bağlamanın şəklini çəkin",
                    style = AppType.body,
                    color = colors.onWarningContainer,
                )
                Spacer(Modifier.height(Spacing.sm))
                SecondaryButton(text = "+ Şəkil", onClick = onAddPhoto)
            }
        }

        Spacer(Modifier.height(Spacing.md))
        Text("Şəkil bu günün göndərişinə düşəcək", style = AppType.caption, color = colors.textMuted)
        Spacer(Modifier.height(Spacing.lg))

        TransferButton(
            text = "Keçir",
            onClick = { selected?.let(onConfirm) },
            enabled = hasPhoto && selected != null,
        )
    }

    if (showCalendar) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showCalendar = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        val fmt = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
                        selected = fmt.format(java.util.Date(millis))
                    }
                    showCalendar = false
                }) { Text("Təsdiqlə") }
            },
            dismissButton = {
                TextButton(onClick = { showCalendar = false }) { Text("Ləğv et") }
            },
        ) {
            DatePicker(state = state)
        }
    }
}
