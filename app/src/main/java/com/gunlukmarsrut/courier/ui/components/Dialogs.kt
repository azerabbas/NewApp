package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** Generic confirm dialog: title, body text, an optional amber warning block, Ləğv et / confirm. */
@Composable
fun ConfirmDialog(
    title: String,
    onDismiss: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    dismissText: String = "Ləğv et",
    warning: String? = null,
    warningAction: (@Composable () -> Unit)? = null,
    confirmColor: androidx.compose.ui.graphics.Color = MaterialTheme.appColors.primary,
    confirmContentColor: androidx.compose.ui.graphics.Color = MaterialTheme.appColors.onPrimary,
) {
    val colors = MaterialTheme.appColors
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = CardShape,
        containerColor = colors.surface,
        title = { Text(title, style = AppType.title, color = colors.textPrimary) },
        text = {
            Column {
                if (text != null) {
                    Text(text, style = AppType.body, color = colors.textSecondary)
                }
                if (warning != null) {
                    Spacer(Modifier.height(Spacing.sm))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.warningContainer, RoundedCornerShape(10.dp))
                            .padding(Spacing.sm),
                    ) {
                        Text(warning, style = AppType.body, color = colors.onWarningContainer)
                        if (warningAction != null) {
                            Spacer(Modifier.height(Spacing.xs))
                            warningAction()
                        }
                    }
                }
            }
        },
        confirmButton = {
            CompactSolidButton(
                text = confirmText,
                onClick = onConfirm,
                containerColor = confirmColor,
                contentColor = confirmContentColor,
            )
        },
        dismissButton = {
            GhostButton(text = dismissText, onClick = onDismiss)
        },
    )
}

/** "23.09.26 aktiv edilsin?" — h) in the spec. */
@Composable
fun ActivateDayDialog(
    newDayTitle: String,
    currentActiveDayTitle: String?,
    unfinishedCount: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onLookFirst: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val bodyText = if (currentActiveDayTitle != null) {
        "$currentActiveDayTitle bitəcək. Ona baxmaq və şəkilləri göndərmək mümkün qalacaq."
    } else null

    ConfirmDialog(
        title = "$newDayTitle aktiv edilsin?",
        text = bodyText,
        onDismiss = onDismiss,
        confirmText = "Aktiv et",
        onConfirm = onConfirm,
        modifier = modifier,
        warning = if (unfinishedCount > 0 && currentActiveDayTitle != null) {
            "$currentActiveDayTitle-da $unfinishedCount ünvan tamamlanmayıb"
        } else null,
        warningAction = if (unfinishedCount > 0 && currentActiveDayTitle != null) {
            { GhostButton(text = "Əvvəlcə bax", onClick = onLookFirst, contentColor = colors.onWarningContainer) }
        } else null,
    )
}
